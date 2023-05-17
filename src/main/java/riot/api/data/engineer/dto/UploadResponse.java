package riot.api.data.engineer.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadResponse {

    private String id;
    private String objectName;
    private String bucket;
}
