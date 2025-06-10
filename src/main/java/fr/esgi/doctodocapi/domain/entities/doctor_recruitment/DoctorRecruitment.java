package fr.esgi.doctodocapi.domain.entities.doctor_recruitment;

public class DoctorRecruitment {
    private String lastName;
    private String firstName;

    private DoctorRecruitment(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public static DoctorRecruitment create(String firstName, String lastName) {
        return new DoctorRecruitment(lastName, firstName);
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
}
