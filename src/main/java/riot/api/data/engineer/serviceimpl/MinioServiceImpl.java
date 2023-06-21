package riot.api.data.engineer.serviceimpl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import riot.api.data.engineer.dto.FileDto;
import riot.api.data.engineer.entity.MinioInfo;
import riot.api.data.engineer.repository.MinioRepository;
import riot.api.data.engineer.service.MinioService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
@Slf4j
public class MinioServiceImpl implements MinioService {

    @Value("${minio.bucket}")
    private String bucket;
    
    @Value("${minio.url}")
    private String url;

    private final MinioClient minioClient;
    private final MinioRepository minioRepository;

    @Override
    public FileDto uploadFile(FileDto fileDto){
        try{
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileDto.getFile().getOriginalFilename())
                    .stream(fileDto.getFile().getInputStream(), fileDto.getSize(), -1).build());
        } catch (Exception e) {
            log.info("Upload ERROR : {}",e.getMessage());
            return null;
        }
        return FileDto.builder()
                .title(fileDto.getTitle())
                .description(fileDto.getDescription())
                .size(fileDto.getSize())
                .url(fileDto.getFile().getOriginalFilename())
                .filename(fileDto.getFile().getOriginalFilename()).build();
    }

    @Override
    public MinioInfo uploadInputStream(String matchId,String text){
        InputStream inputStream = stringToInputStream(text);
        String filename =  new StringBuilder(matchId).append(".json").toString();
        try{
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .stream(inputStream, inputStream.available(), -1).build());
            return MinioInfo.builder()
                    .downloadUrl(url+"/"+bucket+"/"+filename)
                    .size(inputStream.available())
                    .filename(filename)
                    .bucketName(bucket)
                    .matchId(matchId)
                    .build();
        } catch (Exception e) {
            log.info("Upload ERROR : {}",e.getMessage());
            return null;
        }
    }

    @Override
    public InputStream getObject(String filename) {
        InputStream inputStream;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());
        } catch (Exception e) {
            log.error("minio getObject Error: ", e);
            return null;
        }
        return inputStream;
    }

    public InputStream stringToInputStream(String text){
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public MinioInfo save(MinioInfo minioInfo){
        return minioRepository.save(minioInfo);
    }

}
