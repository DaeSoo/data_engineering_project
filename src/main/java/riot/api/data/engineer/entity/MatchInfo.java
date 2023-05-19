package riot.api.data.engineer.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import riot.api.data.engineer.entity.api.ApiKey;

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

    @Column(name = "collect_date")
    private String collectDate;

    @Column(name = "collect_complete_yn")
    private Boolean collectCompleteYn;

    public MatchInfo(String id, Long apiKeyId, String collectDate) {
        this.id = id;
        this.apiKeyId = apiKeyId;
        this.collectDate = collectDate;
        this.collectCompleteYn = Boolean.FALSE;
    }
}
