package dao;

import models.Paiement;
import java.util.List;

public interface PaiementDAO {
    void create(Paiement paiement);
    Paiement findById(String idPaiement);
    List<Paiement> findByAbonnement(String idAbonnement);
    List<Paiement> findAll();
    void update(Paiement paiement);
    void delete(String idPaiement);
    List<Paiement> findUnpaidByAbonnement(String idAbonnement);
    List<Paiement> findLastPayments(String idAbonnement, int limit);
}
