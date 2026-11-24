package com.duoc.level_up_gamer_backend.controller;

import com.duoc.level_up_gamer_backend.dto.BoletaDTO;
import com.duoc.level_up_gamer_backend.model.Rol;
import com.duoc.level_up_gamer_backend.model.Usuario;
import com.duoc.level_up_gamer_backend.service.BoletaService;
import com.duoc.level_up_gamer_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/boletas", "/api/boletas"})
@CrossOrigin(origins = "*")
@Tag(name = "Boletas y Órdenes", description = "Endpoints para registro y consulta de compras/boletas")
public class BoletaController {

    private final BoletaService boletaService;
    private final UsuarioService usuarioService;

    public BoletaController(BoletaService boletaService, UsuarioService usuarioService) {
        this.boletaService = boletaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar boletas (Admin y Vendedor ven todas; Cliente ve solo las suyas)")
    public ResponseEntity<List<BoletaDTO>> listarBoletas(Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioService.obtenerPorUsername(auth.getName());
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        if (usuario.getRol() == Rol.ROLE_ADMIN || usuario.getRol() == Rol.ROLE_VENDEDOR) {
            return ResponseEntity.ok(boletaService.listarTodas());
        }

        return ResponseEntity.ok(boletaService.listarPorUsuario(usuario.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de una boleta por ID")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(401).build();
        }

        Usuario usuario = usuarioService.obtenerPorUsername(auth.getName());
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }

        BoletaDTO boleta = boletaService.obtenerPorId(id);
        if (boleta == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Boleta no encontrada"));
        }

        if (usuario.getRol() == Rol.ROLE_ADMIN || usuario.getRol() == Rol.ROLE_VENDEDOR ||
            (boleta.getUsuarioId() != null && boleta.getUsuarioId().equals(usuario.getId()))) {
            return ResponseEntity.ok(boleta);
        }

        return ResponseEntity.status(403).body(Map.of("error", "No tienes permisos para ver esta boleta"));
    }

    @PostMapping
    @Operation(summary = "Emitir nueva orden / boleta de compra")
    public ResponseEntity<?> emitirBoleta(@RequestBody BoletaDTO dto, Authentication auth) {
        try {
            String username = auth != null ? auth.getName() : null;
            BoletaDTO creada = boletaService.crearBoleta(dto, username);
            return ResponseEntity.status(201).body(creada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la compra: " + e.getMessage()));
        }
    }
}
