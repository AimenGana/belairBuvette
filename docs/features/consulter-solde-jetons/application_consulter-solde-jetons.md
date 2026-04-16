# Consulter le solde restant de jetons : Application

**Contexte**
La couche application expose l'API de consultation des soldes boisson et nourriture d'un festivalier, avec les contraintes journalières.

**Critères d'acceptation**
Feature: Consulter le solde restant de jetons - Application

Scenario: Retourner les soldes de jetons pour un festivalier existant
Given un festivalier existant avec des soldes boisson et nourriture
When le client appelle l'endpoint de consultation du solde
Then l'API retourne un statut de succès
And la réponse contient les deux soldes de jetons

Scenario: Retourner une erreur fonctionnelle si les données sont incohérentes
Given un état de solde incohérent conduisant à un solde négatif
When le client appelle l'endpoint de consultation du solde
Then l'API retourne un statut d'erreur fonctionnelle
And la cause de l'échec est explicite

**Notes**
- Le contrat de réponse doit distinguer jetons boisson et jetons nourriture.
