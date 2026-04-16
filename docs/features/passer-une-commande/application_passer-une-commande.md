# Passer une commande : Application

**Contexte**
La couche application expose un endpoint REST pour créer une commande contenant plusieurs articles. Elle orchestre le cas d’usage domaine et mappe les erreurs métier vers des réponses HTTP explicites.

**Critères d'acceptation**
Feature: Passer une commande - Application

Scenario: Retourner un succès pour une commande valide
Given une requête API de création de commande avec plusieurs lignes valides
And le festivalier a un solde de jetons suffisant
And tous les articles demandés sont disponibles en stock
When le client appelle l’endpoint de création de commande
Then l’API répond avec un statut de succès
And la réponse contient l’identifiant de commande créée
And la réponse contient le récapitulatif des lignes commandées

Scenario: Retourner une erreur métier en cas de rupture de stock
Given une requête API de création de commande avec plusieurs lignes
And au moins un article demandé est en rupture de stock
When le client appelle l’endpoint de création de commande
Then l’API répond avec un statut d’erreur fonctionnelle
And la réponse indique explicitement la cause rupture de stock

Scenario: Retourner une erreur métier en cas de solde insuffisant
Given une requête API de création de commande avec plusieurs lignes
And le festivalier n’a pas assez de jetons
When le client appelle l’endpoint de création de commande
Then l’API répond avec un statut d’erreur fonctionnelle
And la réponse indique explicitement la cause solde insuffisant

**Notes**
- Prévoir un DTO de requête avec `customerId` et une liste de lignes `{articleId, quantity}`.
- La validation de base (quantités > 0, payload non vide) est effectuée en entrée applicative avant appel du cas d’usage.
