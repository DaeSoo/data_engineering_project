package riot.api.data.engineer.entity.api;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "api_params")
@Getter
@Setter
public class ApiParams {
    /** 시퀀스 **/
    @Id
    @Column(name = "api_params_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long apiParamsId;
    /** ApiInfo 테이블 **/
    @Column(name = "api_info_id",insertable = false,updatable = false)
    Long apiInfoId;
    /** 파라미터 명 **/
    @Column(name = "name")
    String name;
    /** 설명 **/
    @Column(name = "description")
    String description;
    /** 키 **/
    @Column(name = "data_type")
    String dataType;
    /** 필수 여부 **/
    @Column(name = "is_required")
    Boolean isRequired;

    /** Api key **/
    @Column(name = "api_key")
    String apiKey;

    /** Api Value **/
    @Column(name = "api_value")
    String apiValue;

    /** 날짜 파라미터 여부 **/
    @Column(name = "date_param_required")
    Boolean dateParamRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_info_id")
    ApiInfo apiInfo;
}
