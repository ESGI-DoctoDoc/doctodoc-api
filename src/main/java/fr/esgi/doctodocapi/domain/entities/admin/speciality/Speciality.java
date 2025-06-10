package fr.esgi.doctodocapi.domain.entities.admin.speciality;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a medical speciality that can be associated with doctors.
 * <p>
 * A speciality is identified by a unique UUID, has a name, and a creation date.
 * Instances of this class can be created using the static factory method {@link #create(String)},
 * which sets the creation date to the current date.
 */
public class Speciality {
    private UUID id;
    private String name;
    private LocalDate createdAt;

    public Speciality(UUID id, String name, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
    }

    private Speciality(String name, LocalDate createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }

    /**
     * Static factory method to create a new {@code Speciality} with the current date as creation date.
     *
     * @param name the name of the speciality
     * @return a new {@code Speciality} instance
     */
    public static Speciality create(String name) {
        return new Speciality(name, LocalDate.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Speciality that = (Speciality) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
