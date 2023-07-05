package com.kardbank.desafio.controller;


import com.kardbank.desafio.model.Pessoa;
import com.kardbank.desafio.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoas")
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
}