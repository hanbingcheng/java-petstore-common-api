package com.example.petstore.api.common.errorhandler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDetail {
  private String field;
  private String message;
  private Object rejectedValue;
}
