package com.duoc.level_up_gamer_backend.repository;

import com.duoc.level_up_gamer_backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByActivoTrue();
    List<Producto> findByCategoriaId(Long categoriaId);
}
