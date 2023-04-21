package riot.api.data.engineer.entity.api;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "api_logs")
public class ApiLogs {

    /** 시리얼 **/
    @Id
    @Column(name = "api_logs_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    /** ApiInfo 테이블 아이디 **/
    @Column(name = "api_info_id")
    Long apiInfoId;
    /** 타임스탬프  **/
    @Column(name = "call_timestamp")
    Timestamp callTimestamp;
    /** 호출 url **/
    @Column(name = "api_url")
    String apiUrl;
    /** 파라미터 스트링 **/
    @Column(name = "request_params")
    String requestParams;
    /** 리스폰스 **/
    @Column(name = "response")
    String response;
    /** 상태 코드 **/
    @Column(name = "status_code")
    String statusCode;
}
