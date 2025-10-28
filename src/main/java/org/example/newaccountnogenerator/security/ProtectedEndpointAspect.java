package org.example.newaccountnogenerator.security;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.newaccountnogenerator.model.AccessGroup;
import org.example.newaccountnogenerator.repository.AccessGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class ProtectedEndpointAspect {

    private static final Logger log = LoggerFactory.getLogger(ProtectedEndpointAspect.class);

    @Autowired
    private AccessGroupRepository accessGroupRepository;

    @Around("@annotation(org.example.newaccountnogenerator.security.ProtectedEndpoint)")
    public Object validateAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Extract PathVariables: accessGroupCode and token
        String accessGroupCode = null;
        String token = null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameters[i].getAnnotations()) {
                if (annotation.annotationType().equals(PathVariable.class)) {
                    PathVariable pv = (PathVariable) annotation;
                    if (pv.value().equals("accessGroupCode")) {
                        accessGroupCode = args[i].toString();
                    } else if (pv.value().equals("token")) {
                        token = args[i].toString();
                    }
                }
            }
        }

        if (accessGroupCode == null || token == null) {
            log.warn("Missing accessGroupCode or token in request");
            return unauthorized("Missing access group code or token.");
        }

        // Fetch AccessGroup by accessGroupCode + token
        Optional<AccessGroup> accessGroupOpt =
                accessGroupRepository.findByAccessGroupCodeAndToken(accessGroupCode, token);

        if (accessGroupOpt.isEmpty()) {
            log.warn("Invalid AccessGroupCode or Token: {}", accessGroupCode);
            return unauthorized("Invalid AccessGroupCode or Token.");
        }

        AccessGroup group = accessGroupOpt.get();
        if (!Boolean.TRUE.equals(group.getActive())) {
            log.warn("AccessGroup inactive: {}", group.getAccessGroupCode());
            return unauthorized("Access group is inactive.");
        }

        // Extract the client IP (not the server IP)
        String clientIp = getClientIp(request);
        log.info("Client IP: {}, Allowed IP1: {}, IP2: {}", clientIp, group.getRequestIp(), group.getRequestIp2());

        boolean ipAllowed = false;
        if (clientIp.equalsIgnoreCase(group.getRequestIp())) {
            ipAllowed = true;
        } else if (group.getRequestIp2() != null && clientIp.equalsIgnoreCase(group.getRequestIp2())) {
            ipAllowed = true;
        }

        if (!ipAllowed) {
            log.warn("Unauthorized IP: {}", clientIp);
            return unauthorized("Unauthorized IP. This client IP is not whitelisted for the provided Access Group.");
        }

        // ✅ All checks passed — proceed
        return joinPoint.proceed();
    }

    private ResponseEntity<Map<String, Object>> unauthorized(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0];
        }
        return ip.trim();
    }
}
