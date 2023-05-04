package riot.api.data.engineer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchInfoParam {
    Long startTime;
    Long endTime;
    String type;
    int start;
    int count;

}
