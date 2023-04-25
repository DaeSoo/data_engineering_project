package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiKey;
import riot.api.data.engineer.repository.ApiKeyQueryRepository;
import riot.api.data.engineer.service.ApiKeyService;

import java.util.List;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    private final ApiKeyQueryRepository apiKeyRepository;

    public ApiKeyServiceImpl(ApiKeyQueryRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public ApiKey findOne(){
        return apiKeyRepository.findOne();
    }

    @Override
    public List<ApiKey> findList() {
        return apiKeyRepository.findList();
    }
}
