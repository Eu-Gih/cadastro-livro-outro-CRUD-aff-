package com.javanauta.cadastro_livro;

import tools.jackson.databind.ObjectMapper;
import com.javanauta.cadastro_livro.infrastructure.entitys.Livro;
import com.javanauta.cadastro_livro.infrastructure.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /livro - Deve criar um novo livro com status 201")
    void deveCriarLivroComSucesso() throws Exception {
        Livro livro = Livro.builder()
                .titulo("Clean Architecture")
                .autor("Robert C. Martin")
                .genero("Tecnologia")
                .paginas(432)
                .build();

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo", is("Clean Architecture")))
                .andExpect(jsonPath("$.autor", is("Robert C. Martin")))
                .andExpect(jsonPath("$.paginas", is(432)));
    }

    @Test
    @DisplayName("POST /livro - Deve retornar 409 ao cadastrar livro com título duplicado")
    void deveRetornar409ParaTituloDuplicado() throws Exception {
        Livro livro = Livro.builder()
                .titulo("O Hobbit")
                .autor("J. R. R. Tolkien")
                .genero("Fantasia")
                .paginas(310)
                .build();

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/livro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livro)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Conflict")))
                .andExpect(jsonPath("$.message", containsString("Já existe um livro cadastrado com o título")));
    }

    @Test
    @DisplayName("GET /livro - Deve listar todos os livros")
    void deveListarTodosLivros() throws Exception {
        Livro livro1 = Livro.builder().titulo("Livro 1").autor("Autor 1").genero("Ficção").paginas(100).build();
        Livro livro2 = Livro.builder().titulo("Livro 2").autor("Autor 2").genero("Drama").paginas(200).build();
        livroRepository.save(livro1);
        livroRepository.save(livro2);

        mockMvc.perform(get("/livro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].titulo", is("Livro 1")))
                .andExpect(jsonPath("$[1].titulo", is("Livro 2")));
    }

    @Test
    @DisplayName("GET /livro/titulo/{titulo} - Deve buscar por título")
    void deveBuscarPorTitulo() throws Exception {
        Livro livro = Livro.builder().titulo("Duna").autor("Frank Herbert").genero("Ficção Científica").paginas(680).build();
        livroRepository.save(livro);

        mockMvc.perform(get("/livro/titulo/Duna"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo", is("Duna")))
                .andExpect(jsonPath("$.autor", is("Frank Herbert")));
    }

    @Test
    @DisplayName("GET /livro/titulo/{titulo} - Deve retornar 404 para título inexistente")
    void deveRetornar404ParaTituloInexistente() throws Exception {
        mockMvc.perform(get("/livro/titulo/NaoExiste"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")));
    }

    @Test
    @DisplayName("PUT /livro/titulo/{titulo} - Deve atualizar livro")
    void deveAtualizarLivro() throws Exception {
        Livro livro = Livro.builder().titulo("Refatoração").autor("Martin Fowler").genero("Tecnologia").paginas(450).build();
        livroRepository.save(livro);

        Livro atualizacao = Livro.builder().autor("Martin Fowler (2ª Edição)").paginas(448).build();

        mockMvc.perform(put("/livro/titulo/Refatoração")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autor", is("Martin Fowler (2ª Edição)")))
                .andExpect(jsonPath("$.paginas", is(448)))
                .andExpect(jsonPath("$.titulo", is("Refatoração")));
    }

    @Test
    @DisplayName("DELETE /livro/titulo/{titulo} - Deve deletar livro")
    void deveDeletarLivro() throws Exception {
        Livro livro = Livro.builder().titulo("Para Deletar").autor("Autor").genero("Geral").paginas(50).build();
        livroRepository.save(livro);

        mockMvc.perform(delete("/livro/titulo/Para Deletar"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/livro/titulo/Para Deletar"))
                .andExpect(status().isNotFound());
    }
}
