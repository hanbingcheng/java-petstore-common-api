package com.example.petstore.api.common.converter.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.example.petstore.api.common.converter")
public class ConverterConfiguration {}
