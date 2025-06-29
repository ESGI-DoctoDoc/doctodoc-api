package fr.esgi.doctodocapi.use_cases.admin.ports.out;

public interface InvoiceFetcher {
    String getInvoiceUrl(String sessionId);
}
