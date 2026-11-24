package com.duoc.level_up_gamer_backend.controller;

import com.duoc.level_up_gamer_backend.dto.ProductoDTO;
import com.duoc.level_up_gamer_backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/productos", "/api/productos"})
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Operaciones CRUD sobre catálogo de productos gamer")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos disponibles")
    public ResponseEntity<List<ProductoDTO>> listarTodos(@RequestParam(required = false) Boolean soloActivos) {
        if (Boolean.TRUE.equals(soloActivos)) {
            return ResponseEntity.ok(productoService.listarActivos());
        }
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un producto por ID")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        ProductoDTO dto = productoService.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto (Admin)")
    public ResponseEntity<?> crear(@RequestBody ProductoDTO dto) {
        try {
            ProductoDTO creado = productoService.crear(dto);
            return ResponseEntity.status(201).body(creado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente (Admin)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        ProductoDTO actualizado = productoService.actualizar(id, dto);
        if (actualizado == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto por ID (Admin)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = productoService.eliminar(id);
        if (!ok) {
            return ResponseEntity.status(404).body(Map.of("error", "Producto no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado con éxito"));
    }
}
