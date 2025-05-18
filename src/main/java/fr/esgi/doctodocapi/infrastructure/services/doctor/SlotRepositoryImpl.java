package fr.esgi.doctodocapi.infrastructure.services.doctor;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.SlotEntity;
import fr.esgi.doctodocapi.infrastructure.jpa.repositories.SlotJpaRepository;
import fr.esgi.doctodocapi.infrastructure.mappers.AppointmentFacadeMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.MedicalConcernMapper;
import fr.esgi.doctodocapi.infrastructure.mappers.SlotMapper;
import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.Slot;
import fr.esgi.doctodocapi.model.doctor.calendar.SlotRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class SlotRepositoryImpl implements SlotRepository {
    private final SlotJpaRepository slotJpaRepository;
    private final SlotMapper slotMapper;
    private final AppointmentFacadeMapper appointmentFacadeMapper;
    private final MedicalConcernMapper medicalConcernMapper;

    public SlotRepositoryImpl(SlotJpaRepository slotJpaRepository, SlotMapper slotMapper, AppointmentFacadeMapper appointmentFacadeMapper, MedicalConcernMapper medicalConcernMapper) {
        this.slotJpaRepository = slotJpaRepository;
        this.slotMapper = slotMapper;
        this.appointmentFacadeMapper = appointmentFacadeMapper;
        this.medicalConcernMapper = medicalConcernMapper;
    }

    @Override
    public List<Slot> getSlotsByMedicalConcernAndDate(UUID medicalConcernId, LocalDate date) {
        List<SlotEntity> slotsFoundByMedicalConcern = this.slotJpaRepository.findAllByMedicalConcerns_IdAndDate(medicalConcernId, date);

        return slotsFoundByMedicalConcern.stream().map(slotEntity -> {
            List<Appointment> appointments = slotEntity.getAppointments().stream().map(this.appointmentFacadeMapper::mapAppointmentToDomain).toList();
            List<MedicalConcern> medicalConcerns = slotEntity.getMedicalConcerns().stream().map(this.medicalConcernMapper::toDomain).toList();
            return this.slotMapper.toDomain(slotEntity, appointments, medicalConcerns);
        }).toList();
    }

    @Override
    public Slot getById(UUID id) {
        SlotEntity entity = this.slotJpaRepository.findById(id).orElseThrow(SlotNotFoundException::new);
        List<Appointment> appointments = entity.getAppointments().stream().map(this.appointmentFacadeMapper::mapAppointmentToDomain).toList();
        List<MedicalConcern> medicalConcerns = entity.getMedicalConcerns().stream().map(this.medicalConcernMapper::toDomain).toList();

        return this.slotMapper.toDomain(entity, appointments, medicalConcerns);
    }


}
