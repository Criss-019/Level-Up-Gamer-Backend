package com.duoc.level_up_gamer_backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Tag(name = "Estado del Sistema", description = "Verificación de estado y accesos directos")
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "Página de inicio y estado de la API")
    public ResponseEntity<?> home() {
        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("proyecto", "Level-Up Gamer - Backend API REST (Spring Boot 3 + MySQL)");
        resp.put("estado", "ONLINE Y OPERACIONAL");
        resp.put("base_de_datos", "MySQL Server (level_up_gamer_db en puerto 3306)");
        resp.put("swagger_ui", "http://localhost:8089/swagger-ui/index.html");
        resp.put("phpmyadmin", "http://localhost/phpmyadmin/");

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("documentacion_swagger", "GET /swagger-ui/index.html");
        endpoints.put("login_jwt", "POST /api/v1/auth/login");
        endpoints.put("registro_usuario", "POST /api/v1/auth/registro");
        endpoints.put("catalogo_productos", "GET /api/v1/productos");
        endpoints.put("detalle_producto", "GET /api/v1/productos/{id}");
        endpoints.put("categorias", "GET /api/v1/categorias");
        endpoints.put("ordenes_boletas", "GET /api/v1/boletas");
        endpoints.put("usuarios_admin", "GET /api/v1/usuarios");
        resp.put("endpoints_disponibles", endpoints);

        return ResponseEntity.ok(resp);
    }
}
