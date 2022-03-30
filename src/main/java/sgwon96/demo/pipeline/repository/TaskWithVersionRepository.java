package sgwon96.demo.pipeline.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sgwon96.demo.pipeline.domain.TaskWithVersionEntity;

public interface TaskWithVersionRepository extends JpaRepository<TaskWithVersionEntity, Long> {
}
