package riot.api.data.engineer.service;

import org.springframework.http.ResponseEntity;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface MatchInfoService {

    MatchInfo matchInfoSave(MatchInfo matchInfo);

    void matchListApiCall(ApiKey apiKey, String apiName, String startDate, String endDate);

    ResponseEntity<ApiResult> createMatchInfoTasks(String method, String startDate, String endDate);

    List<MatchInfo> findMatchInfoList();

    ResponseEntity<ApiResult> createMatchInfoDetailTasks(ApiInfo apiInfo, List<ApiKey> apiKeyList);

    ApiResult deleteAllByCollectCompleteYn(String collectCompleteYn);

}
