package dao;

import models.Abonnement;
import java.util.List;

public interface AbonnementDAO {
    void create(Abonnement abonnement);
    Abonnement findById(String id);
    List<Abonnement> findAll();
    void update(Abonnement abonnement);
    void delete(String id);
    List<Abonnement> findActiveSubscriptions();
    List<Abonnement> findByType(String type);
}
