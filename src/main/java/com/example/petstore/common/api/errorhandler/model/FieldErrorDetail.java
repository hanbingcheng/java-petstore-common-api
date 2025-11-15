package com.example.petstore.common.api.errorhandler.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDetail {
  private String field;
  private String message;
  private Object rejectedValue;
}
