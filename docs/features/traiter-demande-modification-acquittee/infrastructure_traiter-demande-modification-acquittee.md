# Examiner et accepter/refuser une demande de modification sur commande acquittée : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la feature « Examiner et accepter/refuser une demande de modification sur commande acquittée ». L'infrastructure supporte les liens entre commandes pour transfert d'articles.

**Critères d'acceptation**
Feature: Examiner et accepter/refuser une demande de modification sur commande acquittée - Infrastructure

Scenario: Exécuter correctement les opérations techniques en cas nominal
Given les adaptateurs techniques nécessaires sont disponibles
When l'opération technique de la feature est exécutée
Then les données sont persistées ou lues correctement
And les ports attendus sont satisfaits

Scenario: Préserver la cohérence en cas d'échec technique ou métier
Given une exécution où la règle aucun article transférable vers une autre commande conduit à un échec
When l'adaptateur finalise l'opération
Then aucun état partiel incohérent n'est persisté
And une erreur exploitable est remontée vers la couche supérieure

**Notes**
- Garantir la cohérence transactionnelle sur les écritures critiques.
