package pe.edu.epeu.sysventas.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.epeu.sysventas.dto.PaginaResponseDTO;
import pe.edu.epeu.sysventas.dto.ProductoRequestDTO;
import pe.edu.epeu.sysventas.dto.ProductoResponseDTO;
import pe.edu.epeu.sysventas.service.service.CategoriaService;
import pe.edu.epeu.sysventas.service.service.ProductoService;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping({"/api/productos", "/api/v1/productos"})
@Validated
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<ProductoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "La página debe ser mayor o igual a cero") int pagina,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño debe ser mayor que cero") @Max(value = 100, message = "El tamaño máximo permitido es 100") int tamanio,
            @RequestParam(defaultValue = "nombre") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direccion) {
        return ResponseEntity.ok(productoService.listar(pagina, tamanio, ordenarPor, direccion));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(productoService.read(id)
        );
    }
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> create(@Valid @RequestBody ProductoRequestDTO requestDTO){
        ProductoResponseDTO response = productoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO requestDTO){
        ProductoResponseDTO response = productoService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductoRequestDTO> delete(
            @PathVariable Long id){
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
