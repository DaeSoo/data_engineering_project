package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.service.MatchInfoService;

@RestController
@RequestMapping("match")
@RequiredArgsConstructor
public class MatchInfoController {

    private final MatchInfoService matchInfoService;

    @GetMapping("save")
    public String getMatchList(){
        matchInfoService.createThread(new Exception().getStackTrace()[0].getMethodName());

        return "success";
    }

}
