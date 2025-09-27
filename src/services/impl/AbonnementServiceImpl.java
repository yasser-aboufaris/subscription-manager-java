package services.impl;

import dao.AbonnementDAO;
import dao.impl.AbonnementDAOImpl;
import models.Abonnement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AbonnementServiceImpl implements services.AbonnementService {

    private final AbonnementDAO abonnementDAO;

    public AbonnementServiceImpl() {
        this.abonnementDAO = new AbonnementDAOImpl();
    }

    @Override
    public void createAbonnement(Abonnement abonnement) {
        abonnementDAO.create(abonnement);
    }

    @Override
    public Abonnement getAbonnementById(String id) {
        return abonnementDAO.findById(id);
    }

    @Override
    public List<Abonnement> getAllAbonnements() {
        return abonnementDAO.findAll().stream()
                .sorted(Comparator.comparing(Abonnement::getMontantMensuel))
                .collect(Collectors.toList());
    }

    @Override
    public void updateAbonnement(Abonnement abonnement) {
        abonnementDAO.update(abonnement); // full edit still exists
    }

    @Override
    public void deleteAbonnement(String id) {
        abonnementDAO.delete(id);
    }

    // 🔥 new: generic setter
    @Override
    public void setStatut(String id, Abonnement.Statut statut) {
        Abonnement abonnement = abonnementDAO.findById(id);
        if (abonnement != null) {
            abonnement.setStatut(statut);
            abonnementDAO.update(abonnement);
        }
    }

    @Override
    public void suspendAbonnement(String id) {
        setStatut(id, Abonnement.Statut.SUSPENDU);
    }

    @Override
    public void resumeAbonnement(String id) {
        setStatut(id, Abonnement.Statut.ACTIVE);
    }

    @Override
    public void resiliateAbonnement(String id) {
        setStatut(id, Abonnement.Statut.RESILIE);
    }

    @Override
    public List<Abonnement> getActiveAbonnements() {
        return abonnementDAO.findAll().stream()
                .filter(a -> a.getStatut() == Abonnement.Statut.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Abonnement> getAbonnementsByType(String type) {
        return abonnementDAO.findAll().stream()
                .filter(a -> a.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }
}
