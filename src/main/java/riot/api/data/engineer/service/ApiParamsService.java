package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.api.ApiParams;

import java.util.List;

public interface ApiParamsService {

    List<ApiParams> getApiParamsList(Long apiInfoId);
}