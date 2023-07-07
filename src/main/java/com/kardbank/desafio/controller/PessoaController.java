package com.kardbank.desafio.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kardbank.desafio.model.Contato;
import com.kardbank.desafio.model.Pessoa;
import com.kardbank.desafio.repository.PessoaRepository;
import com.kardbank.desafio.service.TelefoneService;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
@ControllerAdvice
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneService telefoneService;

    @GetMapping()
    @ResponseBody
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPessoaPorId(@PathVariable Long id) {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);
        return optionalPessoa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa) {
        if (!validarCpf(pessoa.getCpf())) {
            throw new RuntimeException("CPF inválido. Não é possível adicionar a pessoa.");
        }
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RuntimeException("CPF já existe. Não é possível adicionar a pessoa.");
        }
        // Verifica se o telefone é válido
        for (Contato contato : pessoa.getContatos()) {
            if (contato.getTipo() == Contato.Tipo.TELEFONE) {
                String telefone = contato.getValor();
                if (!telefoneService.validarTelefone(telefone)) {
                    throw new RuntimeException("Telefone inválido. Não é possível adicionar a pessoa.");
                }
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaRepository.save(pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoaAtualizada) {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);
        if (optionalPessoa.isPresent()) {
            Pessoa pessoa = optionalPessoa.get();
            pessoa.setNome(pessoaAtualizada.getNome());
            pessoa.setEndereco(pessoaAtualizada.getEndereco());
            pessoa.setContatos(pessoaAtualizada.getContatos());
            pessoaRepository.save(pessoa);
            return ResponseEntity.ok(pessoa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);
        if (optionalPessoa.isPresent()) {
            pessoaRepository.delete(optionalPessoa.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public boolean validarCpf(String cpf) {
        // Implemente a lógica de validação do CPF aqui
        if (cpf == null || cpf.isEmpty() || cpf.length() != 11) {
            return false;
        }
        // Outras regras de validação do CPF...
        return true;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        String mensagemErro = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse erro = new ErrorResponse(mensagemErro, status.value());
        return ResponseEntity.status(status).body(erro);
    }
}
