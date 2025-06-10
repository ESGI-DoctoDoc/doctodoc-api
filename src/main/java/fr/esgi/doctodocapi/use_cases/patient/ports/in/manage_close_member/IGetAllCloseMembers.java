package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member;

import fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;

import java.util.List;

public interface IGetAllCloseMembers {
    List<GetCloseMemberResponse> getCloseMembers();
}
