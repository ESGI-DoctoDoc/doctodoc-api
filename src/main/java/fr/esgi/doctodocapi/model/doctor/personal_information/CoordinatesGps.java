package fr.esgi.doctodocapi.model.doctor.personal_information;

import java.math.BigDecimal;

public class CoordinatesGps {
    private BigDecimal clinicLatitude;
    private BigDecimal clinicLongitude;

    private CoordinatesGps(BigDecimal clinicLatitude, BigDecimal clinicLongitude) {
        this.clinicLatitude = clinicLatitude;
        this.clinicLongitude = clinicLongitude;
    }

    public static CoordinatesGps of(BigDecimal clinicLatitude, BigDecimal clinicLongitude) {
        return new CoordinatesGps(clinicLatitude, clinicLongitude);
    }

    public BigDecimal getClinicLatitude() {
        return clinicLatitude;
    }

    public void setClinicLatitude(BigDecimal clinicLatitude) {
        this.clinicLatitude = clinicLatitude;
    }

    public BigDecimal getClinicLongitude() {
        return clinicLongitude;
    }

    public void setClinicLongitude(BigDecimal clinicLongitude) {
        this.clinicLongitude = clinicLongitude;
    }
}
