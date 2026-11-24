package com.duoc.level_up_gamer_backend.config;

import com.duoc.level_up_gamer_backend.model.*;
import com.duoc.level_up_gamer_backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final BoletaRepository boletaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           CategoriaRepository categoriaRepository,
                           ProductoRepository productoRepository,
                           BoletaRepository boletaRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.boletaRepository = boletaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Categorías
        Categoria catJuegos = crearCategoriaSiNoExiste("Juegos de Mesa", "Juegos de mesa estratégicos y familiares");
        Categoria catAccesorios = crearCategoriaSiNoExiste("Accesorios", "Mandos, auriculares y complementos gamer");
        Categoria catConsolas = crearCategoriaSiNoExiste("Consolas", "Consolas de última generación PlayStation, Xbox y Nintendo");
        Categoria catPC = crearCategoriaSiNoExiste("Computadores Gamers", "Torres y laptops de alto rendimiento");
        Categoria catSillas = crearCategoriaSiNoExiste("Sillas Gamers", "Sillas ergonómicas profesionales");
        Categoria catMouse = crearCategoriaSiNoExiste("Mouse", "Mouses de precisión óptica para e-sports");
        Categoria catMousepad = crearCategoriaSiNoExiste("Mousepad", "Alfombrillas de tela de alta velocidad");
        Categoria catTeclados = crearCategoriaSiNoExiste("Teclados", "Teclados mecánicos con iluminación RGB");

        // 2. Inicializar Usuarios
        Usuario admin = null;
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@duoc.cl");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setApellidos("LevelUp");
            admin.setRun("19011022K");
            admin.setRegion("Región Metropolitana de Santiago");
            admin.setComuna("Providencia");
            admin.setDireccion("Av. Manuel Montt 123");
            admin.setPuntos(500);
            admin.setEsDuoc(true);
            admin.setRol(Rol.ROLE_ADMIN);
            admin.setActivo(true);
            admin = usuarioRepository.save(admin);
        } else {
            admin = usuarioRepository.findByUsername("admin").get();
        }

        Usuario vendedor = null;
        if (usuarioRepository.findByUsername("vendedor").isEmpty()) {
            vendedor = new Usuario();
            vendedor.setUsername("vendedor");
            vendedor.setEmail("vendedor@duoc.cl");
            vendedor.setPassword(passwordEncoder.encode("vendedor123"));
            vendedor.setNombre("Carlos");
            vendedor.setApellidos("Vendedor");
            vendedor.setRun("182345678");
            vendedor.setRegion("Región Metropolitana de Santiago");
            vendedor.setComuna("Santiago Centro");
            vendedor.setDireccion("Paseo Ahumada 456");
            vendedor.setPuntos(200);
            vendedor.setEsDuoc(false);
            vendedor.setRol(Rol.ROLE_VENDEDOR);
            vendedor.setActivo(true);
            vendedor = usuarioRepository.save(vendedor);
        } else {
            vendedor = usuarioRepository.findByUsername("vendedor").get();
        }

        Usuario cliente = null;
        if (usuarioRepository.findByUsername("cliente").isEmpty()) {
            cliente = new Usuario();
            cliente.setUsername("cliente");
            cliente.setEmail("juan.perez@gmail.com");
            cliente.setPassword(passwordEncoder.encode("user123"));
            cliente.setNombre("Juan");
            cliente.setApellidos("Pérez");
            cliente.setRun("201234567");
            cliente.setRegion("Región de Valparaíso");
            cliente.setComuna("Viña del Mar");
            cliente.setDireccion("Calle 1 Norte 456");
            cliente.setPuntos(100);
            cliente.setEsDuoc(false);
            cliente.setRol(Rol.ROLE_CLIENTE);
            cliente.setActivo(true);
            cliente = usuarioRepository.save(cliente);
        } else {
            cliente = usuarioRepository.findByUsername("cliente").get();
        }

        // 3. Inicializar Productos
        Producto pCatan = crearProductoSiNoExiste("JM001", "Catan", "Un clásico juego de estrategia donde los jugadores compiten por colonizar la isla de Catan.", 29990.0, 15, 3, "img/Logo.png", catJuegos);
        Producto pCarcassonne = crearProductoSiNoExiste("JM002", "Carcassonne", "Juego de colocación de fichas construyendo el paisaje medieval.", 24990.0, 8, 2, "img/Logo.png", catJuegos);
        Producto pXbox = crearProductoSiNoExiste("AC001", "Controlador Inalámbrico Xbox Series X", "Mando ergonómico compatible con Xbox y PC.", 59990.0, 20, 5, "img/Logo.png", catAccesorios);
        Producto pHyperX = crearProductoSiNoExiste("AC002", "Auriculares Gamer HyperX Cloud II", "Sonido envolvente 7.1 con almohadillas viscoelásticas.", 79990.0, 12, 4, "img/Logo.png", catAccesorios);
        Producto pPS5 = crearProductoSiNoExiste("CO001", "PlayStation 5", "Consola de última generación con soporte para 4K a 120 fps.", 549990.0, 5, 2, "img/Logo.png", catConsolas);
        Producto pPC = crearProductoSiNoExiste("CG001", "PC Gamer ASUS ROG Strix", "Torre gamer con procesador de alto rendimiento y GPU RTX.", 1299990.0, 3, 1, "img/Logo.png", catPC);
        Producto pSilla = crearProductoSiNoExiste("SG001", "Silla Gamer Secretlab Titan", "Soporte ergonómico ajustable de calidad premium.", 349990.0, 7, 2, "img/Logo.png", catSillas);
        Producto pMouse = crearProductoSiNoExiste("MS001", "Mouse Gamer Logitech G502 HERO", "Sensor óptico HERO 25K con pesas ajustables.", 49990.0, 25, 5, "img/Logo.png", catMouse);

        // 4. Inicializar Boleta de prueba para vista de Vendedor y Admin
        if (boletaRepository.count() == 0 && cliente != null && pCatan != null) {
            Boleta b = new Boleta();
            b.setFecha(LocalDateTime.now().minusDays(1));
            b.setEstado("PAGADA");
            b.setUsuario(cliente);
            b.setSubtotal(109980.0);
            b.setDescuento(0.0);
            b.setTotal(109980.0);

            DetalleBoleta det1 = new DetalleBoleta();
            det1.setBoleta(b);
            det1.setProducto(pCatan);
            det1.setCantidad(1);
            det1.setPrecioUnitario(pCatan.getPrecio());
            det1.setSubtotal(pCatan.getPrecio());

            DetalleBoleta det2 = new DetalleBoleta();
            det2.setBoleta(b);
            det2.setProducto(pHyperX);
            det2.setCantidad(1);
            det2.setPrecioUnitario(pHyperX.getPrecio());
            det2.setSubtotal(pHyperX.getPrecio());

            b.setDetalles(List.of(det1, det2));
            boletaRepository.save(b);
        }

        System.out.println("==================================================");
        System.out.println("⚡ LEVEL-UP GAMER BACKEND INICIALIZADO CON ÉXITO");
        System.out.println("Usuarios precargados:");
        System.out.println(" • Admin    : admin / admin123  (Rol: ROLE_ADMIN)");
        System.out.println(" • Vendedor : vendedor / vendedor123 (Rol: ROLE_VENDEDOR)");
        System.out.println(" • Cliente  : cliente / user123 (Rol: ROLE_CLIENTE)");
        System.out.println("Base de Datos MySQL: level_up_gamer_db (puerto 3306)");
        System.out.println("Swagger UI disponible en: http://localhost:8089/swagger-ui/index.html");
        System.out.println("==================================================");
    }

    private Categoria crearCategoriaSiNoExiste(String nombre, String descripcion) {
        return categoriaRepository.findByNombre(nombre)
                .orElseGet(() -> categoriaRepository.save(new Categoria(nombre, descripcion)));
    }

    private Producto crearProductoSiNoExiste(String codigo, String nombre, String desc, Double precio, Integer stock, Integer stockCritico, String imagen, Categoria cat) {
        return productoRepository.findByCodigo(codigo)
                .orElseGet(() -> {
                    Producto p = new Producto();
                    p.setCodigo(codigo);
                    p.setNombre(nombre);
                    p.setDescripcion(desc);
                    p.setPrecio(precio);
                    p.setStock(stock);
                    p.setStockCritico(stockCritico);
                    p.setImagen(imagen);
                    p.setActivo(true);
                    p.setCategoria(cat);
                    return productoRepository.save(p);
                });
    }
}
