package com.js;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

@EnableTransactionManagement
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.js.download.manager.mapper*")
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }


    private CorsConfiguration buildConfig() {

        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("Access-Control-Allow-Origin");
        exposedHeaders.add("Access-Control-Allow-Credentials");

        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("GET");
        allowedMethods.add("POST");

        List<String> allowedHeaders = new ArrayList<String>();
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("X-Requested-With");
        allowedHeaders.add("accept");
        allowedHeaders.add("Origin");
        allowedHeaders.add("Access-Control-Request-Method");
        allowedHeaders.add("Access-Control-Request-Headers");
        allowedHeaders.add("Accept");//这里设置要和前端js名字一样
        allowedHeaders.add("userToken");//这里设置要和前端js名字一样

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*"); // 1.允许任何域名使用
        corsConfiguration.setAllowedHeaders(allowedHeaders);
        corsConfiguration.setAllowedMethods(allowedMethods);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }
}
