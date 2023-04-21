package riot.api.data.engineer.entity.api;

import javax.persistence.*;

@Entity(name = "api_params")
public class ApiParams {
    /** 시퀀스 **/
    @Id
    @Column(name = "api_params_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long api_params_id;
    /** ApiInfo 테이블 **/
    @Column(name = "api_info_id")
    Long ApiInfoId;
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

}
