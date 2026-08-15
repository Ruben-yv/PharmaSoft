package pe.edu.epeu.sysventas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.epeu.sysventas.entity.Categoria;
import pe.edu.epeu.sysventas.service.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }
    @GetMapping
    public Iterable<Categoria> listar(){
        return categoriaService.readAll();
    }
}
