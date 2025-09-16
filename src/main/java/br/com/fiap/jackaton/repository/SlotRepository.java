package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SlotRepository extends JpaRepository<SlotEntity, Long> {

    @Modifying
    @Query("delete from SlotEntity s where s.unit.id = :unitId and s.dateTime = :dateTime and ((:professionalId is null and s.professionalId is null) or s.professionalId = :professionalId)")
    int deleteByUnitIdAndDateTimeAndProfessionalId(@Param("unitId") String unitId, @Param("dateTime") LocalDateTime dateTime, @Param("professionalId") String professionalId);
}

