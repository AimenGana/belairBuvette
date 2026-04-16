# Commander plusieurs articles dans une commande : Application

**Contexte**
La couche application expose l'API de commande multi-articles et orchestre la validation du coût total face au solde boisson/nourriture.

**Critères d'acceptation**
Feature: Commander plusieurs articles dans une commande - Application

Scenario: Accepter une commande multi-articles valide
Given une requête API avec plusieurs lignes d'articles valides
When le client appelle l'endpoint de création de commande
Then l'API retourne un statut de succès
And la réponse contient le récapitulatif des lignes commandées

Scenario: Rejeter une commande si le coût total dépasse le solde
Given une requête API dont le coût total dépasse le solde du festivalier
When le client appelle l'endpoint de création de commande
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique un solde insuffisant

**Notes**
- La réponse d'erreur doit rester stable pour faciliter le traitement client.
