# Acquitter une commande et estimer le temps de préparation : Application

**Contexte**
La couche application expose l'API d'acquittement et restitue le temps estimé de préparation calculé selon les règles métier.

**Critères d'acceptation**
Feature: Acquitter une commande et estimer le temps de préparation - Application

Scenario: Acquitter une commande et retourner un ETA valide
Given une commande prête à être acquittée
When le barman appelle l'endpoint d'acquittement
Then l'API retourne un statut de succès
And la réponse contient un temps estimé de préparation

Scenario: Rejeter l'acquittement si le calcul ETA est invalide
Given un contexte de commande où les données ne permettent pas un ETA conforme
When le barman appelle l'endpoint d'acquittement
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet est explicite

**Notes**
- La réponse doit permettre de comprendre l'ETA retourné côté client.
