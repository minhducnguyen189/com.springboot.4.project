package com.springboot.project;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mongodb.MongoDBContainer;

/** MongoDB Testcontainer wired into Spring Boot through {@link ServiceConnection}. */
@TestConfiguration(proxyBeanMethods = false)
public class MongoContainerConfiguration {

    /**
     * MongoDB 8.0+ (new TCMalloc) refuses to start on Linux kernels 6.19 to 7.0.13 (SERVER-121912),
     * which includes the dev machine; 7.0 is unaffected and covers every feature this module uses.
     */
    private static final String MONGO_IMAGE = "mongo:7.0";

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return new MongoDBContainer(MONGO_IMAGE);
    }
}
