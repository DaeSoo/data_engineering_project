package riot.api.data.engineer.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.api.ApiParams;
import riot.api.data.engineer.repository.ApiParamsQueryRepository;
import riot.api.data.engineer.service.ApiParamsService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiParamsServiceImpl implements ApiParamsService {

    private final ApiParamsQueryRepository apiParamsQueryRepository;


    @Override
    public List<ApiParams> getApiParamsList(Long apiInfoId) {
        return apiParamsQueryRepository.findByApiInfoId(apiInfoId);
    }
}
