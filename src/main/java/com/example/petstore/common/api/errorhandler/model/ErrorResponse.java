package com.example.petstore.common.api.errorhandler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String errorCode;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String message;

  private String path;
  private LocalDateTime timestamp;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<FieldErrorDetail> fieldErrors;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String traceId;
}
