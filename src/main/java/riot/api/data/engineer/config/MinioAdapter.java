package riot.api.data.engineer.config;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import riot.api.data.engineer.dto.UploadResponse;

import java.io.File;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioAdapter {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public Flux<Bucket> getAllBuckets() {
        try {
            return Flux.fromIterable(minioClient.listBuckets()).subscribeOn(Schedulers.boundedElastic());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @SneakyThrows
    public Mono<UploadResponse> uploadFile(Mono<FilePart> file) {
        return file.subscribeOn(Schedulers.boundedElastic()).map(multipartFile -> {
            long startMillis = System.currentTimeMillis();
            File temp = new File(multipartFile.filename());
            temp.canWrite();
            temp.canRead();
            try {
                // blocking to complete io operation
                multipartFile.transferTo(temp).block();
                UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(multipartFile.filename())
                        .filename(temp.getAbsolutePath())
                        .build();

                ObjectWriteResponse response = minioClient.uploadObject(uploadObjectArgs);
                temp.delete();
                log.info("upload file execution time {} ms", System.currentTimeMillis() - startMillis);
                return UploadResponse.builder().bucket(response.bucket()).objectName(response.object()).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).log();
    }

    public Mono<InputStreamResource> download(String name) {
        return Mono.fromCallable(() -> {
            InputStream response = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(name).build());
            return new InputStreamResource(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<UploadResponse> putObject(FilePart file) {
        return file.content()
                .subscribeOn(Schedulers.boundedElastic())
                .reduce(new InputStreamCollector(),
                        (collector, dataBuffer) -> collector.collectInputStream(dataBuffer.asInputStream()))
                .map(inputStreamCollector -> {
                    long startMillis = System.currentTimeMillis();
                    try {
                        PutObjectArgs args = PutObjectArgs.builder().object(file.filename())
                                .contentType(file.headers().getContentType().toString())
                                .bucket(bucketName)
                                .stream(inputStreamCollector.getStream(), inputStreamCollector.getStream().available(), -1)
                                .build();
                        ObjectWriteResponse response = minioClient.putObject(args);
                        log.info("upload file execution time {} ms", System.currentTimeMillis() - startMillis);
                        return UploadResponse.builder().bucket(response.bucket()).objectName(response.object()).build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).log();
    }
}
