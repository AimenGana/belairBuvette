# Examiner une demande de modification sur commande acquittée : Application

**Contexte**
La couche application expose l'API de décision (accepter/refuser) d'une demande de modification sur commande déjà acquittée.

**Critères d'acceptation**
Feature: Examiner une demande de modification sur commande acquittée - Application

Scenario: Accepter la demande quand au moins un article est transférable
Given une demande de modification avec au moins un article préparé transférable
When le barman appelle l'endpoint de décision avec acceptation
Then l'API retourne un statut de succès
And la réponse contient le nouvel ETA communiqué

Scenario: Refuser la demande quand aucun article n'est transférable
Given une demande de modification sans article transférable
When le barman appelle l'endpoint de décision avec évaluation
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du refus est explicite

**Notes**
- Le motif de décision doit être traçable pour audit fonctionnel.
