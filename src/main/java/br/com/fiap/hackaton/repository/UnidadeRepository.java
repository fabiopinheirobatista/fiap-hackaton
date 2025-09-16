package br.com.fiap.hackaton.repository;

import br.com.fiap.hackaton.entity.UnidadeEntity;
import br.com.fiap.hackaton.enums.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends JpaRepository<UnidadeEntity, String> {

    @Query("SELECT u FROM UnidadeEntity u WHERE :tipo MEMBER OF u.tiposSuportados AND (:localizacao IS NULL OR u.localizacao = :localizacao)")
    List<UnidadeEntity> buscarPorTipoELocalizacao(@Param("tipo") TipoAgendamento tipo, @Param("localizacao") String localizacao);
}
