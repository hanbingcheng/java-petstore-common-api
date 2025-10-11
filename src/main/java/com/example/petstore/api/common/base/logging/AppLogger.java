package com.example.petstore.api.common.base.logging;

import java.util.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/** ロガークラス. */
@AllArgsConstructor
public class AppLogger {

  /** MDCのキー. */
  private static final String MDC_KEY_CODE = "code";

  /** slf4jのロガー. */
  private final Logger logger;

  /** メッセージソース. */
  private final MessageSource messageSource;

  /**
   * @see org.slf4j.Logger
   */
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String msg) {
    this.logger.debug(msg);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String format, Object arg) {
    logger.debug(format, arg);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String format, Object... arguments) {
    logger.debug(format, arguments);
  }

  /**
   * @see org.slf4j.Logger
   */
  public void debug(String msg, Throwable t) {
    logger.debug(msg, t);
  }

  /**
   * codeに対応するメッセージのinfoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void info(String code) {
    String message = createMessage(code, null);

    infoInternal(code, message);
  }

  /**
   * codeに対応するメッセージのinfoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void info(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    infoInternal(code, message);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void warn(String code) {
    String message = createMessage(code, null);

    warnInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   */
  public void warn(String code, Throwable throwable) {
    String message = createMessage(code, null);

    warnInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void warn(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    warnInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのwarnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void warn(String code, Throwable throwable, Object... arguments) {
    String message = createMessage(code, arguments);

    warnInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   */
  public void error(String code) {
    String message = createMessage(code, null);

    errorInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   */
  public void error(String code, Throwable throwable) {
    String message = createMessage(code, null);

    errorInternal(code, message, throwable);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void error(String code, Object... arguments) {
    String message = createMessage(code, arguments);

    errorInternal(code, message, null);
  }

  /**
   * codeに対応するメッセージのerrorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param throwable スタックトレースを出力する例外.
   * @param arguments メッセージの埋め込み情報配列.
   */
  public void error(String code, Throwable throwable, Object... arguments) {
    String message = createMessage(code, arguments);

    errorInternal(code, message, throwable);
  }

  /**
   * コードに対応するメッセージを作成する.
   *
   * @param code ログメッセージに対応するコード.
   * @param arguments メッセージの埋め込み情報配列.
   * @return メッセージ.
   */
  private String createMessage(String code, Object[] arguments) {

    String result;
    try {
      result = messageSource.getMessage(code, arguments, Locale.getDefault());
    } catch (NoSuchMessageException e) {
      result = code;
    }

    return result;
  }

  /**
   * infoログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void infoInternal(String code, String message) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      logger.info(message);
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }

  /**
   * warnログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void warnInternal(String code, String message, Throwable throwable) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      if (throwable == null) {
        logger.warn(message);
      } else {
        logger.warn(message, throwable);
      }
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }

  /**
   * errorログを出力する.
   *
   * @param code ログメッセージに対応するコード.
   * @param message ログメッセージ.
   */
  private void errorInternal(String code, String message, Throwable throwable) {
    MDC.put(MDC_KEY_CODE, code);
    try {
      if (throwable == null) {
        logger.error(message);
      } else {
        logger.error(message, throwable);
      }
    } finally {
      MDC.remove(MDC_KEY_CODE);
    }
  }
}
