package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;


public interface UserInfoDetailService {

    UserInfoDetail userInfoDetailSave(UserInfoDetail userInfoDetail);

    Boolean userInfoDetailApiRequest(ApiKey apiKey, String apiName);

    void createThread(String method);

    UserInfoDetail jsonToEntity(String response, Long apiKeyId);

    void createTestMethod(String packageName, String methodName);

    List<UserInfoDetail> userInfoDeatilList();

    List<UserInfoDetail> userInfoDeatilList(Long apiKey);


}
