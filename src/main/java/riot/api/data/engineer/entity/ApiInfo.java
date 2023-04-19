package riot.api.data.engineer.entity;

public class ApiInfo {
    /** 시퀀스 **/
    Long id;
    /** 호출 url **/
    String apiUrl;
    String description;
    /** 키 **/
    String apiKey;
    /** 토큰 타입 **/
    String apiKeyType;

    String httpMethod;

    int rateLimit;

}
