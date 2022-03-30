package sgwon96.demo.pipeline.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class PodDto {

    private String name;
    private String status;
    private String namespace;

    @Builder
    public PodDto(String name, String status, String namespace) {
        this.name = name;
        this.status = status;
        this.namespace = namespace;
    }

}
