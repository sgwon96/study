package sgwon96.demo.pipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import sgwon96.demo.pipeline.domain.TaskEntity;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query(value = "select task from TaskEntity task where task.id = :id")
    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "10000")})
    TaskEntity findByIdWithPessimisticLock(@Param("id") Long id);

}
