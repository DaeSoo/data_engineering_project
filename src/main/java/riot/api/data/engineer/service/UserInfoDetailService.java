package riot.api.data.engineer.service;

import org.springframework.http.ResponseEntity;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.UserInfoDetail;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;


public interface UserInfoDetailService {

    UserInfoDetail userInfoDetailSave(UserInfoDetail userInfoDetail);

    void userInfoDetailApiCall(ApiKey apiKey, String apiName);

    ResponseEntity<ApiResult> createUserInfoDetailTasks(String method);

    UserInfoDetail jsonToEntity(String response, Long apiKeyId);

    List<UserInfoDetail> findUserInfoDetailList();

    List<UserInfoDetail> findUserInfoDetailListByApiKey(Long apiKey);

    ApiResult deleteAll();


}
