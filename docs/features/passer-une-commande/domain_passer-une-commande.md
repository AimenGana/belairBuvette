# Passer une commande : Domaine

**Contexte**
Le domaine doit permettre la création d’une commande contenant plusieurs articles tout en garantissant les invariants métier : vérification de stock pour chaque ligne, calcul des jetons requis (boisson/nourriture), et absence d’effet de bord en cas d’échec.

**Critères d'acceptation**
Feature: Passer une commande - Domaine

Scenario: Accepter une commande multi-articles quand le stock et les jetons sont suffisants
Given un festivalier avec un solde de jetons suffisant
And des articles disponibles en stock pour toutes les lignes demandées
When le cas d’usage de création de commande est exécuté avec plusieurs lignes
Then la commande est créée avec toutes les lignes demandées
And le coût total en jetons boisson et nourriture est validé
And le stock est décrémenté pour chaque article commandé

Scenario: Rejeter la commande si un article est en rupture de stock
Given un festivalier avec un solde de jetons suffisant
And au moins un article demandé a un stock insuffisant
When le cas d’usage de création de commande est exécuté
Then la commande est refusée avec une erreur de rupture de stock
And aucun jeton n’est débité
And aucun stock n’est décrémenté

Scenario: Rejeter la commande si le solde de jetons est insuffisant
Given un festivalier avec un solde de jetons insuffisant
And tous les articles demandés sont disponibles en stock
When le cas d’usage de création de commande est exécuté
Then la commande est refusée avec une erreur de solde insuffisant
And aucun jeton n’est débité
And aucun stock n’est décrémenté

**Notes**
- Le contrôle de stock et le débit des jetons doivent être traités de façon atomique au niveau métier/transactionnel.
- Les erreurs métier doivent être explicites pour permettre un mapping API précis.
