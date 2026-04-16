# Modifier une commande : Application

**Contexte**
La couche application expose l'API de modification de commande et orchestre les validations liées au statut de commande.

**Critères d'acceptation**
Feature: Modifier une commande - Application

Scenario: Accepter une modification sur une commande non acquittée
Given une commande non acquittée et une requête de modification valide
When le client appelle l'endpoint de modification
Then l'API retourne un statut de succès
And la réponse contient la commande mise à jour

Scenario: Rejeter une modification sur une commande acquittée
Given une commande déjà acquittée
When le client appelle l'endpoint de modification
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique que la commande n'est plus modifiable

**Notes**
- Le contrat API doit distinguer modification directe et demande de changement post-acquittement.
