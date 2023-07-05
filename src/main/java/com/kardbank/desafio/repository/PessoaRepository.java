package com.kardbank.desafio.repository;

import com.kardbank.desafio.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository <Pessoa, Long>{

    Iterable<Pessoa> listarPessoas();

    Pessoa listarPessoasId(Long id);

    void adicionarPessoa(Pessoa pessoa);

    void atualizarPessoa(Long id, Pessoa pessoa);

    void deletarPessoa(Long id);

}
