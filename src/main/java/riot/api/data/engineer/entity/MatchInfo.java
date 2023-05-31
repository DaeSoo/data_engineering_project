package riot.api.data.engineer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MatchInfo extends BaseEntity{

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "api_key_id")
    private Long apiKeyId;

    @Column(name = "collect_complete_yn")
    private Boolean collectCompleteYn;

    public MatchInfo(String id, Long apiKeyId) {
        this.id = id;
        this.apiKeyId = apiKeyId;
        this.collectCompleteYn = Boolean.FALSE;
    }
}
