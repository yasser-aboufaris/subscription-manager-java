package services;

import models.Abonnement;
import java.util.List;

public interface AbonnementService {

    void createAbonnement(Abonnement abonnement);

    Abonnement getAbonnementById(String id);

    List<Abonnement> getAllAbonnements();

    void updateAbonnement(Abonnement abonnement); // full edit if needed

    void deleteAbonnement(String id);

    // 🔥 new status-specific methods
    void setStatut(String id, Abonnement.Statut statut);

    void suspendAbonnement(String id);

    void resumeAbonnement(String id);

    void resiliateAbonnement(String id);

    List<Abonnement> getActiveAbonnements();

    List<Abonnement> getAbonnementsByType(String type);
}
