package com.kardbank.desafio.repository;


import com.kardbank.desafio.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    // Método para buscar uma pessoa pelo nome
    List<Pessoa> findByNome(String nome);

    // Método para buscar pessoas por cidade do endereço
    List<Pessoa> findByEnderecoCidade(String cidade);

    // Método para buscar pessoas por estado do endereço
    List<Pessoa> findByEnderecoEstado(String estado);

    // Método para buscar pessoas por telefone do contato
    List<Pessoa> findByContatosTelefone(String telefone);

    // Método para buscar pessoas por email do contato
    List<Pessoa> findByContatosEmail(String email);

    // Método para validação de cpf
    boolean existsByCpf(String cpf);
}


