# Code Review Guidelines

## 1) Objectif

Une revue de code doit augmenter la qualité du changement, pas seulement détecter des fautes de style.

La revue doit prioriser :
- la correction fonctionnelle ;
- le respect de l’architecture ;
- la lisibilité ;
- la testabilité ;
- le niveau de risque introduit.

## 2) Ordre de lecture recommandé

Relire dans cet ordre :
1. le besoin ou l’intention du changement ;
2. les frontières de modules impactées ;
3. le code métier ;
4. les adaptateurs techniques ;
5. les tests ;
6. la documentation mise à jour.

## 3) Points de contrôle obligatoires

Vérifier systématiquement :
- le respect des frontières hexagonales ;
- l’absence de dépendance technique dans `domain` ;
- la cohérence des noms et du vocabulaire métier ;
- la simplicité de l’implémentation ;
- la couverture de test adaptée au risque ;
- le traitement des erreurs et cas limites.

## 4) Priorisation des remarques

Classer mentalement les remarques ainsi :
- **bloquant** : bug, rupture d’architecture, faille évidente, dette inacceptable ;
- **important** : lisibilité, test manquant, contrat ambigu, duplication problématique ;
- **mineur** : formulation, simplification locale, amélioration non essentielle.

Ne pas noyer un problème important dans une liste de détails mineurs.

## 5) Formulation attendue

Une remarque de review doit être :
- précise ;
- justifiée ;
- actionnable ;
- proportionnée au risque réel.

Privilégier :
- le problème observé ;
- pourquoi il compte ;
- la direction de correction.

## 6) Tests dans la revue

Vérifier que les tests :
- couvrent le comportement utile ;
- ne sont pas trop couplés à l’implémentation ;
- restent lisibles ;
- échoueraient réellement si le comportement attendu se brisait.

## 7) Ce qu’il faut éviter

Éviter :
- les préférences personnelles déguisées en règles ;
- les commentaires vagues ;
- les demandes de refactoring non justifiées ;
- l’obsession du style au détriment de la conception ou du risque réel.

## 8) Sortie attendue

Une bonne revue doit permettre de répondre clairement à ces questions :
- est-ce correct ?
- est-ce maintenable ?
- est-ce suffisamment testé ?
- est-ce cohérent avec le projet ?

Ce document définit la baseline de revue de code pour le repository.
