package riot.api.data.engineer.entity;

import java.sql.Timestamp;

public class ApiLogs {

    /** 시리얼 **/
    Long id;
    /** ApiInfo 테이블 아이디 **/
    Long apiInfoId;
    /** 타임스탬프  **/
    Timestamp callTimestamp;
    /** 호출 url **/
    String apiUrl;
    /** 파라미터 스트링 **/
    String requestParams;
    /** 리스폰스 **/
    String response;
    /** 상태 코드 **/
    String statusCode;
}
