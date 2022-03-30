package sgwon96.demo.pipeline.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sgwon96.demo.pipeline.domain.TaskEntity;
import sgwon96.demo.pipeline.dto.TaskDto;
import sgwon96.demo.pipeline.service.TaskService;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks/{id}")
    public TaskDto.Response updateTask(
            @PathVariable(value = "id") Long id,
            @RequestBody TaskDto.CreateUpdate createUpdate){
        TaskEntity taskEntity = taskService.updateTask(id,createUpdate, 0);
        return new TaskDto.Response(taskEntity);
    }

    @PostMapping("/tasks/{id}/lock")
    public TaskDto.Response updateTaskWithPessimisticLock(
            @PathVariable(value = "id") Long id,
            @RequestBody TaskDto.CreateUpdate createUpdate){
        TaskEntity taskEntity = taskService.updateTaskWithPessimisticLock(id,createUpdate,0);
        return new TaskDto.Response(taskEntity);
    }



}
