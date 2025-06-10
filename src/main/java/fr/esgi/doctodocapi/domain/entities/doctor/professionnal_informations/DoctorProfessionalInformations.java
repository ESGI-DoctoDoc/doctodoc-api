package fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations;

import fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.vo.experience_year.ExperienceYear;
import fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.vo.rpps.Rpps;

import java.util.List;

public class DoctorProfessionalInformations {
    private Rpps rpps;
    private String bio;
    private String speciality;
    private ExperienceYear experienceYears;
    private List<String> languages;
    private List<String> doctorDocuments;
    private boolean acceptPublicCoverage;

    public DoctorProfessionalInformations(String rppsValue, String bio, String speciality, Short experienceYearsValue, List<String> languages, List<String> doctorDocuments, boolean acceptPublicCoverage) {
        this.rpps = Rpps.of(rppsValue);
        this.bio = bio;
        this.speciality = speciality;
        this.experienceYears = ExperienceYear.of(experienceYearsValue);
        this.languages = languages;
        this.doctorDocuments = doctorDocuments;
        this.acceptPublicCoverage = acceptPublicCoverage;
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

    public ExperienceYear getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(ExperienceYear experienceYears) {
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

    public boolean isAcceptPublicCoverage() {
        return acceptPublicCoverage;
    }

    public void setAcceptPublicCoverage(boolean acceptPublicCoverage) {
        this.acceptPublicCoverage = acceptPublicCoverage;
    }
}