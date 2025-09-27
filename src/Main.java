package main;

import models.*;
import services.AbonnementService;
import services.PaiementService;
import services.impl.AbonnementServiceImpl;
import services.impl.PaiementServiceImpl;
import java.util.Map;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        AbonnementService abonnementService = new AbonnementServiceImpl();
        PaiementService paiementService = new PaiementServiceImpl();

        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gérer les abonnements");
            System.out.println("2. Gérer les paiements");
            System.out.println("0. Quitter");

            System.out.print("Choix : ");
            int choix = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choix) {
                case 1:
                    abonnementMenu(sc, abonnementService);
                    break;
                case 2:
                    paiementMenu(sc,abonnementService,paiementService);
                    break;
                case 0:
                    running = false;
                    System.out.println("👋 Au revoir !");
                    break;
                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
        sc.close();
    }

    private static void abonnementMenu(Scanner sc, AbonnementService abonnementService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU ABONNEMENTS ===");
            System.out.println("1. Créer un abonnement");
            System.out.println("2. Modifier nom d’un abonnement");
            System.out.println("3. Supprimer un abonnement");
            System.out.println("4. Consulter la liste des abonnements");
            System.out.println("5. Lister abonnements actifs");
            System.out.println("6. Suspendre un abonnement");
            System.out.println("7. Reprendre un abonnement");
            System.out.println("8. Résilier un abonnement");
            System.out.println("0. Retour");

            System.out.print("Choix : ");
            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1: // Créer
                    System.out.print("Nom du service : ");
                    String nomService = sc.nextLine();

                    System.out.print("Montant mensuel : ");
                    BigDecimal montant = sc.nextBigDecimal();
                    sc.nextLine();

                    System.out.print("Avec engagement ? (y/n): ");
                    String engagement = sc.nextLine();

                    // ✅ Choisir statut avec un numéro
                    System.out.println("Choisir le statut initial :");
                    System.out.println("1. ACTIVE");
                    System.out.println("2. SUSPENDU");
                    System.out.println("3. RESILIE");
                    System.out.print("Votre choix : ");
                    int statutChoice = sc.nextInt();
                    sc.nextLine();

                    Abonnement.Statut statut;
                    switch (statutChoice) {
                        case 2:
                            statut = Abonnement.Statut.SUSPENDU;
                            break;
                        case 3:
                            statut = Abonnement.Statut.RESILIE;
                            break;
                        default:
                            statut = Abonnement.Statut.ACTIVE;
                            break;
                    }

                    Abonnement abonnement;
                    if (engagement.equalsIgnoreCase("y")) {
                        System.out.print("Durée engagement (mois) : ");
                        int duree = sc.nextInt();
                        sc.nextLine();
                        abonnement = new AbonnementAvecEngagement(
                                nomService, montant,
                                LocalDate.now(),
                                LocalDate.now().plusMonths(duree),
                                statut, duree
                        );
                    } else {
                        abonnement = new AbonnementSansEngagement(
                                nomService, montant,
                                LocalDate.now(), null,
                                statut
                        );
                    }
                    abonnementService.createAbonnement(abonnement);
                    System.out.println("✅ Abonnement créé !");
                    break;

                case 2: // Modifier nom
                    System.out.print("ID de l’abonnement à modifier : ");
                    String idUpdate = sc.nextLine();
                    Abonnement toUpdate = abonnementService.getAbonnementById(idUpdate);
                    if (toUpdate != null) {
                        System.out.print("Nouveau nom du service : ");
                        String newName = sc.nextLine();
                        toUpdate.setNomService(newName);
                        abonnementService.updateAbonnement(toUpdate);
                        System.out.println("✅ Abonnement mis à jour !");
                    } else {
                        System.out.println("❌ Abonnement introuvable !");
                    }
                    break;

                case 3: // Supprimer
                    System.out.print("ID de l’abonnement à supprimer : ");
                    String idDelete = sc.nextLine();
                    abonnementService.deleteAbonnement(idDelete);
                    System.out.println("✅ Abonnement supprimé !");
                    break;

                case 4: // Lister
                    List<Abonnement> all = abonnementService.getAllAbonnements();
                    if (all.isEmpty()) {
                        System.out.println("⚠️ Aucun abonnement trouvé.");
                    } else {
                        all.forEach(System.out::println);
                    }
                    break;

                case 5: // Actifs
                    List<Abonnement> actifs = abonnementService.getActiveAbonnements();
                    if (actifs.isEmpty()) {
                        System.out.println("⚠️ Aucun abonnement actif.");
                    } else {
                        actifs.forEach(System.out::println);
                    }
                    break;

                case 6: // Suspendre
                    System.out.print("ID de l’abonnement à suspendre : ");
                    String idSusp = sc.nextLine();
                    abonnementService.suspendAbonnement(idSusp);
                    System.out.println("✅ Abonnement suspendu !");
                    break;

                case 7: // Reprendre
                    System.out.print("ID de l’abonnement à reprendre : ");
                    String idResume = sc.nextLine();
                    abonnementService.resumeAbonnement(idResume);
                    System.out.println("✅ Abonnement repris !");
                    break;

                case 8: // Résilier
                    System.out.print("ID de l’abonnement à résilier : ");
                    String idResil = sc.nextLine();
                    abonnementService.resiliateAbonnement(idResil);
                    System.out.println("✅ Abonnement résilié !");
                    break;

                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
    }

    private static void paiementMenu(Scanner sc, AbonnementService abonnementService ,PaiementService paiementService) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU PAIEMENTS ===");
            System.out.println("1. Enregistrer un paiement");
            System.out.println("2. Modifier un paiement");
            System.out.println("3. Supprimer un paiement");
            System.out.println("4. Voir les paiements d’un abonnement");
            System.out.println("5. Voir les 5 derniers paiements");
            System.out.println("6. Montant total payé pour un abonnement");
            System.out.println("7. Voir les paiements manqués + total impayé");
            System.out.println("8. Rapport financier mensuel");
            System.out.println("9. Rapport financier annuel");
            System.out.println("10. Rapport des impayés");
            System.out.println("0. Retour");

            System.out.print("Choix : ");
            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1: // Créer paiement
                    // ✅ already working
                    System.out.print("ID de l’abonnement : ");
                    String aboId = sc.nextLine();
                    System.out.print("Type de paiement : ");
                    String type = sc.nextLine();
                    Paiement paiement = new Paiement(
                            aboId,
                            LocalDate.now().plusDays(7),
                            LocalDate.now(),
                            type,
                            Paiement.Statut.PAYE
                    );
                    paiementService.createPaiement(paiement);
                    System.out.println("✅ Paiement enregistré !");
                    break;

                case 2: // Modifier
                    // ✅ already working
                    System.out.print("ID du paiement à modifier : ");
                    String idPaiementUpdate = sc.nextLine();
                    Paiement toUpd = paiementService.getPaiementById(idPaiementUpdate);
                    if (toUpd != null) {
                        System.out.print("Nouveau type de paiement : ");
                        String newType = sc.nextLine();
                        toUpd.setTypePaiement(newType);
                        paiementService.updatePaiement(toUpd);
                        System.out.println("✅ Paiement mis à jour !");
                    } else {
                        System.out.println("❌ Paiement introuvable !");
                    }
                    break;

                case 3: // Supprimer
                    // ✅ already working
                    System.out.print("ID du paiement à supprimer : ");
                    String idPaiementDelete = sc.nextLine();
                    paiementService.deletePaiement(idPaiementDelete);
                    System.out.println("✅ Paiement supprimé !");
                    break;

                case 4: // Voir paiements
                    // ✅ already working
                    System.out.print("ID de l’abonnement : ");
                    String idAbo = sc.nextLine();
                    List<Paiement> paiements = paiementService.getPaiementsByAbonnement(idAbo);
                    paiements.forEach(System.out::println);
                    break;

                case 5: // 5 derniers
                    // ✅ already working
                    System.out.print("ID de l’abonnement : ");
                    String idForLast = sc.nextLine();
                    paiementService.getLastPaiements(idForLast, 5).forEach(System.out::println);
                    break;

                case 6: // Total payé
                    // ✅ already working
                    System.out.print("ID de l’abonnement : ");
                    String idForTotal = sc.nextLine();
                    System.out.println("💰 Total payé = " +
                            paiementService.getTotalPaidForAbonnement(idForTotal) + " MAD");
                    break;

                case 7:
                    System.out.print("ID de l’abonnement : ");
                    String idUnpaid = sc.nextLine();
                    Map<String, Object> summary = paiementService.getUnpaidSummary(idUnpaid);

                    List<Paiement> impayes = (List<Paiement>) summary.get("impayes");
                    BigDecimal total = (BigDecimal) summary.get("total");

                    if (impayes.isEmpty()) {
                        System.out.println("✅ Aucun paiement manqué.");
                    } else {
                        impayes.forEach(System.out::println);
                        System.out.println("💰 Total impayé = " + total + " MAD");
                    }
                    break;


                case 8: // Rapport financier mensuel
                    System.out.println("\n=== Rapport Financier Mensuel ===");
                    paiementService.monthlyTotalsAll()
                            .forEach((mois, somme) ->
                                    System.out.println(mois + " : " + somme + " MAD"));
                    break;

                case 9: // Rapport annuel
                    System.out.println("\n=== Rapport Financier Annuel ===");
                    paiementService.annualTotalsAll()
                            .forEach((annee, somme) ->
                                    System.out.println(annee + " : " + somme + " MAD"));
                    break;


                case 10: // Rapport des impayés
                    System.out.println("\n=== Rapport des Impayés ===");
                    abonnementService.getAllAbonnements().forEach(ab -> {
                        BigDecimal impaye = paiementService.totalUnpaidForAbonnement(ab.getId());
                        if (impaye.compareTo(BigDecimal.ZERO) > 0) {
                            System.out.println("❌ " + ab.getNomService() + " : " + impaye + " MAD impayés");
                        }
                    });
                    break;


                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("❌ Choix invalide !");
            }
        }
    }

}
