package com.example.petstore.api.common.logging.interceptor;

import com.example.petstore.api.common.logging.AppLogger;
import com.example.petstore.api.common.logging.annotation.StartEndLog;
import com.example.petstore.api.common.logging.constant.CommonLogId;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class StartEndLoggingInterceptor {

  private final AppLogger logger;

  @Around("@annotation(com.example.petstore.api.common.logging.annotation.StartEndLog)")
  public Object around(ProceedingJoinPoint pj) throws Throwable {

    if (pj instanceof MethodInvocationProceedingJoinPoint) {

      // メソッドに対するアノテーションのみをここで処理する
      MethodInvocationProceedingJoinPoint mipj = MethodInvocationProceedingJoinPoint.class.cast(pj);

      String methodName = "unknown";
      boolean isResponseDetail = true;
      Object target = pj.getTarget();
      if (target != null) {
        MethodSignature signature = (MethodSignature) mipj.getSignature();
        Method method = signature.getMethod();
        StartEndLog annoMethod = method.getAnnotation(StartEndLog.class);

        methodName = target.getClass().getSimpleName() + "#" + signature.getName();
        isResponseDetail = annoMethod.isResponseDetail();
      }

      // 開始終了ログ＋本処理実行
      return loggingAndProceed(pj, methodName, isResponseDetail);

    } else {
      return pj.proceed();
    }
  }

  /**
   * 開始終了ログ、本処理実行処理
   *
   * @param pj 本処理
   * @param methodName メソッド名
   * @param isResponseDetail レスポンス詳細フラグ
   * @return 処理結果
   * @throws Throwable 例外全般
   */
  private Object loggingAndProceed(
      ProceedingJoinPoint pj, String methodName, boolean isResponseDetail) throws Throwable {

    long start = 0;

    boolean success = true;
    Object result = null;
    try {
      // 開始ログ出力
      logger.info(CommonLogId.START_LOG, methodName, Arrays.asList(pj.getArgs()));

      // 処理開始時取得
      start = System.currentTimeMillis();

      // 本処理
      result = pj.proceed();

    } catch (Throwable e) {
      success = false;

      throw e;

    } finally {

      // 処理時間計測
      long processingTime = System.currentTimeMillis() - start;

      // 終了ログ出力
      if (!isResponseDetail) {
        result = "-";
      }
      logger.info(CommonLogId.END_LOG, methodName, String.valueOf(processingTime), success, result);
    }

    return result;
  }
}
