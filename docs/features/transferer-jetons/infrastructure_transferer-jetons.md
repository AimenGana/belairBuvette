# Transférer des jetons à un autre festivalier : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la feature « Transférer des jetons à un autre festivalier ». L'infrastructure persiste la transaction de transfert et son accusé de confirmation.

**Critères d'acceptation**
Feature: Transférer des jetons à un autre festivalier - Infrastructure

Scenario: Exécuter correctement les opérations techniques en cas nominal
Given les adaptateurs techniques nécessaires sont disponibles
When l'opération technique de la feature est exécutée
Then les données sont persistées ou lues correctement
And les ports attendus sont satisfaits

Scenario: Préserver la cohérence en cas d'échec technique ou métier
Given une exécution où la règle transfert dépassant 3 jetons par type conduit à un échec
When l'adaptateur finalise l'opération
Then aucun état partiel incohérent n'est persisté
And une erreur exploitable est remontée vers la couche supérieure

**Notes**
- Garantir la cohérence transactionnelle sur les écritures critiques.
