package com.duoc.level_up_gamer_backend.controller;

import com.duoc.level_up_gamer_backend.model.Categoria;
import com.duoc.level_up_gamer_backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/categorias", "/api/categorias"})
@CrossOrigin(origins = "*")
@Tag(name = "Categorías", description = "Endpoints para consultar y administrar categorías de productos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías")
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría (Admin)")
    public ResponseEntity<?> crear(@RequestBody Categoria categoria) {
        try {
            Categoria guardada = categoriaService.crear(categoria);
            return ResponseEntity.status(201).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
