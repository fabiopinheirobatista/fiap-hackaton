package br.com.fiap.jackaton.repository;

import br.com.fiap.jackaton.entity.UnitEntity;
import br.com.fiap.jackaton.enums.AppointmentType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, String> {

    @Query("select u from UnitEntity u join u.supportedTypes t where t = :type and (:location is null or :location = '' or lower(u.location) = lower(:location))")
    List<UnitEntity> findByTypeAndLocation(@Param("type") AppointmentType type, @Param("location") String location);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from UnitEntity u where u.id = :id")
    Optional<UnitEntity> findByIdForUpdate(@Param("id") String id);
}
