package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.entity.HorarioDisponivelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HorarioRepository extends JpaRepository<HorarioDisponivelEntity, Long> {

    @Modifying
    @Query("delete from HorarioDisponivelEntity h where h.unidade.id = :unidadeId and h.dataHora = :dataHora and ((:profissionalId is null and h.idProfissional is null) or h.idProfissional = :profissionalId)")
    int deleteByUnidadeIdAndDataHoraAndProfissionalId(@Param("unidadeId") String unidadeId,
                                                      @Param("dataHora") LocalDateTime dataHora,
                                                      @Param("profissionalId") String profissionalId);
}
