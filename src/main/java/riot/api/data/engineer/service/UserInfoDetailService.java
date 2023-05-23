package riot.api.data.engineer.service;

import org.springframework.http.ResponseEntity;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;


public interface UserInfoDetailService {

    UserInfoDetail userInfoDetailSave(UserInfoDetail userInfoDetail);

    void userInfoDetailApiRequest(ApiKey apiKey, String apiName);

    ResponseEntity<ApiResult> createThread(String method);

    UserInfoDetail jsonToEntity(String response, Long apiKeyId);

    List<UserInfoDetail> userInfoDeatilList();

    List<UserInfoDetail> userInfoDeatilList(Long apiKey);


}
