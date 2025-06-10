package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.SaveCloseMemberRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment.GetCloseMemberResponse;

public interface ICreateCloseMember {
    GetCloseMemberResponse process(SaveCloseMemberRequest saveCloseMemberRequest);
}
