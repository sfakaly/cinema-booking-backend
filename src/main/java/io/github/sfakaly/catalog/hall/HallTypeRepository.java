package io.github.sfakaly.catalog.hall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallTypeRepository extends JpaRepository<HallType, Integer> {
}
