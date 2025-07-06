package fr.esgi.doctodocapi.use_cases.patient.ports.out;

import java.util.Map;

public class NotificationMessage {
    private final String title;
    private final String body;
    private final Map<String, String> datas;

    public NotificationMessage(String title, String body, Map<String, String> datas) {
        this.title = title;
        this.body = body;
        this.datas = datas;
    }

    public NotificationMessage(String body, String title) {
        this.body = body;
        this.title = title;
        this.datas = null;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getDatas() {
        return datas;
    }
}
