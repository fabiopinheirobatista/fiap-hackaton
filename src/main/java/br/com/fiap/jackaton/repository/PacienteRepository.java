package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.PacienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<PacienteEntity, String> {
}
