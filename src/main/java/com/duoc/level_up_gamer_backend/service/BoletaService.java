package com.duoc.level_up_gamer_backend.service;

import com.duoc.level_up_gamer_backend.dto.BoletaDTO;
import com.duoc.level_up_gamer_backend.dto.DetalleBoletaDTO;
import com.duoc.level_up_gamer_backend.model.Boleta;
import com.duoc.level_up_gamer_backend.model.DetalleBoleta;
import com.duoc.level_up_gamer_backend.model.Producto;
import com.duoc.level_up_gamer_backend.model.Usuario;
import com.duoc.level_up_gamer_backend.repository.BoletaRepository;
import com.duoc.level_up_gamer_backend.repository.ProductoRepository;
import com.duoc.level_up_gamer_backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public BoletaService(BoletaRepository boletaRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.boletaRepository = boletaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<BoletaDTO> listarTodas() {
        return boletaRepository.findAllByOrderByFechaDesc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<BoletaDTO> listarPorUsuario(Long usuarioId) {
        return boletaRepository.findByUsuarioIdOrderByFechaDesc(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public BoletaDTO obtenerPorId(Long id) {
        return boletaRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    @Transactional
    public BoletaDTO crearBoleta(BoletaDTO dto, String username) {
        Usuario usuario = null;
        if (username != null) {
            usuario = usuarioRepository.findByUsername(username)
                    .or(() -> usuarioRepository.findByEmail(username))
                    .orElse(null);
        } else if (dto.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
        }

        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());
        boleta.setEstado("PAGADA");
        boleta.setUsuario(usuario);

        double total = 0.0;
        double subtotal = 0.0;
        List<DetalleBoleta> detalles = new ArrayList<>();

        if (dto.getItems() != null) {
            for (DetalleBoletaDTO itemDto : dto.getItems()) {
                Producto prod = null;
                if (itemDto.getProductoId() != null) {
                    prod = productoRepository.findById(itemDto.getProductoId()).orElse(null);
                } else if (itemDto.getProductoCodigo() != null) {
                    prod = productoRepository.findByCodigo(itemDto.getProductoCodigo()).orElse(null);
                }

                if (prod != null) {
                    int cant = itemDto.getCantidad() != null ? itemDto.getCantidad() : 1;
                    // Descontar stock
                    if (prod.getStock() >= cant) {
                        prod.setStock(prod.getStock() - cant);
                    } else {
                        prod.setStock(0);
                    }
                    productoRepository.save(prod);

                    DetalleBoleta det = new DetalleBoleta();
                    det.setBoleta(boleta);
                    det.setProducto(prod);
                    det.setCantidad(cant);
                    det.setPrecioUnitario(prod.getPrecio());
                    double itemSubtotal = prod.getPrecio() * cant;
                    det.setSubtotal(itemSubtotal);
                    detalles.add(det);

                    subtotal += itemSubtotal;
                }
            }
        }

        double descuento = dto.getDescuento() != null ? dto.getDescuento() : 0.0;
        total = Math.max(0, subtotal - descuento);

        boleta.setSubtotal(subtotal);
        boleta.setDescuento(descuento);
        boleta.setTotal(total);
        boleta.setDetalles(detalles);

        // Sumar puntos al usuario si es cliente
        if (usuario != null) {
            int puntosGanados = (int) (total / 1000); // 1 punto cada $1.000
            usuario.setPuntos((usuario.getPuntos() != null ? usuario.getPuntos() : 0) + puntosGanados);
            usuarioRepository.save(usuario);
        }

        Boleta guardada = boletaRepository.save(boleta);
        return convertirADTO(guardada);
    }

    private BoletaDTO convertirADTO(Boleta b) {
        BoletaDTO dto = new BoletaDTO();
        dto.setId(b.getId());
        dto.setFecha(b.getFecha());
        dto.setSubtotal(b.getSubtotal());
        dto.setDescuento(b.getDescuento());
        dto.setTotal(b.getTotal());
        dto.setEstado(b.getEstado());
        if (b.getUsuario() != null) {
            dto.setUsuarioId(b.getUsuario().getId());
            dto.setUsuarioNombre(b.getUsuario().getNombre() + " " + (b.getUsuario().getApellidos() != null ? b.getUsuario().getApellidos() : ""));
        }

        if (b.getDetalles() != null) {
            dto.setItems(b.getDetalles().stream().map(d -> {
                DetalleBoletaDTO item = new DetalleBoletaDTO();
                item.setId(d.getId());
                item.setCantidad(d.getCantidad());
                item.setPrecioUnitario(d.getPrecioUnitario());
                item.setSubtotal(d.getSubtotal());
                if (d.getProducto() != null) {
                    item.setProductoId(d.getProducto().getId());
                    item.setProductoCodigo(d.getProducto().getCodigo());
                    item.setProductoNombre(d.getProducto().getNombre());
                }
                return item;
            }).collect(Collectors.toList()));
        }
        return dto;
    }
}
