package com.duoc.level_up_gamer_backend.repository;

import com.duoc.level_up_gamer_backend.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
    List<DetalleBoleta> findByBoletaId(Long boletaId);
}
