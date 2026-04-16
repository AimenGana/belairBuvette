# Commander plusieurs articles dans une commande : Domaine

**Contexte**
La couche domaine porte les règles métier de la feature « Commander plusieurs articles dans une commande ». Le domaine agrège les lignes et vérifie les soldes boisson et nourriture.

**Critères d'acceptation**
Feature: Commander plusieurs articles dans une commande - Domaine

Scenario: Appliquer la règle métier nominale
Given les invariants métier de la feature sont satisfaits
When le cas d'usage domaine est exécuté
Then le résultat métier attendu est produit
And les règles de la feature sont respectées

Scenario: Rejeter le traitement quand une règle est violée
Given une requête qui viole la règle coût total supérieur au solde disponible
When le cas d'usage domaine est exécuté
Then le traitement est refusé avec une erreur métier explicite
And aucun effet de bord partiel n'est conservé

**Notes**
- Couvrir au minimum le happy path et les cas limites dans les tests de domaine.
- Les erreurs métier doivent être explicites pour permettre un mapping API précis.
