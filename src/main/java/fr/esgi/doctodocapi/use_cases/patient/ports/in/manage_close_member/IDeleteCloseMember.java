package fr.esgi.doctodocapi.use_cases.patient.ports.in.manage_close_member;

import java.util.UUID;

public interface IDeleteCloseMember {
    void process(UUID closeMemberId);
}
