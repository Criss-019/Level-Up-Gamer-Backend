package com.duoc.level_up_gamer_backend.repository;

import com.duoc.level_up_gamer_backend.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
    List<Boleta> findAllByOrderByFechaDesc();
}
