package riot.api.data.engineer.entity.api;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class ApiKey {
    @Id
    @Column(name = "api_key_id")
    private Long apiKeyId;
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "key_Summoner_id")
    private String keySummonerId;

    @Column(name = "use_yn")
    private Boolean useYn;
}
