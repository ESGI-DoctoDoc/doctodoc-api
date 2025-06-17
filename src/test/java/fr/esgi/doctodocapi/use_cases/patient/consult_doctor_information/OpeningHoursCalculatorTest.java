package fr.esgi.doctodocapi.use_cases.patient.consult_doctor_information;

import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.doctor_detail_reponse.OpeningHoursResponse;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OpeningHoursCalculatorTest {
    private final OpeningHoursCalculator service = new OpeningHoursCalculator();

    @Test
    void should_return_opening_hours_for_each_day_of_week() {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate tuesday = monday.plusDays(1);
        LocalDate thursday = monday.plusDays(3);

        Slot mondaySlot1 = Slot.create(monday, LocalTime.of(9, 0), LocalTime.of(12, 0), List.of());
        Slot mondaySlot2 = Slot.create(monday, LocalTime.of(14, 0), LocalTime.of(18, 0), List.of());
        Slot tuesdaySlot = Slot.create(tuesday, LocalTime.of(10, 0), LocalTime.of(16, 0), List.of());
        Slot thursdaySlot = Slot.create(thursday, LocalTime.of(8, 30), LocalTime.of(11, 45), List.of());

        List<Slot> slots = List.of(mondaySlot1, mondaySlot2, tuesdaySlot, thursdaySlot);

        List<OpeningHoursResponse> responses = service.getOpeningHoursOfCurrentWeek(slots);

        assertThat(responses).hasSize(7);

        // Lundi
        OpeningHoursResponse mondayResponse = responses.getFirst();
        assertThat(mondayResponse.day()).isEqualTo("lundi");
        assertThat(mondayResponse.start()).isEqualTo(LocalTime.of(9, 0));
        assertThat(mondayResponse.end()).isEqualTo(LocalTime.of(18, 0));

        // Mardi
        OpeningHoursResponse tuesdayResponse = responses.get(1);
        assertThat(tuesdayResponse.day()).isEqualTo("mardi");
        assertThat(tuesdayResponse.start()).isEqualTo(LocalTime.of(10, 0));
        assertThat(tuesdayResponse.end()).isEqualTo(LocalTime.of(16, 0));

        // Mercredi (aucun slot)
        OpeningHoursResponse wednesdayResponse = responses.get(2);
        assertThat(wednesdayResponse.day()).isEqualTo("mercredi");
        assertThat(wednesdayResponse.start()).isNull();
        assertThat(wednesdayResponse.end()).isNull();

        // Jeudi
        OpeningHoursResponse thursdayResponse = responses.get(3);
        assertThat(thursdayResponse.day()).isEqualTo("jeudi");
        assertThat(thursdayResponse.start()).isEqualTo(LocalTime.of(8, 30));
        assertThat(thursdayResponse.end()).isEqualTo(LocalTime.of(11, 45));

        // Vendredi à dimanche : fermés
        for (int i = 4; i < 7; i++) {
            assertThat(responses.get(i).start()).isNull();
            assertThat(responses.get(i).end()).isNull();
        }
    }

}