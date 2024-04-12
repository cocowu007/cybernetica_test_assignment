package ee.cyber.manatee.repository;

import ee.cyber.manatee.model.ScheduleInterview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleInterview, Integer> {
}
