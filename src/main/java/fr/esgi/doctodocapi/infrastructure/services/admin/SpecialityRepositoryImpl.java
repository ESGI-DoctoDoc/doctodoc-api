package fr.esgi.doctodocapi.infrastructure.services.admin;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SpecialityEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.SpecialityJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.SpecialityMapper;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityAlreadyExistException;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityNotFoundException;
import fr.esgi.doctodocapi.model.admin.speciality.SpecialityRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the {@link SpecialityRepository} interface.
 * <p>
 * This service provides methods to persist and retrieve {@link Speciality} domain objects
 * using JPA repositories and mappers to interact with the database layer.
 */
@Repository
public class SpecialityRepositoryImpl implements SpecialityRepository {
    private final SpecialityJpaRepository specialityJpaRepository;
    private final SpecialityMapper specialityMapper;

    public SpecialityRepositoryImpl(SpecialityJpaRepository specialityJpaRepository, SpecialityMapper specialityMapper) {
        this.specialityJpaRepository = specialityJpaRepository;
        this.specialityMapper = specialityMapper;
    }

    /**
     * Saves a {@link Speciality} to the database.
     * The domain object is mapped to an entity before being persisted, and then mapped back to domain form.
     *
     * @param speciality the speciality domain object to be saved
     * @return the persisted speciality as a domain object
     */
    @Override
    public Speciality save(Speciality speciality) {
        SpecialityEntity entityToBeSaved = this.specialityMapper.toEntity(speciality);
        this.specialityJpaRepository.save(entityToBeSaved);
        return this.specialityMapper.toDomain(entityToBeSaved);
    }

    /**
     * Retrieves all stored {@link Speciality} objects from the database.
     * The returned list contains domain representations of the entities.
     *
     * @return a list of all specialities
     */
    @Override
    public List<Speciality> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SpecialityEntity> entities = this.specialityJpaRepository.findAll(pageable);
        return entities.stream().map(specialityMapper::toDomain).toList();
    }

    @Override
    public Speciality findByName(String name) {
        return this.specialityJpaRepository.findByName(name)
                .map(specialityMapper::toDomain).orElseThrow(SpecialityNotFoundException::new);
    }

    @Override
    public Speciality update(UUID id, String newName) {
        SpecialityEntity entity = this.specialityJpaRepository.findById(id)
                .orElseThrow(SpecialityNotFoundException::new);

        if (this.specialityJpaRepository.existsByNameIgnoreCase(newName)) {
            throw new SpecialityAlreadyExistException();
        }

        entity.setName(newName);

        SpecialityEntity saved = this.specialityJpaRepository.save(entity);
        return this.specialityMapper.toDomain(saved);
    }

    @Override
    public boolean existsByName(String name) {
        return this.specialityJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Speciality findById(UUID id) {
        return this.specialityJpaRepository.findById(id)
                .map(specialityMapper::toDomain)
                .orElseThrow(SpecialityNotFoundException::new);
    }

    @Override
    public List<Speciality> searchSpecialitiesByName(String name, int page, int size) {
        String nameLower = (name == null || name.isBlank()) ? null : name.toLowerCase();
        Pageable pageable = PageRequest.of(page, size);
        Page<SpecialityEntity> specialities = this.specialityJpaRepository.searchByName(nameLower, pageable);
        return specialities.stream().map(this.specialityMapper::toDomain).toList();
    }
}
