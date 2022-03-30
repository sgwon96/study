package sgwon96.demo.pipeline.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sgwon96.demo.pipeline.domain.TaskEntity;

import java.time.LocalDateTime;

public class TaskDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateUpdate{

        private String status;

        @Builder
        public CreateUpdate(String status){
            this.status = status;
        }

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{

        private Long id;

        private String status;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private Long duration;

        public Response(TaskEntity taskEntity){
            id = taskEntity.getId();
            status = taskEntity.getStatus();
            startTime = taskEntity.getStartTime();
            endTime = taskEntity.getEndTime();
            duration = taskEntity.getDuration();
        }
    }
}
