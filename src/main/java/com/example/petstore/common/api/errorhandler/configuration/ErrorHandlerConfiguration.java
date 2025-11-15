package com.example.petstore.common.api.errorhandler.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.example.petstore.common.api.errorhandler")
public class ErrorHandlerConfiguration {}
