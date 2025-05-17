package fr.esgi.doctodocapi.model.doctor.consultation_informations;

import fr.esgi.doctodocapi.model.doctor.personal_information.CoordinatesGps;

import java.math.BigDecimal;
import java.util.List;

public class DoctorConsultationInformations {
    private BigDecimal price;
    private String address;
    private CoordinatesGps coordinatesGps;
    private List<String> medicalConcerns;

    public DoctorConsultationInformations(BigDecimal price, String address, CoordinatesGps coordinatesGps, List<String> medicalConcerns) {
        this.price = price;
        this.address = address;
        this.coordinatesGps = coordinatesGps;
        this.medicalConcerns = medicalConcerns;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CoordinatesGps getCoordinatesGps() {
        return coordinatesGps;
    }

    public void setCoordinatesGps(CoordinatesGps coordinatesGps) {
        this.coordinatesGps = coordinatesGps;
    }

    public List<String> getMedicalConcerns() {
        return medicalConcerns;
    }

    public void setMedicalConcerns(List<String> medicalConcerns) {
        this.medicalConcerns = medicalConcerns;
    }
}