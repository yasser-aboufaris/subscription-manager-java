# Gestion des Abonnements

Application console en **Java 8** pour gérer des abonnements (avec ou sans engagement) et leurs paiements.

## Fonctionnalités
- CRUD abonnements et paiements
- Suspendre, résilier, reprendre un abonnement
- Voir paiements d’un abonnement / derniers paiements
- Total payé et impayés
- Rapports financiers (mensuels, annuels, impayés)

## Technologies
- Java 8
- JDBC (PostgreSQL/MySQL)
- Stream API, Lambda, Collectors

## Exécution
```bash
javac -cp lib/postgresql-42.7.3.jar -d out $(find src -name "*.java")
java -cp "out:lib/postgresql-42.7.3.jar" main.Main
