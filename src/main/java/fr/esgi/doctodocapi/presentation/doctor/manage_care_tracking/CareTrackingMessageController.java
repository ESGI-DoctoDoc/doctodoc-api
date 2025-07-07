package fr.esgi.doctodocapi.presentation.doctor.manage_care_tracking;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.manage_care_tracking.message.SendMessageRequest;
import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.care_tracking_response.messaga.CareTrackingMessageResponse;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.IGetCareTrackingMessages;
import fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_care_tracking.message.ISendCareTrackingMessage;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("doctors")
public class CareTrackingMessageController {
    private final ISendCareTrackingMessage sendCareTrackingMessage;
    private final IGetCareTrackingMessages getCareTrackingMessages;

    public CareTrackingMessageController(ISendCareTrackingMessage sendCareTrackingMessage, IGetCareTrackingMessages getCareTrackingMessages) {
        this.sendCareTrackingMessage = sendCareTrackingMessage;
        this.getCareTrackingMessages = getCareTrackingMessages;
    }

    @PostMapping("care-tracking/{id}/message")
    public CareTrackingMessageResponse sendMessage(@PathVariable UUID id, @Valid @RequestBody SendMessageRequest request) {
        return this.sendCareTrackingMessage.execute(id, request);
    }

    @GetMapping("/{id}/messages")
    public List<CareTrackingMessageResponse> getMessages(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cursorSentAt,
            @RequestParam(required = false) UUID cursorId
    ) {
        return getCareTrackingMessages.execute(id, cursorSentAt, cursorId);
    }
}
