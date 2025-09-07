package com.example.petstore.api.common.base.configuration;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@ComponentScan("com.example.petstore.api.common.base")
public class BaseConfiguration implements WebMvcConfigurer {
  @Override
  public void addFormatters(FormatterRegistry registry) {
    ApplicationConversionService.configure(registry);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**") // すべてのエンドポイントを対象
        .allowedOriginPatterns("http://localhost:[*]") // 許可するオリジンを指定（複数可）
        .allowedMethods("GET", "POST", "PUT", "DELETE") // 許可するHTTPメソッド
        .allowedHeaders("*") // 許可するリクエストヘッダー
        .allowCredentials(false) // クレデンシャルの許可（CookieやAuthorizationヘッダーなど）
        .maxAge(3600); // プリフライトリクエストの結果をキャッシュする時間（秒）
  }
}
