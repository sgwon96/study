package sgwon96.demo.pipeline.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sgwon96.demo.pipeline.domain.TaskEntity;
import sgwon96.demo.pipeline.domain.TaskWithVersionEntity;
import sgwon96.demo.pipeline.dto.TaskDto;
import sgwon96.demo.pipeline.repository.TaskRepository;
import sgwon96.demo.pipeline.repository.TaskWithVersionRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskWithVersionRepository taskWithVersionRepository;

    public TaskEntity findTask(Long id) {

        Optional<TaskEntity> findTask = taskRepository.findById(id);
        return findTask.orElse(null);
    }

    public TaskWithVersionEntity findTaskWithVersion(Long id) {

        Optional<TaskWithVersionEntity> findTask = taskWithVersionRepository.findById(id);
        return findTask.orElse(null);
    }

    @Transactional
    public TaskEntity createTask(TaskEntity taskEntity){
        return taskRepository.save(taskEntity);
    }

    @Transactional
    public TaskWithVersionEntity createTask(TaskWithVersionEntity taskWithVersionEntity){
        return taskWithVersionRepository.save(taskWithVersionEntity);
    }

    @Transactional
    public TaskEntity updateTask(Long id, TaskDto.CreateUpdate createUpdate, int sleepTime){

        TaskEntity taskEntity = findTask(id);
        threadSleep(sleepTime);
        taskEntity.updateStatus(createUpdate.getStatus());
        taskEntity.updateTime(createUpdate.getStatus());
        return taskEntity;
    }

    @Transactional
    public TaskEntity updateTaskWithPessimisticLock(Long id, TaskDto.CreateUpdate createUpdate, int sleepTime){

        TaskEntity taskEntity = taskRepository.findByIdWithPessimisticLock(id);
        threadSleep(sleepTime);
        taskEntity.updateStatus(createUpdate.getStatus());
        taskEntity.updateTime(createUpdate.getStatus());
        return taskEntity;
    }

    @Transactional
    public TaskWithVersionEntity updateTaskWithOptimisticLock(Long id, TaskDto.CreateUpdate createUpdate, int sleepTime){

        TaskWithVersionEntity taskEntity = taskWithVersionRepository.findById(id).get();
        threadSleep(sleepTime);
        taskEntity.updateStatus(createUpdate.getStatus());
        taskEntity.updateTime(createUpdate.getStatus());
        return taskEntity;
    }

    public void threadSleep(int sleepTime){
        try {
            Thread.sleep(sleepTime);
        } catch (Exception e){
            log.info(e.getMessage());
        }
    }
}
