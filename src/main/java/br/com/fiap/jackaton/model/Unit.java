package br.com.fiap.jackaton.model;

import br.com.fiap.jackaton.enums.AppointmentType;

import java.util.List;
import java.util.Objects;

public class Unit {
    private final String id;
    private final String name;
    private final String location;
    private final List<AppointmentType> supportedTypes;
    private final List<Slot> availableSlots;

    public Unit(String id, String name, String location, List<AppointmentType> supportedTypes, List<Slot> availableSlots) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.supportedTypes = supportedTypes;
        this.availableSlots = availableSlots;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<AppointmentType> getSupportedTypes() {
        return supportedTypes;
    }

    public List<Slot> getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Unit unit = (Unit) o;
        return Objects.equals(id, unit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

