package ee.cyber.manatee.repository;

import ee.cyber.manatee.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}
