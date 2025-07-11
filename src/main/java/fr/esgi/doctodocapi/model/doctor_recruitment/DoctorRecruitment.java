package fr.esgi.doctodocapi.model.doctor_recruitment;

import java.time.LocalDateTime;
import java.util.UUID;

public class DoctorRecruitment {
    private UUID id;
    private String lastName;
    private String firstName;
    private LocalDateTime createdAt;

    public DoctorRecruitment(UUID id, String lastName, String firstName, LocalDateTime createdAt) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.createdAt = createdAt;
    }

    public static DoctorRecruitment create(String firstName, String lastName) {
        return new DoctorRecruitment(UUID.randomUUID(), lastName, firstName, LocalDateTime.now());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
