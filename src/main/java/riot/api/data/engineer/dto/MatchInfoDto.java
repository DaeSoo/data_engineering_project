package riot.api.data.engineer.dto;

import lombok.Getter;
import lombok.Setter;
import riot.api.data.engineer.utils.DateValid;

@Getter
@Setter
public class MatchInfoDto {

    @DateValid(message = "10자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
    String startTime;

    @DateValid(message = "10자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
    String endTime;
}
