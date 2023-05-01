package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.repository.ApiInfoQueryRepository;
import riot.api.data.engineer.service.ApiInfoService;

import java.util.List;

@Service
public class ApiInfoServiceImpl implements ApiInfoService {

    private final ApiInfoQueryRepository apiInfoQueryRepository;

    protected ApiInfoServiceImpl(ApiInfoQueryRepository apiInfoQueryRepository) {
        this.apiInfoQueryRepository = apiInfoQueryRepository;
    }

    public ApiInfo findOneByName(String apiName) {
        return apiInfoQueryRepository.findOneByName(apiName);
    }

    @Override
    public List<ApiInfo> findByName(String apiName) {
        return apiInfoQueryRepository.findByName(apiName);
    }
}
