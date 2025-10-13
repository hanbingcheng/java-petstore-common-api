package com.example.petstore.api.common.base.crypt.reflect;

import com.example.petstore.api.common.base.crypt.annotation.Encrypted;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EncryptReflection {
  /** オブジェクト内の@Encryptedアノテーションが付いたフィールドを取得 */
  public List<Field> getEncryptedFields(Object obj) {
    List<Field> encryptedFields = new ArrayList<>();
    Class<?> clazz = obj.getClass();

    // 継承階層をたどってすべてのフィールドを検査
    while (clazz != null && clazz != Object.class) {
      for (Field field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Encrypted.class)) {
          encryptedFields.add(field);
        }
      }
      clazz = clazz.getSuperclass();
    }

    return encryptedFields;
  }

  /** フィールドの値を取得 */
  public Object getFieldValue(Field field, Object obj) {
    try {
      field.setAccessible(true);
      return field.get(obj);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Failed to get field value: " + field.getName(), e);
    }
  }

  /** フィールドに値を設定 */
  public void setFieldValue(Field field, Object obj, Object value) {
    try {
      field.setAccessible(true);
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Failed to set field value: " + field.getName(), e);
    }
  }
}
