# Passer une commande : Infrastructure

**Contexte**
La couche infrastructure implémente les ports nécessaires à la création de commande : lecture/mise à jour du stock, persistance de commande, et opérations de débit, avec garanties transactionnelles pour éviter les effets de bord partiels.

**Critères d'acceptation**
Feature: Passer une commande - Infrastructure

Scenario: Persister la commande et mettre à jour le stock en succès
Given les adaptateurs de persistance sont disponibles
And toutes les lignes demandées ont un stock suffisant
When l’adaptateur exécute la transaction de création de commande
Then la commande est persistée avec toutes ses lignes
And le stock de chaque article est décrémenté correctement
And la transaction est validée

Scenario: Annuler la transaction si un article est en rupture pendant le traitement
Given une création de commande multi-articles en cours
And un contrôle de disponibilité échoue pour au moins une ligne
When l’adaptateur finalise l’opération transactionnelle
Then la transaction est annulée
And aucune décrémentation de stock n’est persistée
And aucune commande partielle n’est persistée

Scenario: Garantir la cohérence en concurrence sur le stock
Given deux commandes concurrentes sur le même article avec stock limité
When les deux transactions sont exécutées simultanément
Then une seule commande peut consommer le stock restant
And l’autre commande échoue avec une erreur de stock insuffisant

**Notes**
- Les stratégies de verrouillage/isolement doivent empêcher les sur-ventes.
- Les exceptions techniques sont traduites vers des erreurs de port compréhensibles par le domaine.
