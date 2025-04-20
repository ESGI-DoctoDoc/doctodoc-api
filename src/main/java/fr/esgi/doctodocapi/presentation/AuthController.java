package fr.esgi.doctodocapi.presentation;


import fr.esgi.doctodocapi.use_cases.patient.ListAllPatients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final ListAllPatients listAllPatients;

    public AuthController(ListAllPatients listAllPatients) {
        this.listAllPatients = listAllPatients;
    }

    @GetMapping("up")
    public String up() {
        return "up";
    }

    @GetMapping("lists")
    public String lists() {
        return this.listAllPatients.findAll().toString();
    }

}
