package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.entity.TriagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriagemRepository extends JpaRepository<TriagemEntity, String> {
}
