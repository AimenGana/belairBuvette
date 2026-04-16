# Envoyer des notifications régulières d'hydratation : Application

**Contexte**
La couche application orchestre l'émission des notifications d'hydratation selon la cadence nominale et renforcée.

**Critères d'acceptation**
Feature: Envoyer des notifications régulières d'hydratation - Application

Scenario: Émettre une notification sur la cadence nominale
Given un festivalier dans la plage horaire 11h00-19h00
And une consommation alcoolisée inférieure ou égale à trois boissons sur la dernière heure
When le service applicatif déclenche l'envoi périodique
Then l'API/service retourne un statut de succès
And une notification est émise toutes les heures

Scenario: Appliquer la cadence renforcée pour forte consommation
Given un festivalier ayant consommé plus de trois boissons alcoolisées sur la dernière heure
When le service applicatif déclenche l'envoi périodique
Then l'API/service applique une cadence toutes les trente minutes
And aucun envoi n'est réalisé hors plage horaire autorisée

**Notes**
- Les règles de fréquence doivent être explicites dans les événements de notification.
