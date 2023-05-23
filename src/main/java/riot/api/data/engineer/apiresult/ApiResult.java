package riot.api.data.engineer.apiresult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ApiResult {

    int StatusCode;
    String message;
    Object data;

}
