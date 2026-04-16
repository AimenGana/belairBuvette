# Passer une commande de groupe avec mutualisation : Application

**Contexte**
La couche application expose l'API de commande de groupe avec contributions individuelles de jetons.

**Critères d'acceptation**
Feature: Passer une commande de groupe avec mutualisation - Application

Scenario: Accepter une commande de groupe quand la contribution couvre le total
Given une requête API de commande de groupe avec contributions suffisantes
When le client appelle l'endpoint de commande de groupe
Then l'API retourne un statut de succès
And la réponse contient le récapitulatif des contributions

Scenario: Rejeter une commande de groupe quand les contributions sont insuffisantes
Given une requête API de commande de groupe avec contributions insuffisantes
When le client appelle l'endpoint de commande de groupe
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique une couverture insuffisante

**Notes**
- Les contributions doivent être traçables par festivalier.
