    package fr.esgi.doctodocapi.model.doctor.payment.invoice;

    import java.math.BigDecimal;
    import java.util.Objects;
    import java.util.UUID;

    public class DoctorInvoice {
        private UUID id;
        private UUID subscription;
        private InvoiceState state;
        private BigDecimal amount;
        private String sessionId;

        public DoctorInvoice(UUID id, UUID subscription, InvoiceState state, BigDecimal amount, String sessionId) {
            this.id = id;
            this.subscription = subscription;
            this.state = state;
            this.amount = amount;
            this.sessionId = sessionId;
        }

        public static DoctorInvoice create(UUID subscriptionId, BigDecimal amount, String sessionId) {
            return new DoctorInvoice(UUID.randomUUID(), subscriptionId, InvoiceState.UNPAID, amount, sessionId);
        }

        public void markAsPaid() {
            this.state = InvoiceState.PAID;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UUID getSubscription() {
            return subscription;
        }

        public void setSubscription(UUID subscription) {
            this.subscription = subscription;
        }

        public InvoiceState getState() {
            return state;
        }

        public void setState(InvoiceState state) {
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

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            DoctorInvoice that = (DoctorInvoice) o;
            return Objects.equals(id, that.id);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }
