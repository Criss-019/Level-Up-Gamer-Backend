package com.duoc.level_up_gamer_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BoletaDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total;
    private Double subtotal;
    private Double descuento;
    private String estado;
    private Long usuarioId;
    private String usuarioNombre;
    private List<DetalleBoletaDTO> items;

    public BoletaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public List<DetalleBoletaDTO> getItems() { return items; }
    public void setItems(List<DetalleBoletaDTO> items) { this.items = items; }
}
