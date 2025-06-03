package fr.esgi.doctodocapi.model.doctor.personal_information;

import fr.esgi.doctodocapi.model.vo.birthdate.Birthdate;

public class DoctorPersonnalInformations {
    private String profilePictureUrl;
    private String firstName;
    private String lastName;
    private Birthdate birthDate;
    private Gender gender;

    public DoctorPersonnalInformations(String profilePictureUrl, String firstName, String lastName, Birthdate birthDate, Gender gender) {
        this.profilePictureUrl = profilePictureUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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

    public Birthdate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Birthdate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}