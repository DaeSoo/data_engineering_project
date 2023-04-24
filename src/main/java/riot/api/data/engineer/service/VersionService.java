package riot.api.data.engineer.service;

import riot.api.data.engineer.entity.Version;

import java.util.List;

public interface VersionService {
    Version findOneByName();

    void save(List<Version> versionList);
}
