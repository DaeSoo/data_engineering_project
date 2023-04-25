package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.api.ApiInfo;

import java.util.List;

public interface ApiInfoService {
    ApiInfo findOneByName(String apiName);

    List<ApiInfo> findByName(String apiName);
}
