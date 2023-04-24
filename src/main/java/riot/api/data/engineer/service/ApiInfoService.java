package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.api.ApiInfo;

public interface ApiInfoService {
    ApiInfo findOneByName(String apiName);
}
