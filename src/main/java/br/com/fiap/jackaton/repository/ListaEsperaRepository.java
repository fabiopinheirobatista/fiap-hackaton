package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.ListaEsperaEntity;
import br.com.fiap.jackaton.enums.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ListaEsperaRepository extends JpaRepository<ListaEsperaEntity, String> {

    @Query("SELECT COUNT(l) FROM ListaEsperaEntity l WHERE (:unidadeId IS NULL OR l.unidade.id = :unidadeId) AND l.tipo = :tipo")
    long countByUnidadeIdAndTipo(@Param("unidadeId") String unidadeId, @Param("tipo") String tipo);

    @Query("SELECT COUNT(l) FROM ListaEsperaEntity l WHERE l.unidade.id = :unidadeId AND l.tipo = :tipo")
    long countByUnidadeIdAndEspecialidade(@Param("unidadeId") String unidadeId, @Param("tipo") String tipo);
}
