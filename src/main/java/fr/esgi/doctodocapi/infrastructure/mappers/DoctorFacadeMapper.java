package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorFacadeMapper {
    private final DoctorMapper doctorMapper;
    private final SlotMapper slotMapper;
    private final AbsenceMapper absenceMapper;
    private final MedicalConcernMapper medicalConcernMapper;

    public DoctorFacadeMapper(DoctorMapper doctorMapper, SlotMapper slotMapper, AbsenceMapper absenceMapper, MedicalConcernMapper medicalConcernMapper) {
        this.doctorMapper = doctorMapper;
        this.slotMapper = slotMapper;
        this.absenceMapper = absenceMapper;
        this.medicalConcernMapper = medicalConcernMapper;
    }

    public Doctor mapDoctorToDomain(DoctorEntity doctorEntity) {
        List<Slot> slots = doctorEntity.getSlots().stream().map(slotMapper::toDomain).toList();
        List<Absence> absences = doctorEntity.getAbsences().stream().map(absenceMapper::toDomain).toList();
        List<MedicalConcern> medicalConcerns = doctorEntity.getMedicalConcerns().stream().map(medicalConcernMapper::toDomain).toList();
        return this.doctorMapper.toDomain(doctorEntity, slots, absences, medicalConcerns);
    }
}
