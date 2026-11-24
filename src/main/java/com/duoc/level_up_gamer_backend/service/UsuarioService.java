package com.duoc.level_up_gamer_backend.service;

import com.duoc.level_up_gamer_backend.dto.RegistroRequest;
import com.duoc.level_up_gamer_backend.model.Rol;
import com.duoc.level_up_gamer_backend.model.Usuario;
import com.duoc.level_up_gamer_backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .or(() -> usuarioRepository.findByEmail(username))
                .orElse(null);
    }

    public Usuario registrar(RegistroRequest req) {
        String username = req.getUsername();
        if (username == null || username.trim().isEmpty()) {
            username = req.getEmail();
        }

        if (usuarioRepository.existsByUsername(username) || usuarioRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("El correo o nombre de usuario ya está registrado");
        }

        Usuario u = new Usuario();
        u.setUsername(username);
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setNombre(req.getNombre());
        u.setApellidos(req.getApellidos());
        u.setRun(req.getRun());
        u.setFechaNacimiento(req.getFechaNacimiento());
        u.setRegion(req.getRegion());
        u.setComuna(req.getComuna());
        u.setDireccion(req.getDireccion());
        u.setRol(Rol.ROLE_CLIENTE);

        // Si el correo es Duoc, tiene 20% descuento de por vida
        boolean esDuoc = req.getEmail() != null && (
                req.getEmail().endsWith("@duocuc.cl") ||
                req.getEmail().endsWith("@profesor.duoc.cl") ||
                req.getEmail().endsWith("@duoc.cl")
        );
        u.setEsDuoc(esDuoc);

        int puntos = (req.getReferido() != null && !req.getReferido().trim().isEmpty()) ? 150 : 50;
        u.setPuntos(puntos);
        u.setActivo(true);

        return usuarioRepository.save(u);
    }

    public Usuario actualizar(Long id, Usuario datos) {
        return usuarioRepository.findById(id).map(u -> {
            if (datos.getNombre() != null) u.setNombre(datos.getNombre());
            if (datos.getApellidos() != null) u.setApellidos(datos.getApellidos());
            if (datos.getRun() != null) u.setRun(datos.getRun());
            if (datos.getRegion() != null) u.setRegion(datos.getRegion());
            if (datos.getComuna() != null) u.setComuna(datos.getComuna());
            if (datos.getDireccion() != null) u.setDireccion(datos.getDireccion());
            if (datos.getPuntos() != null) u.setPuntos(datos.getPuntos());
            if (datos.getActivo() != null) u.setActivo(datos.getActivo());
            if (datos.getRol() != null) u.setRol(datos.getRol());
            return usuarioRepository.save(u);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
