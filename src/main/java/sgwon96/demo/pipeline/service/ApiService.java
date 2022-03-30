package sgwon96.demo.pipeline.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sgwon96.demo.pipeline.dto.PodDto;
import sgwon96.demo.pipeline.util.RestApiUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiService {

    private final RestApiUtil restApiUtil;

    public List getResource(String resourceType) {

        Map response = (Map)restApiUtil.execute(HttpMethod.GET,resourceType).getBody();

        List<Map> items = (List) response.get("items");

        return items.stream()
                .map(item -> {

                    Map metadata = (Map)item.get("metadata");
                    String name = (String)metadata.get("name");
                    String namespace = (String)metadata.get("namespace");
                    Map status = (Map)item.get("status");
                    String phase = (String)status.get("phase");

                    return PodDto.builder()
                            .name(name)
                            .status(phase)
                            .namespace(namespace)
                            .build();
                })
                .collect(Collectors.toList());

    }




}
