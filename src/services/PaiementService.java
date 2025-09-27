package services;

import models.Paiement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PaiementService {
    void createPaiement(Paiement paiement);
    Paiement getPaiementById(String idPaiement);
    List<Paiement> getPaiementsByAbonnement(String idAbonnement);
    List<Paiement> getAllPaiements();
    void updatePaiement(Paiement paiement);
    void deletePaiement(String idPaiement);
    List<Paiement> getUnpaidPaiements(String idAbonnement);
    List<Paiement> getLastPaiements(String idAbonnement, int limit);
    void generateEcheancesForAbonnement(String idAbonnement, int monthsAhead);
    int markOverdues();

    // --- reports ---
    java.util.Map<java.time.YearMonth, java.math.BigDecimal> monthlyTotalsAll(); // paid totals by month
    java.util.Map<Integer, java.math.BigDecimal> annualTotalsAll();              // paid totals by year
    java.math.BigDecimal totalUnpaidForAbonnement(String idAbonnement);
    Map<String, Object> getUnpaidSummary(String idAbonnement);
// sum of unpaid for one abonnement


    // ✅ extra business method
    BigDecimal getTotalPaidForAbonnement(String idAbonnement);
}
