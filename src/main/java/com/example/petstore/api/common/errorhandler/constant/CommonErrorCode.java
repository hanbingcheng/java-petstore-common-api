package com.example.petstore.api.common.errorhandler.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 共通結果コード */
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
  /** パラメータエラー. */
  REQUEST_PARAMETER_ERROR("Z001MW00001"),
  /** リクエスト不正. */
  INVALID_REQUEST("Z001MW00002"),
  /** リクエストされたリソースが見つからない. */
  NOT_FOUND_ERROR("Z001MW00003"),
  /** DBアクセスエラー. */
  DBACCESS_ERROR("Z001ME0001"),
  /** 想定外エラー. */
  UNEXPECTED_ERROR("Z001ME9999");

  /** 結果コード. */
  private final String code;
}
