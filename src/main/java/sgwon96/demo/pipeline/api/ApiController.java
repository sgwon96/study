package sgwon96.demo.pipeline.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sgwon96.demo.pipeline.dto.PodDto;
import sgwon96.demo.pipeline.service.ApiService;

import java.util.List;

import static sgwon96.demo.pipeline.util.RestApiUtil.RESOURCE_TYPE_POD;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @GetMapping("/pods")
    public List<PodDto> getPods(){
        return apiService.getResource(RESOURCE_TYPE_POD);
    }


}
