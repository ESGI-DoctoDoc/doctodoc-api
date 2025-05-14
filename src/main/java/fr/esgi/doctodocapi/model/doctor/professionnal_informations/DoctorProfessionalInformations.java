package fr.esgi.doctodocapi.model.doctor.professionnal_informations;

import java.util.List;

public class DoctorProfessionalInformations {
    private Rpps rpps;
    private String bio;
    private String speciality;
    private Short experienceYears;
    private List<String> languages;
    private List<String> doctorDocuments;

    public DoctorProfessionalInformations(String rppsValue, String bio, String speciality, Short experienceYears, List<String> languages, List<String> doctorDocuments) {
        this.rpps = Rpps.of(rppsValue);
        this.bio = bio;
        this.speciality = speciality;
        this.experienceYears = experienceYears;
        this.languages = languages;
        this.doctorDocuments = doctorDocuments;
    }

    public Rpps getRpps() {
        return rpps;
    }

    public void setRpps(Rpps rpps) {
        this.rpps = rpps;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Short getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Short experienceYears) {
        this.experienceYears = experienceYears;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getDoctorDocuments() {
        return doctorDocuments;
    }

    public void setDoctorDocuments(List<String> doctorDocuments) {
        this.doctorDocuments = doctorDocuments;
    }
}