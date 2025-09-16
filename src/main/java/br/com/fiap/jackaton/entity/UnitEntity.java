package br.com.fiap.jackaton.entity;

import br.com.fiap.jackaton.enums.AppointmentType;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
public class UnitEntity {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @ElementCollection(targetClass = AppointmentType.class)
    @CollectionTable(name = "unit_supported_types", joinColumns = @JoinColumn(name = "unit_id"))
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private List<AppointmentType> supportedTypes = new ArrayList<>();

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlotEntity> availableSlots = new ArrayList<>();

    public UnitEntity() {}

    public UnitEntity(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<AppointmentType> getSupportedTypes() {
        return supportedTypes;
    }

    public void setSupportedTypes(List<AppointmentType> supportedTypes) {
        this.supportedTypes = supportedTypes;
    }

    public List<SlotEntity> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<SlotEntity> availableSlots) {
        this.availableSlots = availableSlots;
    }

    public void addSlot(SlotEntity slot) {
        slot.setUnit(this);
        this.availableSlots.add(slot);
    }
}

