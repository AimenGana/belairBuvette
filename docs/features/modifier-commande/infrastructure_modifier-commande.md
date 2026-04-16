# Modifier une commande : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la feature « Modifier une commande ». L'infrastructure met à jour les lignes de commande de façon cohérente.

**Critères d'acceptation**
Feature: Modifier une commande - Infrastructure

Scenario: Exécuter correctement les opérations techniques en cas nominal
Given les adaptateurs techniques nécessaires sont disponibles
When l'opération technique de la feature est exécutée
Then les données sont persistées ou lues correctement
And les ports attendus sont satisfaits

Scenario: Préserver la cohérence en cas d'échec technique ou métier
Given une exécution où la règle commande déjà acquittée non modifiable conduit à un échec
When l'adaptateur finalise l'opération
Then aucun état partiel incohérent n'est persisté
And une erreur exploitable est remontée vers la couche supérieure

**Notes**
- Garantir la cohérence transactionnelle sur les écritures critiques.
