package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<PacienteEntity, String> {
}
