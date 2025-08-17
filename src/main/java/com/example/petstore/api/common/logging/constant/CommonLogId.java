package com.example.petstore.api.common.logging.constant;

public class CommonLogId {

  /**
   * 処理開始ログ. <br>
   * 0:メソッドタグ(クラス名+メソッド名)<br>
   * 1:メソッドパラメータ<br>
   */
  public static final String START_LOG = "Z001I0001";

  /**
   * 処理終了ログ.<br>
   * 0:メソッドタグ(サービスクラス名+"#"+メソッド名)<br>
   * 1:処理時間(単位：msec)<br>
   * 2:処理結果(true: 成功、false: 例外発生)<br>
   * 3:処理結果(voidメソッドの場合は、null固定)<br>
   */
  public static final String END_LOG = "Z001LI0002";
}
