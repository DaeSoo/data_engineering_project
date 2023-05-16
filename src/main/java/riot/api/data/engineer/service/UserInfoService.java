package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface UserInfoService {


    int apiCallBatch(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList) throws InterruptedException;

    List<UserInfo> getUserInfoList(Long apiKey, String updateYn);

    List<UserInfo> getUserInfoListAll();

    void removeAll(List<UserInfo> userInfoList);

    UserInfo save(UserInfo userInfo);
}
