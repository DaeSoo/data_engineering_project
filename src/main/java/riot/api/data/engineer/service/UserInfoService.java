package riot.api.data.engineer.service;

import org.springframework.http.ResponseEntity;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.entity.UserInfo;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface UserInfoService {


    ResponseEntity<ApiResult> createUserEntriesTasks(List<ApiInfo> apiInfoList, List<ApiKey> apiKeyList) throws InterruptedException;

    List<UserInfo> findUserInfoListUpdateYnIsN(Long apiKey, String updateYn);

    ApiResult deleteAllByUpdateYn(String updateYn);

    UserInfo save(UserInfo userInfo);
}
