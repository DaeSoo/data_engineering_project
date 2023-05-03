package riot.api.data.engineer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import riot.api.data.engineer.service.UserInfoDetailService;
@Slf4j
@RestController
@RequestMapping(value = "/userinfo/detail")
public class UserInfoDetailController {
    private final UserInfoDetailService userInfoDetailService;

    public UserInfoDetailController(UserInfoDetailService userInfoDetailService)
    {
        this.userInfoDetailService = userInfoDetailService;
    }

    @GetMapping("save")
    public String getUserInfoDetail(){
        userInfoDetailService.createThread(new Exception().getStackTrace()[0].getMethodName());
        return "success";
    }
}
