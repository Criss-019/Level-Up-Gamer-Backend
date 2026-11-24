package com.duoc.level_up_gamer_backend.service;

import com.duoc.level_up_gamer_backend.dto.ProductoDTO;
import com.duoc.level_up_gamer_backend.model.Categoria;
import com.duoc.level_up_gamer_backend.model.Producto;
import com.duoc.level_up_gamer_backend.repository.CategoriaRepository;
import com.duoc.level_up_gamer_backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProductoDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> listarActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    public ProductoDTO crear(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setStockCritico(dto.getStockCritico() != null ? dto.getStockCritico() : 2);
        producto.setImagen(dto.getImagen());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId()).ifPresent(producto::setCategoria);
        } else if (dto.getCategoriaNombre() != null) {
            Categoria cat = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                    .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoriaNombre(), "")));
            producto.setCategoria(cat);
        }

        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }

    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        return productoRepository.findById(id).map(prod -> {
            if (dto.getCodigo() != null) prod.setCodigo(dto.getCodigo());
            if (dto.getNombre() != null) prod.setNombre(dto.getNombre());
            if (dto.getDescripcion() != null) prod.setDescripcion(dto.getDescripcion());
            if (dto.getPrecio() != null) prod.setPrecio(dto.getPrecio());
            if (dto.getStock() != null) prod.setStock(dto.getStock());
            if (dto.getStockCritico() != null) prod.setStockCritico(dto.getStockCritico());
            if (dto.getImagen() != null) prod.setImagen(dto.getImagen());
            if (dto.getActivo() != null) prod.setActivo(dto.getActivo());

            if (dto.getCategoriaId() != null) {
                categoriaRepository.findById(dto.getCategoriaId()).ifPresent(prod::setCategoria);
            } else if (dto.getCategoriaNombre() != null) {
                Categoria cat = categoriaRepository.findByNombre(dto.getCategoriaNombre())
                        .orElseGet(() -> categoriaRepository.save(new Categoria(dto.getCategoriaNombre(), "")));
                prod.setCategoria(cat);
            }
            return convertirADTO(productoRepository.save(prod));
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProductoDTO convertirADTO(Producto prod) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(prod.getId());
        dto.setCodigo(prod.getCodigo());
        dto.setNombre(prod.getNombre());
        dto.setDescripcion(prod.getDescripcion());
        dto.setPrecio(prod.getPrecio());
        dto.setStock(prod.getStock());
        dto.setStockCritico(prod.getStockCritico());
        dto.setImagen(prod.getImagen());
        dto.setActivo(prod.getActivo());
        if (prod.getCategoria() != null) {
            dto.setCategoriaId(prod.getCategoria().getId());
            dto.setCategoriaNombre(prod.getCategoria().getNombre());
        }
        return dto;
    }
}
