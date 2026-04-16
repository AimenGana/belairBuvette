# Passer une commande de nourriture : Application

**Contexte**
La couche application expose l'API de commande de nourriture et orchestre le calcul de coût selon le type d'article alimentaire.

**Critères d'acceptation**
Feature: Passer une commande de nourriture - Application

Scenario: Accepter une commande de nourriture valide
Given une requête API valide avec un article alimentaire autorisé
When le client appelle l'endpoint de commande de nourriture
Then l'API retourne un statut de succès
And la réponse contient le coût en jetons nourriture

Scenario: Rejeter une commande quand la règle de coût est violée
Given une requête API qui viole la règle de coût nourriture
When le client appelle l'endpoint de commande de nourriture
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet est explicite

**Notes**
- Le contrat d'entrée doit distinguer snack et repas.
