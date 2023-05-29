package riot.api.data.engineer.entity.api;

import lombok.Getter;
import javax.persistence.*;

@Entity
@Getter
public class ApiKey {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Id
    @Column(name = "api_key_id")
    private Long apiKeyId;
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "key_Summoner_id")
    private String keySummonerId;

    @Column(name = "use_yn")
    private Boolean useYn;

    @Column(name = "key_name")
    String keyName;

}
