package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface MatchInfoService {

    MatchInfo matchInfoSave(MatchInfo matchInfo);

    void matchApiRequest(ApiKey apiKey, String apiName, String startDate, String endDate);

    int createThread(String method, String startDate, String endDate);

    List<MatchInfo> getMatchInfoList();

    int apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList);

}
