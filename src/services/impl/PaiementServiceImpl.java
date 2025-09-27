package services.impl;

import dao.PaiementDAO;
import dao.AbonnementDAO;
import dao.impl.PaiementDAOImpl;
import dao.impl.AbonnementDAOImpl;
import models.Paiement;
import models.Abonnement;
import services.PaiementService;
import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;
public class PaiementServiceImpl implements PaiementService {

    private final PaiementDAO paiementDAO;
    private final AbonnementDAO abonnementDAO;

    public PaiementServiceImpl() {
        this.paiementDAO = new PaiementDAOImpl();
        this.abonnementDAO = new AbonnementDAOImpl();
    }



    @Override
    public void createPaiement(Paiement paiement) {
        paiementDAO.create(paiement);
    }

    @Override
    public Paiement getPaiementById(String idPaiement) {
        return paiementDAO.findById(idPaiement);
    }

    @Override
    public List<Paiement> getPaiementsByAbonnement(String idAbonnement) {
        return paiementDAO.findByAbonnement(idAbonnement);
    }

    @Override
    public List<Paiement> getAllPaiements() {
        return paiementDAO.findAll();
    }

    @Override
    public void updatePaiement(Paiement paiement) {
        paiementDAO.update(paiement);
    }

    @Override
    public void deletePaiement(String idPaiement) {
        paiementDAO.delete(idPaiement);
    }

    @Override
    public List<Paiement> getUnpaidPaiements(String idAbonnement) {
        return paiementDAO.findUnpaidByAbonnement(idAbonnement);
    }

    @Override
    public List<Paiement> getLastPaiements(String idAbonnement, int limit) {
        return paiementDAO.findLastPayments(idAbonnement, limit);
    }

    @Override
    public BigDecimal getTotalPaidForAbonnement(String idAbonnement) {
        List<Paiement> paiements = paiementDAO.findByAbonnement(idAbonnement);
        long countPaid = paiements.stream()
                .filter(p -> p.getStatut() == Paiement.Statut.PAYE)
                .count();

        Abonnement abonnement = abonnementDAO.findById(idAbonnement);
        if (abonnement == null) {
            return BigDecimal.ZERO;
        }

        return abonnement.getMontantMensuel().multiply(BigDecimal.valueOf(countPaid));
    }


    @Override
    public void generateEcheancesForAbonnement(String idAbonnement, int monthsAhead) {
        // Assumption: one payment per month, amount = abonnement.montantMensuel
        Abonnement ab = abonnementDAO.findById(idAbonnement);
        if (ab == null) throw new RuntimeException("Abonnement introuvable: " + idAbonnement);

        java.time.LocalDate start = (ab.getDateDebut() != null ? ab.getDateDebut() : java.time.LocalDate.now());
        java.time.LocalDate base = java.time.LocalDate.now();
        // échéance = same day-of-month as start, for upcoming months
        java.util.stream.IntStream.rangeClosed(1, monthsAhead).forEach(m -> {
            java.time.LocalDate due = java.time.YearMonth.from(base).plusMonths(m)
                    .atDay(Math.min(start.getDayOfMonth(),
                            java.time.YearMonth.from(base).plusMonths(m).lengthOfMonth()));
            // create only if not already present for that due date
            boolean exists = paiementDAO.findByAbonnement(idAbonnement).stream()
                    .anyMatch(p -> due.equals(p.getDateEcheance()));
            if (!exists) {
                Paiement p = new Paiement(
                        idAbonnement,
                        due,           // dateEcheance
                        null,          // datePaiement (not paid yet)
                        "SYSTEM",      // type
                        Paiement.Statut.NON_PAYE
                );
                paiementDAO.create(p);
            }
        });
    }

    @Override
    public int markOverdues() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.util.List<Paiement> all = paiementDAO.findAll();
        int[] count = {0};
        all.stream()
                .filter(p -> p.getStatut() != Paiement.Statut.PAYE)
                .filter(p -> p.getDateEcheance() != null && p.getDateEcheance().isBefore(today))
                .forEach(p -> {
                    if (p.getStatut() != Paiement.Statut.EN_RETARD) {
                        p.setStatut(Paiement.Statut.EN_RETARD);
                        paiementDAO.update(p);
                        count[0]++;
                    }
                });
        return count[0];
    }

    @Override
    public java.util.Map<java.time.YearMonth, java.math.BigDecimal> monthlyTotalsAll() {
        java.util.List<Paiement> paid = paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == Paiement.Statut.PAYE && p.getDatePaiement() != null)
                .collect(java.util.stream.Collectors.toList());

        return paid.stream().collect(
                java.util.stream.Collectors.groupingBy(
                        p -> java.time.YearMonth.from(p.getDatePaiement()),
                        java.util.stream.Collectors.mapping(
                                p -> {
                                    Abonnement ab = abonnementDAO.findById(p.getIdAbonnement());
                                    return (ab == null ? java.math.BigDecimal.ZERO : ab.getMontantMensuel());
                                },
                                java.util.stream.Collectors.reducing(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                        )
                )
        );
    }

    @Override
    public java.util.Map<Integer, java.math.BigDecimal> annualTotalsAll() {
        java.util.List<Paiement> paid = paiementDAO.findAll().stream()
                .filter(p -> p.getStatut() == Paiement.Statut.PAYE && p.getDatePaiement() != null)
                .collect(java.util.stream.Collectors.toList());

        return paid.stream().collect(
                java.util.stream.Collectors.groupingBy(
                        p -> p.getDatePaiement().getYear(),
                        java.util.stream.Collectors.mapping(
                                p -> {
                                    Abonnement ab = abonnementDAO.findById(p.getIdAbonnement());
                                    return (ab == null ? java.math.BigDecimal.ZERO : ab.getMontantMensuel());
                                },
                                java.util.stream.Collectors.reducing(java.math.BigDecimal.ZERO, java.math.BigDecimal::add)
                        )
                )
        );
    }

    @Override
    public java.math.BigDecimal totalUnpaidForAbonnement(String idAbonnement) {
        java.util.List<Paiement> list = paiementDAO.findByAbonnement(idAbonnement);
        long unpaidCount = list.stream()
                .filter(p -> p.getStatut() != Paiement.Statut.PAYE)
                .count();
        Abonnement ab = abonnementDAO.findById(idAbonnement);
        return (ab == null) ? java.math.BigDecimal.ZERO
                : ab.getMontantMensuel().multiply(java.math.BigDecimal.valueOf(unpaidCount));
    }

    @Override
    public Map<String, Object> getUnpaidSummary(String idAbonnement) {
        List<Paiement> impayes = paiementDAO.findUnpaidByAbonnement(idAbonnement);

        // total impayé = montantMensuel * nombre d'impayés
        BigDecimal total = impayes.stream()
                .map(p -> {
                    Abonnement ab = abonnementDAO.findById(idAbonnement);
                    return (ab == null) ? BigDecimal.ZERO : ab.getMontantMensuel();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> result = new HashMap<>();
        result.put("impayes", impayes);
        result.put("total", total);
        return result;
    }


}
