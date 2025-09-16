package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.AgendamentoEntity;
import br.com.fiap.jackaton.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<AgendamentoEntity, String> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AgendamentoEntity a " +
           "WHERE a.pacienteId = :pacienteId AND a.dataHora = :dataHora AND a.status <> :status")
    boolean existePorPacienteIdEDataHoraEStatusDiferenteDe(
        @Param("pacienteId") String pacienteId,
        @Param("dataHora") LocalDateTime dataHora,
        @Param("status") StatusAgendamento status
    );

    @Query("SELECT a FROM AgendamentoEntity a WHERE a.unidadeId = :unidadeId AND a.dataHora >= :inicio AND a.dataHora < :fim")
    List<AgendamentoEntity> findByUnidadeIdAndDataHoraBetween(@Param("unidadeId") String unidadeId,
                                                               @Param("inicio") LocalDateTime inicio,
                                                               @Param("fim") LocalDateTime fim);
}
