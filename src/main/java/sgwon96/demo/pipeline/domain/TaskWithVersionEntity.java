package sgwon96.demo.pipeline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TaskWithVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    private Long duration;

    @Version
    private Long version;

    @Builder
    public TaskWithVersionEntity(String status){
        this.status = status;
    }

    @Override
    public String toString(){
        return this.id + " " + this.status + " " + this.startTime + " " + this.endTime + " " + this.duration;
    }

    public void updateStatus(String status){
        this.status = status;
    }

    public void updateTime(String status) {

        if (this.startTime == null){
            this.startTime = LocalDateTime.now().withNano(0);
        }

        if (status.equals("Succeeded")){
            this.endTime = LocalDateTime.now().withNano(0);
            this.duration = Duration.between(this.startTime,this.endTime).getSeconds();
        }
    }
}
