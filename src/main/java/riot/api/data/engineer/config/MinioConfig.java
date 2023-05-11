package riot.api.data.engineer.config;

import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access_key}")
    private String accessKey;

    @Value("${minio.secret_key}")
    private String secretKey;

    @Bean
    public MinioClient minioClient(){
        try{
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .build();

            return new MinioClient.Builder()
                    .credentials(accessKey, secretKey)
                    .httpClient(httpClient)
                    .endpoint(url)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
