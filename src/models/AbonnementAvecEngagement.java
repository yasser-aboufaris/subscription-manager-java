package models;

import java.time.LocalDate;

public class AbonnementAvecEngagement extends Abonnement {

    private int dureeEngagementMois;

    public AbonnementAvecEngagement(String nomService, double montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    Statut statut, int dureeEngagementMois) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
        this.dureeEngagementMois = dureeEngagementMois;
    }

    public int getDureeEngagementMois() {
        return dureeEngagementMois;
    }

    public void setDureeEngagementMois(int dureeEngagementMois) {
        this.dureeEngagementMois = dureeEngagementMois;
    }

    @Override
    public String getType() {
        return "Avec Engagement";
    }

    @Override
    public String toString() {
        return super.toString() +
                ", dureeEngagementMois=" + dureeEngagementMois +
                '}';
    }
}
