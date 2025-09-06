package com.example.petstore.api.common.logging.constant;

public class CommonLogId {

  /**
   * 処理開始ログ. <br>
   * 0:メソッドタグ(クラス名+メソッド名)<br>
   * 1:メソッドパラメータ<br>
   */
  public static final String START_LOG = "Z001LI0001";

  /**
   * 処理終了ログ.<br>
   * 0:メソッドタグ(サービスクラス名+"#"+メソッド名)<br>
   * 1:処理時間(単位：msec)<br>
   * 2:処理結果(true: 成功、false: 例外発生)<br>
   * 3:処理結果(voidメソッドの場合は、null固定)<br>
   */
  public static final String END_LOG = "Z001LI0002";

  /**
   * リクエストパラメータエラー発生ログ. <br>
   * 0:エラーとなった項目<br>
   * 1:エラーとなった値<br>
   * 2:例外クラス<br>
   * 3:例外メッセージ<br>
   */
  public static final String REQUEST_PARAMETER_ERROR = "Z001LW0001";

  /**
   * 無効なリクエスト受信時ログ. <br>
   * 0:例外クラス<br>
   * 1:例外メッセージ<br>
   */
  public static final String INVALID_REQUEST = "Z001LW0002";

  /**
   * DBアクセスエラー発生ログ. <br>
   * 0:リポジトリ名<br>
   * 1:操作<br>
   */
  public static final String DB_ACCESS_ERROR = "Z001LE0001";

  /**
   * DBトランザクションエラー発生ログ. <br>
   * 0:例外クラス<br>
   * 1:例外メッセージ<br>
   */
  public static final String DB_TRANSACTION_ERROR = "Z001LE0002";

  /**
   * 想定外エラー発生ログ. <br>
   * 0:例外クラス<br>
   * 1:例外メッセージ<br>
   */
  public static final String UN_EXPECTED_ERROR = "Z001LE0999";
}
