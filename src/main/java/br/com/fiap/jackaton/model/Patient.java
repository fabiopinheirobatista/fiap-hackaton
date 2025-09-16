package br.com.fiap.jackaton.model;

import java.util.Objects;

public class Patient {
    private final String id;
    private final String name;
    private final boolean active;

    public Patient(String id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

