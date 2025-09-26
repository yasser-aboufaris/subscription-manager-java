import java.util.Scanner;

/**
 * Menu console minimal (FR) — actions intentionally EMPTY for you to implement.
 * Put this file in src/Main.java (no package).
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        afficherEntete();
        while (true) {
            afficherMenu();
            String choix = lire("Choix");
            if (choix == null) continue;
            choix = choix.trim();
            try {
                switch (choix) {
                    case "1": actionCreerAbonnement(); break;
                    case "2": actionModifierAbonnement(); break;
                    case "3": actionResilierAbonnement(); break;
                    case "4": actionListerAbonnements(); break;
                    case "5": actionVoirPaiementsAbonnement(); break;
                    case "6": actionEnregistrerPaiement(); break;
                    case "7": actionDetecterImpayes(); break;
                    case "8": actionSommePayee(); break;
                    case "9": action5DerniersPaiements(); break;
                    case "10": actionRapportMensuel(); break;
                    case "0":
                        scanner.close();
                        return;
                    default:
                        // nothing
                }
            } catch (Exception e) {
                // nothing
            }
            // continue loop
        }
    }

    private static void afficherEntete() {
        System.out.println("=============================================");
        System.out.println("      Subscription Manager - Menu (MVP)      ");
        System.out.println("=============================================");
    }

    private static void afficherMenu() {
        System.out.println();
        System.out.println("Menu principal:");
        System.out.println("1  - Créer un abonnement");
        System.out.println("2  - Modifier un abonnement");
        System.out.println("3  - Résilier un abonnement");
        System.out.println("4  - Lister les abonnements");
        System.out.println("5  - Voir les paiements d'un abonnement");
        System.out.println("6  - Enregistrer un paiement");
        System.out.println("7  - Détecter impayés / en retard");
        System.out.println("8  - Afficher la somme payée d'un abonnement");
        System.out.println("9  - Afficher les 5 derniers paiements (globaux)");
        System.out.println("10 - Générer rapport mensuel (payé vs impayé)");
        System.out.println("0  - Quitter");
    }

    // --------- Actions (EMPTY - implement controllers here) ---------

    private static void actionCreerAbonnement() {
        // intentionally empty
    }

    private static void actionModifierAbonnement() {
        // intentionally empty
    }

    private static void actionResilierAbonnement() {
        // intentionally empty
    }

    private static void actionListerAbonnements() {
        // intentionally empty
    }

    private static void actionVoirPaiementsAbonnement() {
        // intentionally empty
    }

    private static void actionEnregistrerPaiement() {
        // intentionally empty
    }

    private static void actionDetecterImpayes() {
        // intentionally empty
    }

    private static void actionSommePayee() {
        // intentionally empty
    }

    private static void action5DerniersPaiements() {
        // intentionally empty
    }

    private static void actionRapportMensuel() {
        // intentionally empty
    }

    // --------- Utilitaires console ---------

    private static String lire(String prompt) {
        System.out.print(prompt + ": ");
        try {
            String line = scanner.nextLine();
            if (line == null) return "";
            return line.trim();
        } catch (Exception e) {
            return "";
        }
    }
}
