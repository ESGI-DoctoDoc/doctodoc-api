package fr.esgi.doctodocapi.model.doctor.professionnal_informations;

import fr.esgi.doctodocapi.model.admin.speciality.Speciality;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.vo.experience_year.ExperienceYear;
import fr.esgi.doctodocapi.model.doctor.professionnal_informations.vo.rpps.Rpps;
import fr.esgi.doctodocapi.model.document.Document;

import java.util.List;
import java.util.UUID;

public class DoctorProfessionalInformations {
    private Rpps rpps;
    private String bio;
    private Speciality speciality;
    private ExperienceYear experienceYears;
    private List<String> languages;
    private List<Document> doctorDocuments;
    private boolean acceptPublicCoverage;

    public DoctorProfessionalInformations(String rppsValue, String bio, Speciality speciality, Short experienceYearsValue, List<String> languages, List<Document> doctorDocuments, boolean acceptPublicCoverage) {
        this.rpps = Rpps.of(rppsValue);
        this.bio = bio;
        this.speciality = speciality;
        this.experienceYears = ExperienceYear.of(experienceYearsValue);
        this.languages = languages;
        this.doctorDocuments = doctorDocuments;
        this.acceptPublicCoverage = acceptPublicCoverage;
    }

    public void addDocument(Document document) {
        if (doctorDocuments.stream().anyMatch(d -> d.getName().equalsIgnoreCase(document.getName()))) {
            throw new DocumentAlreadyExistInDoctorException();
        }
        doctorDocuments.add(document);
    }

    public Document getDocumentById(UUID id) {
        return this.doctorDocuments
                .stream()
                .filter(document -> document.getId().equals(id))
                .findFirst()
                .orElseThrow(DocumentNotFoundInDoctorOnboardingException::new);
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

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
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

    public List<Document> getDoctorDocuments() {
        return doctorDocuments;
    }

    public void setDoctorDocuments(List<Document> doctorDocuments) {
        this.doctorDocuments = doctorDocuments;
    }

    public boolean isAcceptPublicCoverage() {
        return acceptPublicCoverage;
    }

    public void setAcceptPublicCoverage(boolean acceptPublicCoverage) {
        this.acceptPublicCoverage = acceptPublicCoverage;
    }
}