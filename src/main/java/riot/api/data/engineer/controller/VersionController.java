package riot.api.data.engineer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import riot.api.data.engineer.apiresult.ApiResult;
import riot.api.data.engineer.dto.WebClientDTO;
import riot.api.data.engineer.entity.Version;
import riot.api.data.engineer.entity.WebClientCaller;
import riot.api.data.engineer.entity.api.ApiInfo;
import riot.api.data.engineer.service.ApiInfoService;
import riot.api.data.engineer.service.VersionService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ddragon/version")
@RequiredArgsConstructor
public class VersionController {
    private final ApiInfoService apiInfoService;
    private final VersionService versionService;
    private final WebClient webClient;

    @GetMapping("/get")
    public ResponseEntity<ApiResult> getVersion() {
        try {
            ApiInfo apiInfo = apiInfoService.findOneByName(new Exception().getStackTrace()[0].getMethodName());
            WebClientDTO webClientDTO = WebClientDTO.builder()
                    .scheme(apiInfo.getApiScheme())
                    .host(apiInfo.getApiHost())
                    .path(apiInfo.getApiUrl())
                    .build();

            WebClientCaller webClientCaller = WebClientCaller.builder()
                    .webClientDTO(webClientDTO)
                    .webclient(webClient)
                    .build();
            String response = webClientCaller.getWebClientToString();

            List<Version> versionList = versionService.getVersionList(response);
            versionService.save(versionList);

            return new ResponseEntity(new ApiResult(200, "success", versionList), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current/get")
    public ResponseEntity<ApiResult> getCurrentVersion() {
        try{
            Version version = versionService.findOneByCurrentVersion();
            return new ResponseEntity(new ApiResult(200, "success", version), HttpStatus.OK);
        }
            catch (Exception e) {
            return new ResponseEntity(new ApiResult(500, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
