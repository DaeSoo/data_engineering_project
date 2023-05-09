package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface MatchInfoService {

    MatchInfo matchInfoSave(MatchInfo matchInfo);

    Boolean matchApiRequest(ApiKey apiKey, String apiName);

    void createThread(String method);

    List<MatchInfo> getMatchInfoList();

    void apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList);

    int apiCallBatchTest(ApiInfo apiInfo, List<ApiKey> apiKeyList);
}
