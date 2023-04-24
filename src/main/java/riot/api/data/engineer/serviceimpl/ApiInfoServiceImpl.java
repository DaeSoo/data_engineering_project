package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.repository.ApiInfoRepository;
import riot.api.data.engineer.service.ApiInfoService;

@Service
public class ApiInfoServiceImpl implements ApiInfoService {

    private final ApiInfoRepository apiInfoRepository;
    protected ApiInfoServiceImpl(ApiInfoRepository apiInfoRepository){
        this.apiInfoRepository = apiInfoRepository;
    }
    public ApiInfo findOneByName(String apiName){
        return apiInfoRepository.findOneByName(apiName);
    }
}
