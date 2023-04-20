package riot.api.data.engineer.entity.api;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
public class ApiMeta {

    @Id
    private Long id;

    private String apiName;

    private String address;

    private String port;

    private String apiPrefix;

    private String contentType;

    private String method;

    @OneToMany
    private List<ApiKey> apiKey;

}
