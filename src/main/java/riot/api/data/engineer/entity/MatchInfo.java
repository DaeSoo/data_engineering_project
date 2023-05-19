package riot.api.data.engineer.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class MatchInfo extends BaseEntity{

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "api_key_id")
    private Long apiKeyId;

    @Column(name = "collect_date")
    private String collectDate;

}
