package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.entities.UserEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.DoctorJpaRepository;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.UserJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.DoctorMapper;
import fr.esgi.doctodocapi.domain.entities.doctor.Doctor;
import fr.esgi.doctodocapi.domain.entities.doctor.DoctorRepository;
import fr.esgi.doctodocapi.domain.entities.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.domain.entities.user.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the DoctorRepository interface.
 * This service provides methods to manage doctor data, including retrieving, checking existence,
 * and saving doctor information.
 */
@Service
public class DoctorRepositoryImpl implements DoctorRepository {
    /**
     * Repository for accessing doctor data in the database.
     */
    private final DoctorJpaRepository doctorJpaRepository;

    /**
     * Mapper for converting between doctor domain objects and entities.
     */
    private final DoctorMapper doctorMapper;

    /**
     * Repository for accessing user data in the database.
     */
    private final UserJpaRepository userJpaRepository;

    /**
     * Constructs a DoctorRepositoryImpl with the required repositories and mapper.
     *
     * @param doctorJpaRepository Repository for doctor data access
     * @param doctorMapper        Mapper for doctor domain objects and entities
     * @param userJpaRepository   Repository for user data access
     */
    public DoctorRepositoryImpl(DoctorJpaRepository doctorJpaRepository, DoctorMapper doctorMapper, UserJpaRepository userJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
        this.doctorMapper = doctorMapper;
        this.userJpaRepository = userJpaRepository;
    }


    /**
     * Retrieves a doctor by their unique identifier.
     *
     * @param id The unique identifier of the doctor to retrieve
     * @return The doctor with the specified ID
     * @throws DoctorNotFoundException If no doctor with the specified ID exists
     */
    @Override
    public Doctor getById(UUID id) throws DoctorNotFoundException {
        DoctorEntity entity = this.doctorJpaRepository.findById(id).orElseThrow(DoctorNotFoundException::new);
        return this.doctorMapper.toDomain(entity);
    }

    /**
     * Checks if a doctor exists with the specified ID.
     *
     * @param doctorId The unique identifier of the doctor to check
     * @return true if a doctor with the specified ID exists, false otherwise
     */
    @Override
    public boolean isExistsById(UUID doctorId) {
        return this.doctorJpaRepository.existsById(doctorId);
    }

    /**
     * Retrieves a doctor by their associated user ID.
     *
     * @param userId The unique identifier of the user associated with the doctor
     * @return An Optional containing the doctor if found, or an empty Optional if not found
     */
    @Override
    public Optional<Doctor> getByUserId(UUID userId) {
        Optional<DoctorEntity> doctor = this.doctorJpaRepository.findByUser_Id(userId);

        if (doctor.isPresent()) {
            DoctorEntity entity = doctor.get();
            return Optional.of(this.doctorMapper.toDomain(entity));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Doctor> searchDoctors(String name, String speciality, List<String> languages, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        String[] array = (languages != null) ? languages.toArray(new String[0]) : new String[0];

        Page<DoctorEntity> doctors = this.doctorJpaRepository.searchDoctors(name, speciality, array, pageable);
        return doctors.stream().map(this.doctorMapper::toDomain).toList();
    }

    /**
     * Saves a doctor to the database.
     * This method retrieves the associated user entity before saving the doctor.
     *
     * @param doctor The doctor to save
     * @throws UserNotFoundException If the user associated with the doctor does not exist
     */
    @Override
    public void save(Doctor doctor) {
        UserEntity user = this.userJpaRepository.findById(doctor.getUserId()).orElseThrow(UserNotFoundException::new);
        DoctorEntity entityToSaved = this.doctorMapper.toEntity(doctor, user);
        this.doctorJpaRepository.save(entityToSaved);
    }

    /**
     * Finds a doctor by their user ID.
     * Unlike getByUserId, this method throws an exception if the doctor is not found.
     *
     * @param doctorId The unique identifier of the doctor to find
     * @return The doctor with the specified ID
     * @throws DoctorNotFoundException If no doctor with the specified ID exists
     */
    @Override
    public Doctor findDoctorByUserId(UUID doctorId) {
        DoctorEntity entity = this.doctorJpaRepository.findById(doctorId)
                .orElseThrow(DoctorNotFoundException::new);
        return doctorMapper.toDomain(entity);
    }

}
