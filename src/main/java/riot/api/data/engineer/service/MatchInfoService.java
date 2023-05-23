package riot.api.data.engineer.service;

import org.springframework.http.ResponseEntity;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface MatchInfoService {

    MatchInfo matchInfoSave(MatchInfo matchInfo);

    void matchApiRequest(ApiKey apiKey, String apiName, String startDate, String endDate);

    ResponseEntity<ApiResult> createThread(String method, String startDate, String endDate);

    List<MatchInfo> getMatchInfoList();

    ResponseEntity<ApiResult> apiCallBatch(ApiInfo apiInfo, List<ApiKey> apiKeyList);

}
