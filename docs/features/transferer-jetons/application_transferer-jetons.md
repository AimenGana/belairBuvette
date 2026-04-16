# Transférer des jetons à un autre festivalier : Application

**Contexte**
La couche application expose l'API de transfert de jetons avec gestion de la confirmation par destinataire.

**Critères d'acceptation**
Feature: Transférer des jetons à un autre festivalier - Application

Scenario: Accepter un transfert valide avec confirmation
Given une demande de transfert dans les limites autorisées
And une confirmation du destinataire
When le client appelle l'endpoint de transfert
Then l'API retourne un statut de succès
And la réponse confirme le transfert effectif

Scenario: Rejeter un transfert dépassant le plafond autorisé
Given une demande de transfert supérieure à trois jetons par type
When le client appelle l'endpoint de transfert
Then l'API retourne un statut d'erreur fonctionnelle
And la cause du rejet indique un dépassement de plafond

**Notes**
- Exposer un statut de transfert permettant de distinguer en attente/confirmé/refusé.
