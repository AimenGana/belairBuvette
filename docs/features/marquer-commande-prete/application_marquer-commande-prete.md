# Marquer une commande comme prête : Application

**Contexte**
La couche application expose l'API de passage à l'état prête avec contrôle de disponibilité des items préparés.

**Critères d'acceptation**
Feature: Marquer une commande comme prête - Application

Scenario: Marquer la commande prête quand les items sont disponibles
Given une commande dont les items préparés sont suffisants
When le barman appelle l'endpoint de passage à prête
Then l'API retourne un statut de succès
And la réponse confirme que la commande est prête

Scenario: Rejeter le passage à prête si les items préparés sont insuffisants
Given une commande avec des items préparés insuffisants
When le barman appelle l'endpoint de passage à prête
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique une préparation insuffisante

**Notes**
- La notification au festivalier doit être déclenchée uniquement après succès.
