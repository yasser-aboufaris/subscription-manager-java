package models;

import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    public AbonnementSansEngagement(String nomService, double montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    Statut statut) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
    }

    @Override
    public String getType() {
        return "Sans Engagement";
    }

    @Override
    public String toString() {
        return super.toString() + " (Sans Engagement)";
    }
}
