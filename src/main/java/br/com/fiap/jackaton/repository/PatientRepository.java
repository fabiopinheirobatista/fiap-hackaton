package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientEntity, String> {
}
