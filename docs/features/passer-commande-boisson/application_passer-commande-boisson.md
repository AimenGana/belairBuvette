# Passer une commande de boisson : Application

**Contexte**
La couche application expose l'API de commande de boisson et orchestre le calcul de coût en jetons selon le type de boisson.

**Critères d'acceptation**
Feature: Passer une commande de boisson - Application

Scenario: Accepter une commande de boisson valide
Given une requête API valide avec une boisson autorisée
When le client appelle l'endpoint de commande de boisson
Then l'API retourne un statut de succès
And la réponse contient le coût en jetons boisson

Scenario: Rejeter une commande quand la règle de coût est violée
Given une requête API qui viole la règle de coût boisson
When le client appelle l'endpoint de commande de boisson
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet est explicite

**Notes**
- Mapper clairement les types non alcoolisée, alcool normale et alcool premium.
