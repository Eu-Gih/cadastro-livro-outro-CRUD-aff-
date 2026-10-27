package com.javanauta.cadastro_livro.controller;

import com.javanauta.cadastro_livro.business.LivroService;
import com.javanauta.cadastro_livro.infrastructure.entitys.Livro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/livro")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<Livro> salvarLivro(@RequestBody Livro livro) {
        livroService.salvarLivro(livro);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Livro> buscarLivroPorTitulo(@RequestParam String titulo) {
        return ResponseEntity.ok(livroService.buscarLivroPorTitulo(titulo));
    }

    @PutMapping(params = "titulo")
    public ResponseEntity<Livro> atualizarLivroPorTitulo(@RequestParam String titulo, @RequestBody Livro livro) {
        livroService.atualizarLivroPorTitulo(titulo, livro);
        return ResponseEntity.ok().build();
    }

    @PutMapping(params = "id")
    public ResponseEntity<Livro> atualizarLivroPorId(@RequestParam int id, @RequestBody Livro livro) {
        livroService.atualizarLivroPorId(id, livro);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarLivroPorTitulo(@RequestParam String titulo) {
        livroService.deletarLivroPorTitulo(titulo);
        return ResponseEntity.noContent().build();
    }


}
