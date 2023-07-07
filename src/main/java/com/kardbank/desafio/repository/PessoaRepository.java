package com.kardbank.desafio.repository;


import com.kardbank.desafio.model.Contato;
import com.kardbank.desafio.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    List<Pessoa> findByNome(String nome);
    List<Pessoa> findByEnderecoCidade(String cidade);
    List<Pessoa> findByEnderecoEstado(String estado);
    List<Pessoa> findByContatosTipoAndContatosValor(Contato.Tipo tipo, String valor);
    boolean existsByCpf(String cpf);
}



