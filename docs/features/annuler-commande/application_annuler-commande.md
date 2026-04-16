# Annuler une commande : Application

**Contexte**
La couche application expose l'API d'annulation et orchestre le remboursement des jetons si la commande est annulable.

**Critères d'acceptation**
Feature: Annuler une commande - Application

Scenario: Accepter l'annulation d'une commande non acquittée
Given une commande non acquittée
When le client appelle l'endpoint d'annulation
Then l'API retourne un statut de succès
And la réponse confirme l'annulation et le remboursement

Scenario: Rejeter l'annulation d'une commande acquittée
Given une commande déjà acquittée
When le client appelle l'endpoint d'annulation
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique que la commande ne peut pas être annulée

**Notes**
- La confirmation d'annulation doit être explicite et traçable.
