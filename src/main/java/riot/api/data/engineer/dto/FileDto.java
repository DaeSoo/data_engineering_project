package riot.api.data.engineer.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
public class FileDto {
    private String title;

    private String description;

    private MultipartFile file;

    private String url;

    private Long size;

    private String filename;

}
