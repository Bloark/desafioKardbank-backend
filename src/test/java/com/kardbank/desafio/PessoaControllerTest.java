package com.kardbank.desafio;

import com.kardbank.desafio.controller.PessoaController;
import com.kardbank.desafio.model.Pessoa;
import com.kardbank.desafio.repository.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PessoaControllerTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPessoas_DeveRetornarListaDePessoas() {
        // Arrange
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(new Pessoa(1L, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901"));
        pessoas.add(new Pessoa(2L, "Maria", "Rua B", "Cidade B", "Estado B", "9876543210", "maria@example.com", "09876543210"));

        when(pessoaRepository.findAll()).thenReturn(pessoas);

        // Act
        List<Pessoa> result = pessoaController.listarPessoas();

        // Assert
        assertEquals(pessoas.size(), result.size());
        assertEquals(pessoas.get(0), result.get(0));
        assertEquals(pessoas.get(1), result.get(1));

        verify(pessoaRepository, times(1)).findAll();
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void buscarPessoaPorId_QuandoPessoaExistir_DeveRetornarPessoa() {
        // Arrange
        Long id = 1L;
        Pessoa pessoa = new Pessoa(id, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        // Act
        ResponseEntity<Pessoa> response = pessoaController.buscarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoa, response.getBody());

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void buscarPessoaPorId_QuandoPessoaNaoExistir_DeveRetornarNotFound() {
        // Arrange
        Long id = 1L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Pessoa> response = pessoaController.buscarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void criarPessoa_QuandoCpfInvalido_DeveRetornarBadRequest() {
        // Arrange
        Pessoa pessoa = new Pessoa(1L, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901");

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);

        // Act
        assertThrows(RuntimeException.class, () -> pessoaController.criarPessoa(pessoa));

        // Assert
        verify(pessoaRepository, times(1)).existsByCpf(pessoa.getCpf());
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void criarPessoa_QuandoCpfExistir_DeveRetornarBadRequest() {
        // Arrange
        Pessoa pessoa = new Pessoa(1L, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901");

        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);

        // Act
        pessoaController.criarPessoa(pessoa);

        // Assert
        verify(pessoaRepository, times(1)).existsByCpf(pessoa.getCpf());
        verify(pessoaRepository, times(1)).save(pessoa);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void atualizarPessoa_QuandoPessoaExistir_DeveRetornarPessoaAtualizada() {
        // Arrange
        Long id = 1L;
        Pessoa pessoaExistente = new Pessoa(id, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901");
        Pessoa pessoaAtualizada = new Pessoa(id, "Maria", "Rua B", "Cidade B", "Estado B", "9876543210", "maria@example.com", "09876543210");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        // Act
        ResponseEntity<Pessoa> response = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoaAtualizada, response.getBody());

        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).save(pessoaExistente);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void atualizarPessoa_QuandoPessoaNaoExistir_DeveRetornarNotFound() {
        // Arrange
        Long id = 1L;
        Pessoa pessoaAtualizada = new Pessoa(id, "Maria", "Rua B", "Cidade B", "Estado B", "9876543210", "maria@example.com", "09876543210");

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Pessoa> response = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void deletarPessoa_QuandoPessoaExistir_DeveRetornarNoContent() {
        // Arrange
        Long id = 1L;
        Pessoa pessoa = new Pessoa(id, "João", "Rua A", "Cidade A", "Estado A", "1234567890", "joao@example.com", "12345678901");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        // Act
        ResponseEntity<Void> response = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).delete(pessoa);
        verifyNoMoreInteractions(pessoaRepository);
    }

    @Test
    void deletarPessoa_QuandoPessoaNaoExistir_DeveRetornarNotFound() {
        // Arrange
        Long id = 1L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(pessoaRepository);
    }

    // Adicione aqui os demais testes para os demais métodos do PessoaController

}
