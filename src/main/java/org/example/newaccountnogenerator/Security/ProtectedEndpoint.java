package org.example.newaccountnogenerator.Security;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProtectedEndpoint {
}
