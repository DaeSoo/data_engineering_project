package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.MatchInfo;
import riot.api.data.engineer.entity.api.ApiKey;

public interface MatchInfoService {

    MatchInfo matchInfoSave(MatchInfo matchInfo);

    Boolean matchApiRequest(ApiKey apiKey, String apiName);

    void createThread(String method);
}
