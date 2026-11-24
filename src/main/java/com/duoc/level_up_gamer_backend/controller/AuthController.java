package com.duoc.level_up_gamer_backend.controller;

import com.duoc.level_up_gamer_backend.dto.LoginRequest;
import com.duoc.level_up_gamer_backend.dto.LoginResponse;
import com.duoc.level_up_gamer_backend.dto.RegistroRequest;
import com.duoc.level_up_gamer_backend.model.Usuario;
import com.duoc.level_up_gamer_backend.repository.UsuarioRepository;
import com.duoc.level_up_gamer_backend.security.JwtService;
import com.duoc.level_up_gamer_backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/v1/auth", "/api/auth"})
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión, registro y perfil de usuarios")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener token JWT")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        String identifier = req.getUsername();
        String password = req.getPassword();

        if (identifier == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario y contraseña requeridos"));
        }

        Usuario usuario = usuarioRepository.findByUsername(identifier)
                .or(() -> usuarioRepository.findByEmail(identifier))
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no registrado"));
        }

        if (Boolean.FALSE.equals(usuario.getActivo())) {
            return ResponseEntity.status(403).body(Map.of("error", "Tu cuenta se encuentra inactiva"));
        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Contraseña incorrecta"));
        }

        String token = jwtService.generateToken(usuario.getUsername(), Map.of(
                "rol", usuario.getRol().name(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail() != null ? usuario.getEmail() : ""
        ));

        LoginResponse resp = new LoginResponse(
                token,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getPuntos(),
                usuario.getEsDuoc()
        );

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario cliente")
    public ResponseEntity<?> registro(@RequestBody RegistroRequest req) {
        try {
            Usuario nuevo = usuarioService.registrar(req);
            return ResponseEntity.status(201).body(Map.of(
                    "mensaje", "Usuario registrado exitosamente",
                    "id", nuevo.getId(),
                    "username", nuevo.getUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al registrar usuario: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<?> obtenerPerfil(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }
        String username = auth.getName();
        Usuario usuario = usuarioService.obtenerPorUsername(username);
        if (usuario == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(usuario);
    }
}
