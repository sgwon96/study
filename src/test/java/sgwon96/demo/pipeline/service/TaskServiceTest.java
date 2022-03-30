package sgwon96.demo.pipeline.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import sgwon96.demo.pipeline.domain.TaskEntity;
import sgwon96.demo.pipeline.domain.TaskWithVersionEntity;
import sgwon96.demo.pipeline.dto.TaskDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class TaskServiceTest {

    @Autowired
    TaskService taskService;

    @Test
    @DisplayName("Lock 적용하기 전")
    void test1() throws Exception {

        // given
        TaskEntity newTask = TaskEntity.builder()
                .status("Created")
                .build();
        taskService.createTask(newTask);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(()->taskService.updateTask(newTask.getId(), TaskDto.CreateUpdate.builder().status("Started").build(), 2000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTask(newTask.getId(), TaskDto.CreateUpdate.builder().status("Running").build(), 1000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTask(newTask.getId(), TaskDto.CreateUpdate.builder().status("Succeeded").build(), 500));

        // Thread 작업이 다 끝날때까지 10초 대기
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        //then
        TaskEntity findTask = taskService.findTask(newTask.getId());

        assertAll(
                ()-> assertEquals("Started", findTask.getStatus()),
                ()-> assertNotEquals(null,findTask.getStartTime()),
                ()-> assertEquals(null,findTask.getEndTime()),
                ()-> assertEquals(null,findTask.getDuration())
        );

    }

    @Test
    @DisplayName("비관적 락 적용 - Succeeded, Running, Started 순서")
    void test2() throws Exception {

        // given
        TaskEntity newTask = TaskEntity.builder()
                .status("Created")
                .build();
        taskService.createTask(newTask);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Started").build(), 2000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Running").build(), 1000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Succeeded").build(), 500));

        // Thread 작업이 다 끝날때까지 10초 대기
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        //then
        TaskEntity findTask = taskService.findTask(newTask.getId());

        assertAll(
                ()-> assertEquals("Succeeded", findTask.getStatus()),
                ()-> assertNotEquals(null,findTask.getStartTime()),
                ()-> assertNotEquals(null,findTask.getEndTime()),
                ()-> assertNotEquals(null,findTask.getDuration())
        );
    }

    @Test
    @DisplayName("비관적 락 적용 - Running, Started, Succeeded 순서")
    void test3() throws Exception {

        // given
        TaskEntity newTask = TaskEntity.builder()
                .status("Created")
                .build();
        taskService.createTask(newTask);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Started").build(), 2000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Running").build(), 1000));
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithPessimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Succeeded").build(), 2000));

        // Thread 작업이 다 끝날때까지 10초 대기
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        //then
        TaskEntity findTask = taskService.findTask(newTask.getId());

        assertAll(
                ()-> assertEquals("Succeeded", findTask.getStatus()),
                ()-> assertNotEquals(null,findTask.getStartTime()),
                ()-> assertNotEquals(null,findTask.getEndTime()),
                ()-> assertNotEquals(null,findTask.getDuration())
        );
    }

    @Test
    @DisplayName("낙관적 락 적용 - Succeeded, Running, Started 순서")
    void test4() throws Exception {

        // given
        TaskWithVersionEntity newTask = TaskWithVersionEntity.builder()
                .status("Created")
                .build();
        taskService.createTask(newTask);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(()->{
            assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Started").build(),2000);
            });
        });
        Thread.sleep(500);

        executor.execute(()->{
            assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Running").build(),1500);
            });
        });
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Succeeded").build(),500));

        // Thread 작업이 다 끝날때까지 10초 대기
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        //then
        TaskWithVersionEntity findTask = taskService.findTaskWithVersion(newTask.getId());

        assertAll(
                ()-> assertEquals("Succeeded", findTask.getStatus()),
                ()-> assertNotEquals(null,findTask.getStartTime()),
                ()-> assertNotEquals(null,findTask.getEndTime()),
                ()-> assertNotEquals(null,findTask.getDuration())
        );
    }

    @Test
    @DisplayName("낙관적 락 적용 - Running, Started, Succeded 순서")
    void test5() throws Exception {

        // given
        TaskWithVersionEntity newTask = TaskWithVersionEntity.builder()
                .status("Created")
                .build();
        taskService.createTask(newTask);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(()->{
            assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Started").build(),2000);
            });
        });
        Thread.sleep(500);

        executor.execute(()->taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Running").build(),1000));
        Thread.sleep(500);

        executor.execute(()->{
            assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
                taskService.updateTaskWithOptimisticLock(newTask.getId(), TaskDto.CreateUpdate.builder().status("Succeeded").build(),2000);
            });
        });


        // Thread 작업이 다 끝날때까지 10초 대기
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        //then
        TaskWithVersionEntity findTask = taskService.findTaskWithVersion(newTask.getId());

        assertAll(
                ()-> assertEquals("Running", findTask.getStatus()),
                ()-> assertNotEquals(null,findTask.getStartTime()),
                ()-> assertEquals(null,findTask.getEndTime()),
                ()-> assertEquals(null,findTask.getDuration())
        );
    }



}