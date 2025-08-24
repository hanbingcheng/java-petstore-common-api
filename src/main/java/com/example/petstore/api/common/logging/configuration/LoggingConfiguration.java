package com.example.petstore.api.common.logging.configuration;

import com.example.petstore.api.common.logging.AppLogger;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.example.petstore.api.common.logging")
public class LoggingConfiguration {

  /**
   * com.example.petstore.api.common.logging.configuration slf4jのLoggerをBean登録する.
   *
   * @param ip InjectionPoint
   * @return Logger
   */
  @Bean
  @Scope("prototype")
  public Logger slfjLogger(InjectionPoint ip) {

    return Optional.ofNullable(ip.getMethodParameter())
        .map(MethodParameter::getContainingClass)
        .map(LoggerFactory::getLogger)
        .orElseThrow(() -> new BeanCreationException("Cannot find type for logger."));
  }

  /**
   * AppLoggerをBean登録する.
   *
   * @param ip InjectionPoint
   * @param messageSource メッセージソース.
   * @param textEncryptor TextEncryptor
   * @return AppLogger
   */
  @Bean
  @Scope("prototype")
  public AppLogger appLogger(InjectionPoint ip, MessageSource messageSource) {

    return Optional.ofNullable(ip.getMethodParameter())
        .map(MethodParameter::getContainingClass)
        .map(LoggerFactory::getLogger)
        .map(logger -> new AppLogger(logger, messageSource))
        .orElseThrow(() -> new BeanCreationException("Cannot find type for logger."));
  }
}
