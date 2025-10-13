package com.example.petstore.api.common.base.crypt.interceptor;

import com.example.petstore.api.common.base.crypt.annotation.Encrypted;
import com.example.petstore.api.common.base.crypt.cryptor.Encryptor;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;
import lombok.AllArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Intercepts({
  @Signature(
      type = Executor.class,
      method = "update",
      args = {MappedStatement.class, Object.class}),
  @Signature(
      type = ResultSetHandler.class,
      method = "handleResultSets",
      args = {Statement.class})
})
public class MybatisCryptInterceptor implements Interceptor {

  private static final Set<String> ENCRYPTED_FIELDS =
      Set.of(
          "phone", "credit_card", "personal_info" // 暗号化対象カラム名
          );

  private final Encryptor encryptor;

  @Override
  public Object intercept(Invocation invocation) throws Throwable {

    Object target = invocation.getTarget();

    // INSERT/UPDATE 時の暗号化処理
    if (target instanceof Executor) {
      processEncryption(invocation.getArgs()[1]);
    }
    // SELECT 時の復号処理
    else if (target instanceof ResultSetHandler) {
      Object result = invocation.proceed();
      if (result instanceof List) {
        for (Object item : (List<?>) result) {
          processDecryption(item);
        }
      } else {
        processDecryption(result);
      }
      return result;
    }

    return invocation.proceed();
  }

  private void processEncryption(Object parameterObject) {
    if (parameterObject == null) return;

    if (parameterObject instanceof Map) {
      // パラメータがMapの場合
      processMapEncryption((Map<?, ?>) parameterObject);
    } else {
      // パラメータがエンティティオブジェクトの場合
      processEntityEncryption(parameterObject);
    }
  }

  private void processMapEncryption(Map<?, ?> paramMap) {
    Map<String, Object> mutableMap = (Map<String, Object>) paramMap;
    for (String key : mutableMap.keySet()) {
      if (ENCRYPTED_FIELDS.contains(key) && mutableMap.get(key) instanceof String) {
        String value = (String) mutableMap.get(key);
        if (value != null && !value.startsWith("enc:")) {
          mutableMap.put(key, "enc:" + encryptor.encrypt(value));
        }
      }
    }
  }

  private void processEntityEncryption(Object entity) {
    Class<?> clazz = entity.getClass();
    for (Field field : getAllFields(clazz)) {
      if (field.isAnnotationPresent(Encrypted.class)) {
        field.setAccessible(true);
        try {
          Object value = field.get(entity);
          if (value instanceof String stringValue
              && stringValue != null
              && !stringValue.startsWith("enc:")) {
            field.set(entity, "enc:" + encryptor.encrypt(stringValue));
          }
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Failed to encrypt field: " + field.getName(), e);
        }
      }
    }
  }

  private void processDecryption(Object resultObject) {
    if (resultObject == null) return;

    Class<?> clazz = resultObject.getClass();
    for (Field field : getAllFields(clazz)) {
      if (ENCRYPTED_FIELDS.contains(field.getName())
          || field.isAnnotationPresent(Encrypted.class)) {
        field.setAccessible(true);
        try {
          Object value = field.get(resultObject);
          if (value instanceof String stringValue
              && stringValue != null
              && stringValue.startsWith("enc:")) {
            String encryptedValue = stringValue.substring(4); // "enc:" を除去
            field.set(resultObject, encryptor.decrypt(encryptedValue));
          }
        } catch (IllegalAccessException e) {
          throw new RuntimeException("Failed to decrypt field: " + field.getName(), e);
        }
      }
    }
  }

  private List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();
    while (clazz != null) {
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      clazz = clazz.getSuperclass();
    }
    return fields;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
    // プロパティ設定が必要な場合
  }
}
