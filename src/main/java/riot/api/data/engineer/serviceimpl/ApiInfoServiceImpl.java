package riot.api.data.engineer.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.repository.ApiInfoQueryRepository;
import riot.api.data.engineer.service.ApiInfoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiInfoServiceImpl implements ApiInfoService {

    private final ApiInfoQueryRepository apiInfoQueryRepository;

    public ApiInfo findOneByName(String apiName) {
        return apiInfoQueryRepository.findOneByName(apiName);
    }

    @Override
    public List<ApiInfo> findByName(String apiName) {
        return apiInfoQueryRepository.findByName(apiName);
    }
}
