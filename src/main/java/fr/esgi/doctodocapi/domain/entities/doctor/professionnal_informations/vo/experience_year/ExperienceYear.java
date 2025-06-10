package fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.vo.experience_year;

public class ExperienceYear {
    private final Short value;

    private ExperienceYear(Short value) {
        this.value = value;
    }

    public static ExperienceYear of(Short value) {
        valid(value);
        return new ExperienceYear(value);
    }

    private static void valid(Short value) {
        if(value < 0) {
            throw new InvalidExperienceYearException();
        }
    }

    public Short getValue() {
        return value;
    }
}
