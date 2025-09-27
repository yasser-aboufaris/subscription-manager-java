package models;

import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;

public abstract class Abonnement {

    public enum Statut {
        ACTIVE,
        SUSPENDU,
        RESILIE
    }

    protected String id;
    protected String nomService;
    protected BigDecimal montantMensuel;
    protected LocalDate dateDebut;
    protected LocalDate dateFin;
    protected Statut statut;

    public Abonnement(String nomService, BigDecimal montantMensuel, LocalDate dateDebut, LocalDate dateFin, Statut statut) {
        this.id = UUID.randomUUID().toString(); // Génération automatique de l’UUID
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }
    public Abonnement(String id, String nomService, BigDecimal montantMensuel,
                      LocalDate dateDebut, LocalDate dateFin, Statut statut) {
        this.id = id;
        this.nomService = nomService;
        this.montantMensuel = montantMensuel;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public String getNomService() {
        return nomService;
    }

    public void setNomService(String nomService) {
        this.nomService = nomService;
    }

    public BigDecimal getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(BigDecimal montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "Abonnement {" +
                "id='" + id + '\'' +
                ", nomService='" + nomService + '\'' +
                ", montantMensuel=" + montantMensuel +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut=" + statut +
                '}';
    }
}
