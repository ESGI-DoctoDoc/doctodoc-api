package fr.esgi.doctodocapi.domain.entities.doctor.consultation_informations.medical_concern.question;

public enum QuestionType {
    LIST("list"),
    DATE("date"),
    YES_NO("yes_no"),
    TEXT("text");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public static QuestionType fromValue(String value) {
        for (QuestionType type : QuestionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new QuestionTypeNotFound();
    }


    public String getValue() {
        return value;
    }
}
