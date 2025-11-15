package com.localhelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestApplication {
    
    public static void main(String[] args) {
        SpringApplication.from(LocalHelperApp::main)
                .with(TestApplication.class)
                .run(args);
    }
}