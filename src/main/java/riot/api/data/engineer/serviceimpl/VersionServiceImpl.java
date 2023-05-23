package riot.api.data.engineer.serviceimpl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.entity.Version;

import riot.api.data.engineer.repository.VersionQueryRepository;
import riot.api.data.engineer.repository.VersionRepository;
import riot.api.data.engineer.service.VersionService;
import riot.api.data.engineer.utils.UtilManager;

import java.util.ArrayList;
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
    public Version findOneByCurrentVersion() {
        return versionQueryRepository.findOneByCurrentVersion();
    }

    @Override
    public void save(List<Version> versionList) {
        versionRepository.saveAll(versionList);
    }

    @Override
    public List<Version> getVersionList(String response) {
        List<Version> versionList = new ArrayList<>();
        JsonArray jsonArrayResponse = new UtilManager().StringToJsonArray(response);

        for (JsonElement jsonElement : jsonArrayResponse) {
            String versionString = jsonElement.getAsString();
            boolean currentVersionYn = versionString.equals(jsonArrayResponse.get(0).getAsString());
            Version version = new Version(versionString, currentVersionYn);
            versionList.add(version);
        }
        return versionList;
    }


}
