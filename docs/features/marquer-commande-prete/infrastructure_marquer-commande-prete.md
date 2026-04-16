# Marquer une commande comme prête : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la feature « Marquer une commande comme prête ». L'infrastructure lit et met à jour l'état de préparation sans incohérence.

**Critères d'acceptation**
Feature: Marquer une commande comme prête - Infrastructure

Scenario: Exécuter correctement les opérations techniques en cas nominal
Given les adaptateurs techniques nécessaires sont disponibles
When l'opération technique de la feature est exécutée
Then les données sont persistées ou lues correctement
And les ports attendus sont satisfaits

Scenario: Préserver la cohérence en cas d'échec technique ou métier
Given une exécution où la règle stock préparé insuffisant conduit à un échec
When l'adaptateur finalise l'opération
Then aucun état partiel incohérent n'est persisté
And une erreur exploitable est remontée vers la couche supérieure

**Notes**
- Garantir la cohérence transactionnelle sur les écritures critiques.
