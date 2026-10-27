package com.javanauta.cadastro_livro;

import com.javanauta.cadastro_livro.business.LivroService;
import com.javanauta.cadastro_livro.infrastructure.entitys.Livro;
import com.javanauta.cadastro_livro.infrastructure.exceptions.ConflictException;
import com.javanauta.cadastro_livro.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.cadastro_livro.infrastructure.repository.LivroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CadastroLivroApplicationTests {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LivroRepository livroRepository;

    @BeforeEach
    void setUp() {
        livroRepository.deleteAll();
    }

    @Test
    @DisplayName("Contexto da aplicação deve carregar com sucesso")
    void contextLoads() {
        assertNotNull(livroService);
        assertNotNull(livroRepository);
    }

    @Test
    @DisplayName("Deve salvar livros com mesmo autor, mesmo gênero e mesma quantidade de páginas")
    void deveSalvarLivrosComMesmoAutorEGenero() {
        // Testa a correção do bug de unique = true em autor, genero e paginas
        Livro livro1 = Livro.builder()
                .titulo("Harry Potter e a Pedra Filosofal")
                .autor("J. K. Rowling")
                .genero("Fantasia")
                .paginas(300)
                .build();

        Livro livro2 = Livro.builder()
                .titulo("Harry Potter e a Câmara Secreta")
                .autor("J. K. Rowling")
                .genero("Fantasia")
                .paginas(300)
                .build();

        Livro salvo1 = livroService.salvarLivro(livro1);
        Livro salvo2 = livroService.salvarLivro(livro2);

        assertNotNull(salvo1.getId());
        assertNotNull(salvo2.getId());
        assertNotEquals(salvo1.getId(), salvo2.getId());

        List<Livro> todos = livroService.listarLivros();
        assertEquals(2, todos.size());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar livro com título duplicado")
    void deveLancarExcecaoAoDuplicarTitulo() {
        Livro livro1 = Livro.builder()
                .titulo("O Senhor dos Anéis")
                .autor("J. R. R. Tolkien")
                .genero("Fantasia")
                .paginas(1200)
                .build();
        livroService.salvarLivro(livro1);

        Livro livro2 = Livro.builder()
                .titulo("O Senhor dos Anéis")
                .autor("Outro Autor")
                .genero("Ficção")
                .paginas(500)
                .build();

        assertThrows(ConflictException.class, () -> livroService.salvarLivro(livro2));
    }

    @Test
    @DisplayName("Deve buscar livro por título")
    void deveBuscarLivroPorTitulo() {
        Livro livro = Livro.builder()
                .titulo("1984")
                .autor("George Orwell")
                .genero("Distopia")
                .paginas(328)
                .build();
        livroService.salvarLivro(livro);

        Livro encontrado = livroService.buscarLivroPorTitulo("1984");
        assertNotNull(encontrado);
        assertEquals("George Orwell", encontrado.getAutor());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar título inexistente")
    void deveLancarExcecaoAoBuscarTituloInexistente() {
        assertThrows(ResourceNotFoundException.class, () -> livroService.buscarLivroPorTitulo("Inexistente"));
    }

    @Test
    @DisplayName("Deve atualizar livro por título")
    void deveAtualizarLivroPorTitulo() {
        Livro livro = Livro.builder()
                .titulo("Clean Code")
                .autor("Robert Martin")
                .genero("Tecnologia")
                .paginas(450)
                .build();
        livroService.salvarLivro(livro);

        Livro dadosAtualizados = Livro.builder()
                .autor("Robert C. Martin")
                .paginas(464)
                .build();

        Livro atualizado = livroService.atualizarLivroPorTitulo("Clean Code", dadosAtualizados);
        assertEquals("Robert C. Martin", atualizado.getAutor());
        assertEquals(464, atualizado.getPaginas());
        assertEquals("Clean Code", atualizado.getTitulo());
        assertEquals("Tecnologia", atualizado.getGenero());
    }

    @Test
    @DisplayName("Deve deletar livro por título")
    void deveDeletarLivroPorTitulo() {
        Livro livro = Livro.builder()
                .titulo("O Pequeno Príncipe")
                .autor("Antoine de Saint-Exupéry")
                .genero("Fábula")
                .paginas(96)
                .build();
        livroService.salvarLivro(livro);

        livroService.deletarLivroPorTitulo("O Pequeno Príncipe");
        assertTrue(livroRepository.findByTitulo("O Pequeno Príncipe").isEmpty());
    }
}
