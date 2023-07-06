package com.kardbank.desafio;

import com.kardbank.desafio.controller.PessoaController;
import com.kardbank.desafio.model.Contato;
import com.kardbank.desafio.model.Pessoa;
import com.kardbank.desafio.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaControllerTest {
    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaController pessoaController;

    @Test
    void listarPessoas_DeveRetornarListaDePessoas() {
        // Arrange
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(new Pessoa(1L, "Nome1", "Endereço1", new Contato("Telefone1")));
        pessoas.add(new Pessoa(2L, "Nome2", "Endereço2", new Contato("Telefone2")));
        when(pessoaRepository.findAll()).thenReturn(pessoas);

        // Act
        List<Pessoa> resultado = pessoaController.listarPessoas();

        // Assert
        assertEquals(pessoas.size(), resultado.size());
        assertEquals(pessoas, resultado);
    }

    @Test
    void buscarPessoaPorId_DeveRetornarPessoaExistente() {
        // Arrange
        Long id = 1L;
        Pessoa pessoa = new Pessoa(id, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.buscarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pessoa, responseEntity.getBody());
    }

    @Test
    void buscarPessoaPorId_DeveRetornarNotFoundQuandoPessoaNaoExistir() {
        // Arrange
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.buscarPessoaPorId(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void criarPessoa_DeveCriarPessoaQuandoCpfETelefoneValidos() {
        // Arrange
        Pessoa pessoa = new Pessoa(1L, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
        when(pessoaRepository.save(pessoa)).thenReturn(pessoa);

        // Act
        Pessoa resultado = pessoaController.criarPessoa(pessoa);

        // Assert
        assertNotNull(resultado);
        assertEquals(pessoa, resultado);
    }

    @Test
    void criarPessoa_DeveLancarExcecaoQuandoCpfInvalido() {
        // Arrange
        Pessoa pessoa = new Pessoa(1L, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
        // Simula o caso de CPF inválido
        when(pessoaController.validarCpf(pessoa.getCpf())).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pessoaController.criarPessoa(pessoa));
    }

    @Test
    void criarPessoa_DeveLancarExcecaoQuandoCpfJaExistir() {
        // Arrange
        Pessoa pessoa =new Pessoa(1L, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pessoaController.criarPessoa(pessoa));
    }

    @Test
    void criarPessoa_DeveLancarExcecaoQuandoTelefoneInvalido() {
        // Arrange
        Pessoa pessoa = new Pessoa(1L, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.existsByCpf(pessoa.getCpf())).thenReturn(false);
        // Simula o caso de telefone inválido
        when(pessoaController.validarCpf(pessoa.getCpf())).thenReturn(true);
        //when(pessoaController.validarTelefone(pessoa.getContatos().getTelefone())).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> pessoaController.criarPessoa(pessoa));
    }

    @Test
    void atualizarPessoa_DeveRetornarPessoaAtualizadaQuandoPessoaExistir() {
        // Arrange
        Long id = 1L;
        Pessoa pessoaExistente = new Pessoa(id, "Nome1", "Endereço1", new Contato("Telefone1"));
        Pessoa pessoaAtualizada = new Pessoa(id, "Nome2", "Endereço2", new Contato("Telefone2"));
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pessoaAtualizada, responseEntity.getBody());
    }

    @Test
    void atualizarPessoa_DeveRetornarNotFoundQuandoPessoaNaoExistir() {
        // Arrange
        Long id = 1L;
        Pessoa pessoaAtualizada = new Pessoa(id, "Nome2", "Endereço2", new Contato("Telefone2"));
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Object> responseEntity = pessoaController.atualizarPessoa(id, pessoaAtualizada);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deletarPessoa_DeveRetornarNoContentQuandoPessoaExistir() {
        // Arrange
        Long id = 1L;
        Pessoa pessoa = new Pessoa(id, "Nome", "Endereço", new Contato("Telefone"));
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        // Act
        ResponseEntity<Void> responseEntity = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(pessoaRepository, times(1)).delete(pessoa);
    }

    @Test
    void deletarPessoa_DeveRetornarNotFoundQuandoPessoaNaoExistir() {
        // Arrange
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> responseEntity = pessoaController.deletarPessoa(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(pessoaRepository, never()).delete(any());
    }

    // Testes para os métodos validarCpf e validarTelefone...

}
