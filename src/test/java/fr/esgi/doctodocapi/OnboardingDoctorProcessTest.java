//package fr.esgi.doctodocapi;
//
//import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_doctor_account.OnBoardingDoctorRequest;
//import fr.esgi.doctodocapi.model.doctor.Doctor;
//import fr.esgi.doctodocapi.model.doctor.DoctorRepository;
//import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
//import fr.esgi.doctodocapi.model.user.User;
//import fr.esgi.doctodocapi.model.user.UserNotFoundException;
//import fr.esgi.doctodocapi.model.user.UserRepository;
//import fr.esgi.doctodocapi.model.vo.email.Email;
//import fr.esgi.doctodocapi.model.vo.password.Password;
//import fr.esgi.doctodocapi.model.vo.phone_number.PhoneNumber;
//import fr.esgi.doctodocapi.use_cases.admin.validate_doctor_account.ManageValidationDoctorAccount;
//import fr.esgi.doctodocapi.use_cases.doctor.manage_doctor_account.OnboardingDoctorProcess;
//import fr.esgi.doctodocapi.use_cases.exceptions.on_boarding.DoctorAccountAlreadyExist;
//import fr.esgi.doctodocapi.use_cases.user.ports.out.GetCurrentUserContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class OnboardingDoctorProcessTest {
//
//    @Mock private DoctorRepository doctorRepository;
//    @Mock private UserRepository userRepository;
//    @Mock private GetCurrentUserContext authContext;
//
//    @InjectMocks
//    private OnboardingDoctorProcess onboarding;
//
//    @InjectMocks
//    private ManageValidationDoctorAccount manageValidationDoctorAccount;
//
//    private final UUID userId = UUID.randomUUID();
//    private User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        mockUser = new User(
//                userId,
//                Email.of("doctor1@example.com"),
//                Password.of("Abdcd76@"),
//                PhoneNumber.of("+33123456789"),
//                true, true, "123456", LocalDateTime.now()
//        );
//    }
//
//    @Test
//    void should_throw_when_user_not_found_on_onboarding_process() {
//        when(authContext.getUsername()).thenReturn("notfound@example.com");
//        when(userRepository.findByEmail("notfound@example.com")).thenThrow(UserNotFoundException.class);
//
//        OnBoardingDoctorRequest request = new OnBoardingDoctorRequest(
//                "12345678801",
//                "Cardiology",
//                (short) 5,
//
//                true,
//                "John",
//                "Doe",
//                LocalDate.of(1980, 5, 12),
//                "bio",
//                "url",
//                List.of("English"),
//                List.of("https://ex.com/1.pdf", "https://ex.com/2.pdf"),
//                "FEMALE"
//        );
//
//        assertThatThrownBy(() -> onboarding.process(request))
//                .isInstanceOf(UserNotFoundException.class);
//    }
//
//    @Test
//    void should_save_doctor_on_valid_onboarding_process() {
//        when(authContext.getUsername()).thenReturn("doctor1@example.com");
//        when(userRepository.findByEmail("doctor1@example.com")).thenReturn(mockUser);
//
//        OnBoardingDoctorRequest request = new OnBoardingDoctorRequest(
//                "12345678801",
//                "Cardiology",
//                (short) 5,
//
//                true,
//                "John",
//                "Doe",
//                LocalDate.of(1980, 5, 12),
//                "bio",
//                "url",
//                List.of("English"),
//                List.of("https://ex.com/1.pdf", "https://ex.com/2.pdf"),
//                "FEMALE"
//        );
//
//        onboarding.process(request);
//
//        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
//        verify(doctorRepository).save(captor.capture());
//
//        Doctor saved = captor.getValue();
//        assertThat(saved.getProfessionalInformations().getRpps().getValue()).isEqualTo("12345678801");
//        assertThat(saved.getPersonalInformations().getFirstName()).isEqualTo("John");
//        assertThat(saved.getPersonalInformations().getLastName()).isEqualTo("Doe");
//        assertThat(saved.getProfessionalInformations().getSpeciality()).isEqualTo("Cardiology");
//    }
//
//    @Test
//    void should_trim_fields_in_onboarding_request() {
//        when(authContext.getUsername()).thenReturn("doctor1@example.com");
//        when(userRepository.findByEmail("doctor1@example.com")).thenReturn(mockUser);
//
//        OnBoardingDoctorRequest request = new OnBoardingDoctorRequest(
//                "  12345678801  ",
//                "  Cardiology ",
//                (short) 5,
//                true,
//                " John ",
//                " Doe ",
//                LocalDate.of(1980, 5, 12),
//                " bio ",
//                " url ",
//                List.of(" English ", " French "),
//                List.of("  https://ex.com/1.pdf  ", "  https://ex.com/2.pdf  "),
//                "FEMALE"
//        );
//
//        onboarding.process(request);
//
//        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
//        verify(doctorRepository).save(captor.capture());
//
//        Doctor saved = captor.getValue();
//        assertThat(saved.getProfessionalInformations().getRpps().getValue()).isEqualTo("12345678801");
//        assertThat(saved.getPersonalInformations().getFirstName()).isEqualTo("John");
//        assertThat(saved.getPersonalInformations().getLastName()).isEqualTo("Doe");
//        assertThat(saved.getProfessionalInformations().getSpeciality()).isEqualTo("Cardiology");
//        assertThat(saved.getProfessionalInformations().getBio()).isEqualTo("bio");
//        assertThat(saved.getPersonalInformations().getProfilePictureUrl()).isEqualTo("url");
//        assertThat(saved.getProfessionalInformations().getLanguages()).containsExactly("English", "French");
//    }
//
//    @Test
//    void should_throw_when_doctor_not_found_on_validation() {
//        UUID unknownDoctorId = UUID.randomUUID();
//        when(doctorRepository.findDoctorByUserId(unknownDoctorId)).thenReturn(null);
//
//        assertThatThrownBy(() -> manageValidationDoctorAccount.validateDoctorAccount(unknownDoctorId))
//                .isInstanceOf(DoctorNotFoundException.class);
//    }
//
//    @Test
//    void should_throw_when_doctor__account_already_exists() {
//        when(authContext.getUsername()).thenReturn("doctor1@example.com");
//        when(userRepository.findByEmail("doctor1@example.com")).thenReturn(mockUser);
//        when(doctorRepository.isExistsById(userId)).thenReturn(true);
//
//        OnBoardingDoctorRequest request = new OnBoardingDoctorRequest(
//                "12345678801", "Cardiology", (short) 5,
//                 true,
//                "John", "Doe", LocalDate.of(1980, 5, 12), "bio", "url",
//                List.of("English"), List.of("http://doc1.pdf"), "FEMALE"
//        );
//
//        assertThatThrownBy(() -> onboarding.process(request))
//            .isInstanceOf(DoctorAccountAlreadyExist.class);
//    }
//}
