package fr.esgi.doctodocapi.infrastructure.mappers;

import fr.esgi.doctodocapi.infrastructure.jpa.entities.DoctorEntity;
import fr.esgi.doctodocapi.infrastructure.mappers.document_trace_mapper.DocumentTraceMapper;
import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.Doctor;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorFacadeMapper {
    private final DoctorMapper doctorMapper;
    private final SlotMapper slotMapper;
    private final AbsenceMapper absenceMapper;
    private final MedicalConcernMapper medicalConcernMapper;
    private final DocumentMapper documentMapper;
    private final DocumentTraceMapper documentTraceMapper;
    private final SpecialityMapper specialityMapper;

    public DoctorFacadeMapper(DoctorMapper doctorMapper, SlotMapper slotMapper, AbsenceMapper absenceMapper, MedicalConcernMapper medicalConcernMapper, DocumentMapper documentMapper, DocumentTraceMapper documentTraceMapper, SpecialityMapper specialityMapper) {
        this.doctorMapper = doctorMapper;
        this.slotMapper = slotMapper;
        this.absenceMapper = absenceMapper;
        this.medicalConcernMapper = medicalConcernMapper;
        this.documentMapper = documentMapper;
        this.documentTraceMapper = documentTraceMapper;
        this.specialityMapper = specialityMapper;
    }

    public Doctor mapDoctorToDomain(DoctorEntity doctorEntity) {
        List<Slot> slots = doctorEntity.getSlots().stream().map(slotMapper::toDomain).toList();
        List<Absence> absences = doctorEntity.getAbsences().stream().map(absenceMapper::toDomain).toList();
        List<MedicalConcern> medicalConcerns = doctorEntity.getMedicalConcerns().stream().map(medicalConcernMapper::toDomain).toList();
        Speciality speciality = specialityMapper.toDomain(doctorEntity.getSpeciality());

        List<Document> documents = new ArrayList<>(
                doctorEntity.getDoctorDocuments()
                        .stream()
                        .map(document -> documentMapper.toDomain(
                                document,
                                document.getTraces().stream().map(documentTraceMapper::toDomain).toList()
                        ))
                        .toList()
        );

        return this.doctorMapper.toDomain(doctorEntity, slots, absences, medicalConcerns, documents, speciality);
    }
}
