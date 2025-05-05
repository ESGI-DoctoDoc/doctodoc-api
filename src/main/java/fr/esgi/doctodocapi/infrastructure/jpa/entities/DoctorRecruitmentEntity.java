package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "doctor_recruitment")
public class DoctorRecruitmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "doctor_recruitment_id")
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
