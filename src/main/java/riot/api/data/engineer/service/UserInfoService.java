package riot.api.data.engineer.service;

import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface UserInfoService {


    void apiCallBatch(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList);
}
