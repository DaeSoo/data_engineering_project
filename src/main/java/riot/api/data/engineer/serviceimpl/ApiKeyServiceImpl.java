package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.ApiKeyRepository;
import riot.api.data.engineer.service.ApiKeyService;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public ApiKey findOne(){
        return apiKeyRepository.findOne();
    }
}
