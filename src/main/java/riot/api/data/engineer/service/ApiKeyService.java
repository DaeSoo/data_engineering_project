package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.api.ApiKey;

import java.util.List;

public interface ApiKeyService {
    ApiKey findOne();

    List<ApiKey> findList();
}
