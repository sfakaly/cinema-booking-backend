package io.github.sfakaly.catalog.hall;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {
    List<Hall> findAllByCinema_Id(Integer cinemaId);
}
