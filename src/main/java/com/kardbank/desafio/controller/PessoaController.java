package com.kardbank.desafio.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kardbank.desafio.model.Contato;


import com.kardbank.desafio.model.Pessoa;
import com.kardbank.desafio.repository.PessoaRepository;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.apache.catalina.connector.Request.*;

@RestController
@RequestMapping("/pessoas")
@ControllerAdvice
public class PessoaController {
    @Autowired
    private PessoaRepository pessoaRepository;

    // Endpoint para listar todas as pessoas
    @GetMapping()
    public List<Pessoa> listarPessoas() {
        return pessoaRepository.findAll();
    }

    // Endpoint para buscar uma pessoa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPessoaPorId(@PathVariable Long id) {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);
        return optionalPessoa.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para criar uma nova pessoa
    @PostMapping()
    public Pessoa criarPessoa(@RequestBody Pessoa pessoa) {
        // Verifica se o CPF é válido
        if (!validarCpf(pessoa.getCpf())) {
            throw new RuntimeException("CPF inválido. Não é possível adicionar a pessoa.");
        }

        // Verifica se o CPF já existe no banco de dados
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new RuntimeException("CPF já existe. Não é possível adicionar a pessoa.");
        }

        // Realize outras validações aqui antes de salvar a pessoa

        List<Contato> contatos = pessoa.getContatos();
        for (Contato contato : contatos) {

            if (!validarTelefone(contato.getTelefone())) {
                throw new RuntimeException("Telefone inválido. Não é possível adicionar a pessoa.");
            }
        }

        return pessoaRepository.save(pessoa);
    }

    // Endpoint para atualizar uma pessoa existente
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarPessoa(@PathVariable Long id, @RequestBody Pessoa pessoaAtualizada) {
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

    // Endpoint para deletar uma pessoa
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

    // Método para validar o CPF
    public boolean validarCpf(String cpf) {
        // Implemente a lógica de validação do CPF aqui

        // Verifica se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        // Outras regras de validação do CPF...

        return true;
    }

    // Constantes da API de validação de telefone
    private static final String API_KEY = "ab4907d4c9b04707a1243074f2254e69";
    private static final String API_URL = "https://phonevalidation.abstractapi.com/v1/";

    // Método para validar o telefone
    public boolean validarTelefone(String telefone) {
        try {
            String requestUrl = API_URL + "?api_key=" + API_KEY + "&phone="+ telefone;

            Content content = Request.Get(requestUrl).execute().returnContent();

            // Analise a resposta da API para determinar se o telefone é válido
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content.asString());
            boolean isValid = jsonNode.get("valid").asBoolean();

            return isValid;
        } catch (IOException error) {
            System.out.println(error);
            return false;
        }
    }

    // Tratamento de exceções
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        String mensagemErro = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse erro = new ErrorResponse(mensagemErro, status.value());

        return ResponseEntity.status(status).body(erro);
    }
}