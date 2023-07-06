package com.kardbank.desafio.model;


import javax.persistence.*;
import java.util.List;


@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Contato> contatos;

    private String cpf;

    public Pessoa() {
        // Construtor vazio
    }

    public Pessoa(Long id, String nome, Endereco endereco, List<Contato> contatos, String cpf) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.contatos = contatos;
        this.cpf = cpf;
    }

    public Pessoa(Long id, String nome, String endereço, Contato telefone) {
    }
// getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
