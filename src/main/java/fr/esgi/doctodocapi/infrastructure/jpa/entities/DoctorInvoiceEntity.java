package fr.esgi.doctodocapi.infrastructure.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "doctor_invoices")
public class DoctorInvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "invoice_id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private DoctorSubscriptionEntity subscription;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DoctorSubscriptionEntity getSubscription() {
        return subscription;
    }

    public void setSubscription(DoctorSubscriptionEntity subscription) {
        this.subscription = subscription;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
