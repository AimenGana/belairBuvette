# Acquitter une commande et estimer le temps de préparation : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la feature « Acquitter une commande et estimer le temps de préparation ». L'infrastructure fournit les données nécessaires au calcul de charge et de types.

**Critères d'acceptation**
Feature: Acquitter une commande et estimer le temps de préparation - Infrastructure

Scenario: Exécuter correctement les opérations techniques en cas nominal
Given les adaptateurs techniques nécessaires sont disponibles
When l'opération technique de la feature est exécutée
Then les données sont persistées ou lues correctement
And les ports attendus sont satisfaits

Scenario: Préserver la cohérence en cas d'échec technique ou métier
Given une exécution où la règle calcul ETA non conforme aux règles de types d'articles conduit à un échec
When l'adaptateur finalise l'opération
Then aucun état partiel incohérent n'est persisté
And une erreur exploitable est remontée vers la couche supérieure

**Notes**
- Garantir la cohérence transactionnelle sur les écritures critiques.
