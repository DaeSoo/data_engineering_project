package riot.api.data.engineer.apiresult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ApiResult {

    int statusCode;
    String message;
    Object data;

}
