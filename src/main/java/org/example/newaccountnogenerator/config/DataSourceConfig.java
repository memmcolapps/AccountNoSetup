package org.example.newaccountnogenerator.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableJpaRepositories(
        basePackages = "org.example.newaccountnogenerator.repository",  // ✅ FIXED: repository package
        entityManagerFactoryRef = "consolidatedEntityManagerFactory",
        transactionManagerRef = "consolidatedTxManager"
)
public class DataSourceConfig {
    // -------------------------------------------------------------------
    // Consolidated DataSource Configuration
    // -------------------------------------------------------------------
    @Bean(name = "consolidatedDataSource")
    @ConfigurationProperties(prefix = "consolidated.datasource")
    public DataSource consolidatedDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "consolidatedEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean consolidatedEntityManagerFactory(
            @Qualifier("consolidatedDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("org.example.newaccountnogenerator.model");  // ✅ entities here
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "validate");
        props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        props.put("hibernate.show_sql", false);

        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean(name = "consolidatedTxManager")
    public PlatformTransactionManager consolidatedTxManager(
            @Qualifier("consolidatedEntityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf.getObject()));
    }

    // -------------------------------------------------------------------
    // Business Databases (Dynamic)
    // -------------------------------------------------------------------
    @Autowired
    private Environment env;

    @Bean
    public Map<String, DataSource> businessDataSources() {
        Map<String, DataSource> sources = new HashMap<>();
        String list = env.getProperty("business-dbs.list");
        if (list == null || list.isBlank()) {
            System.err.println("⚠️ No business-dbs.list defined in properties file!");
            return sources;
        }

        for (String bu : list.split(",")) {
            bu = bu.trim();
            if (bu.isEmpty()) continue;

            String base = "business-dbs." + bu;
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(env.getProperty(base + ".jdbc-url"));
            cfg.setUsername(env.getProperty(base + ".username"));
            cfg.setPassword(env.getProperty(base + ".password"));
            cfg.setDriverClassName(env.getProperty(base + ".driver-class-name",
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"));

            // ✅ Apply global tuning defaults
            cfg.setMaximumPoolSize(env.getProperty("spring.datasource.hikari.maximum-pool-size", Integer.class, 60));
            cfg.setMinimumIdle(env.getProperty("spring.datasource.hikari.minimum-idle", Integer.class, 10));
            cfg.setIdleTimeout(env.getProperty("spring.datasource.hikari.idle-timeout", Long.class, 30000L));
            cfg.setConnectionTimeout(env.getProperty("spring.datasource.hikari.connection-timeout", Long.class, 20000L));
            cfg.setMaxLifetime(env.getProperty("spring.datasource.hikari.max-lifetime", Long.class, 600000L));
            cfg.setPoolName("HikariPool-" + bu.toUpperCase());

            sources.put(bu, new HikariDataSource(cfg));
        }

        return sources;
    }

    @Bean
    public Map<String, JdbcTemplate> businessJdbcTemplates(Map<String, DataSource> businessDataSources) {
        Map<String, JdbcTemplate> templates = new HashMap<>();
        businessDataSources.forEach((bu, ds) -> templates.put(bu, new JdbcTemplate(ds)));
        return templates;
    }
}