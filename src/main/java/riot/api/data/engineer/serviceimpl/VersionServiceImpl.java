package riot.api.data.engineer.serviceimpl;

import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.Version;

import riot.api.data.engineer.repository.VersionQueryRepository;
import riot.api.data.engineer.repository.VersionRepository;
import riot.api.data.engineer.service.VersionService;

import java.util.List;

@Service
public class VersionServiceImpl implements VersionService {
    private final VersionQueryRepository versionQueryRepository;
    private final VersionRepository versionRepository;

    public VersionServiceImpl(VersionQueryRepository versionQueryRepository, VersionRepository versionRepository) {
        this.versionQueryRepository = versionQueryRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public Version findOneByName() {
        return versionQueryRepository.findOne();
    }

    @Override
    public void save(List<Version> versionList) {
        versionRepository.saveAll(versionList);
    }


}
