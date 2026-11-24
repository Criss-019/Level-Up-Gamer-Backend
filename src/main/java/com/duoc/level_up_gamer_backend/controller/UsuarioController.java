package com.duoc.level_up_gamer_backend.controller;

import com.duoc.level_up_gamer_backend.model.Usuario;
import com.duoc.level_up_gamer_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/usuarios", "/api/usuarios"})
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios", description = "Endpoints de administración de usuarios (Solo Admin)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios registrados (Admin)")
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID (Admin)")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Usuario u = usuarioService.obtenerPorId(id);
        if (u == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(u);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario (Admin)")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario actualizado = usuarioService.actualizar(id, datos);
        if (actualizado == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar / dar de baja usuario (Admin)")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean ok = usuarioService.eliminar(id);
        if (!ok) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado con éxito"));
    }
}
