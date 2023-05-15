package riot.api.data.engineer.service;

import riot.api.data.engineer.dto.FileDto;
import riot.api.data.engineer.entity.MinioInfo;

import java.io.InputStream;

public interface MinioService {


    FileDto uploadFile(FileDto fileDto);

    InputStream getObject(String filename);

    MinioInfo uploadInputStream(String matchId, String text);

    MinioInfo save(MinioInfo minioInfo);
}
