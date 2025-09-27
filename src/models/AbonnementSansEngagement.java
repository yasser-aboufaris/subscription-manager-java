package models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbonnementSansEngagement extends Abonnement {

    // ✅ Constructor for new objects (id auto-generated in base class)
    public AbonnementSansEngagement(String nomService, BigDecimal montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    Statut statut) {
        super(nomService, montantMensuel, dateDebut, dateFin, statut);
    }

    // ✅ Constructor for objects coming from DB (id provided)
    public AbonnementSansEngagement(String id, String nomService, BigDecimal montantMensuel,
                                    LocalDate dateDebut, LocalDate dateFin,
                                    Statut statut) {
        super(id, nomService, montantMensuel, dateDebut, dateFin, statut);
    }

    @Override
    public String getType() {
        return "SANS_ENGAGEMENT";
    }

    @Override
    public String toString() {
        return "AbonnementSansEngagement{" +
                super.toString() +
                "}";
    }
}
