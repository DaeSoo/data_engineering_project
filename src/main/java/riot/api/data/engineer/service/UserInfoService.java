package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface UserInfoService {

    void apiCallBatch(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList);

    int apiCallBatchTest(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList);

    List<UserInfo> getUserInfoList(Long apiKey);

    List<UserInfo> getUserInfoListAll();

    void removeAll(List<UserInfo> userInfoList);
}
