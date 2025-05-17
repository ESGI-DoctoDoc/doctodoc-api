package fr.esgi.doctodocapi.use_cases.patient;

import fr.esgi.doctodocapi.dtos.responses.GetAppointmentAvailableResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.GetCloseMemberResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.GetMedicalConcernsResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.doctor_questions.GetDoctorListQuestionsResponse;
import fr.esgi.doctodocapi.dtos.responses.making_appointment.doctor_questions.GetDoctorQuestionsResponse;
import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcern;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.MedicalConcernRepository;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.Question;
import fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern.question.QuestionType;
import fr.esgi.doctodocapi.model.patient.AppointmentsAvailabilityService;
import fr.esgi.doctodocapi.model.patient.Patient;
import fr.esgi.doctodocapi.model.patient.PatientRepository;
import fr.esgi.doctodocapi.model.user.User;
import fr.esgi.doctodocapi.model.user.UserRepository;
import fr.esgi.doctodocapi.use_cases.user.ports.in.GetCurrentUserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MakingAppointment {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicalConcernRepository medicalConcernRepository;
    private final UserRepository userRepository;

    private final AppointmentsAvailabilityService appointmentsAvailabilityService;
    private final GetCurrentUserContext getCurrentUserContext;

    public MakingAppointment(UserRepository userRepository,
                             PatientRepository patientRepository,
                             MedicalConcernRepository medicalConcernRepository,
                             DoctorRepository doctorRepository,
                             AppointmentsAvailabilityService appointmentsAvailabilityService,
                             GetCurrentUserContext getCurrentUserContext) {
        this.userRepository = userRepository;
        this.medicalConcernRepository = medicalConcernRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentsAvailabilityService = appointmentsAvailabilityService;
        this.getCurrentUserContext = getCurrentUserContext;
    }


    public List<GetCloseMemberResponse> getCloseMembers() {
        String email = this.getCurrentUserContext.getUsername();
        User user = this.userRepository.findByEmail(email);

        List<Patient> closeMembers = this.patientRepository.getCloseMembers(user.getId());

        List<GetCloseMemberResponse> closeMemberResponses = new ArrayList<>();
        closeMembers.forEach(closeMember -> {
            String fullName = closeMember.getFirstName() + " " + closeMember.getLastName();
            closeMemberResponses.add(new GetCloseMemberResponse(closeMember.getId(), fullName, false));
        });

        return closeMemberResponses;
    }

    public List<GetMedicalConcernsResponse> getMedicalConcerns(UUID doctorId) {
        List<MedicalConcern> medicalConcerns = this.medicalConcernRepository.getMedicalConcerns(doctorId);

        return medicalConcerns.stream()
                .map(medicalConcern -> new GetMedicalConcernsResponse(medicalConcern.getId(), medicalConcern.getName()))
                .toList();
    }

    public List<GetDoctorQuestionsResponse> getDoctorQuestions(UUID doctorId) {
        List<Question> doctorQuestions = this.medicalConcernRepository.getDoctorQuestions(doctorId);

        List<GetDoctorQuestionsResponse> questionsResponses = new ArrayList<>();

        doctorQuestions.forEach(question -> {
            if (question.getType() == QuestionType.LIST) {
                // todo do a mapper
                questionsResponses.add(new GetDoctorListQuestionsResponse(question.getType().getValue(),
                        question.getQuestion(), question.isMandatory(), question.getOptions()));
            } else {
                questionsResponses.add(new GetDoctorQuestionsResponse(question.getType().getValue(),
                        question.getQuestion(), question.isMandatory()));
            }
        });

        return questionsResponses;
    }

    public List<GetAppointmentAvailableResponse> getAppointmentsAvailability(UUID medicalConcernId, LocalDate date) {
        MedicalConcern medicalConcern = this.medicalConcernRepository.getMedicalConcernById(medicalConcernId);
        return this.appointmentsAvailabilityService.getAvailableAppointment(medicalConcern, date);
    }

    // todo : confirm

}
