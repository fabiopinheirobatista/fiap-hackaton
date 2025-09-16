package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.BloqueioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloqueioRepository extends JpaRepository<BloqueioEntity, String> {

    @Query("SELECT b FROM BloqueioEntity b WHERE b.unidade.id = :unidadeId AND b.dataHora >= :inicio AND b.dataHora < :fim")
    List<BloqueioEntity> findByUnidadeIdAndDataHoraBetween(@Param("unidadeId") String unidadeId,
                                                            @Param("inicio") LocalDateTime inicio,
                                                            @Param("fim") LocalDateTime fim);
}

