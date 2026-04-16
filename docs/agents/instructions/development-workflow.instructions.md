# Development Workflow Instructions

## 1) Objectif

Ce document définit le workflow de développement attendu dans ce repository.

Le but est de produire des changements petits, cohérents, testés, et alignés avec l’architecture hexagonale du projet.

## 2) Ordre de travail recommandé

Quand une fonctionnalité traverse plusieurs modules, travailler dans cet ordre :
1. `domain` : règles métier, ports, use cases ;
2. `infrastructure` : implémentations techniques des ports ;
3. `application` : exposition API, DTOs, mapping, gestion d’erreurs.

On part du cœur métier vers l’extérieur.

## 3) Découpage du travail

Avant d’implémenter :
- clarifier le besoin fonctionnel ;
- identifier le module concerné ;
- lister les contrats impactés ;
- découper en changements petits et réversibles.

Si le sujet est trop large, le découper avant d’écrire du code.

## 4) Approche de mise en œuvre

Privilégier une boucle courte :
1. écrire ou ajuster le test ;
2. implémenter le minimum ;
3. faire passer le test ;
4. refactorer sans changer le comportement ;
5. relancer les tests ciblés puis les tests globaux pertinents.

Le TDD est recommandé dès qu’il aide à clarifier le comportement attendu.

## 5) Règles d’implémentation

Pendant l’implémentation :
- respecter les frontières entre modules ;
- éviter les dépendances inversées illégitimes ;
- préférer des changements simples à des abstractions prématurées ;
- nommer selon le langage métier autant que possible ;
- garder les diffs petits et lisibles.

## 6) Validation attendue

Avant de considérer un changement comme prêt :
- les tests du module impacté passent ;
- les nouvelles règles métier sont couvertes ;
- les contrats exposés sont cohérents ;
- aucune dette évidente n’est laissée sans être signalée.

Commandes de référence :
- `./gradlew test`
- `./gradlew :domain:test`
- `./gradlew :application:test`
- `./gradlew :infrastructure:test`

## 7) Documentation associée

Mettre à jour la documentation quand un changement modifie :
- un comportement métier visible ;
- un contrat API ;
- une convention de développement ;
- les instructions agent si elles deviennent incomplètes ou incorrectes.

## 8) Git et commits

Les commits doivent rester petits et intentionnels.
Suivre les conventions définies dans le guide git du repository.

Un commit doit idéalement contenir :
- un changement cohérent ;
- les tests associés ;
- la documentation associée si nécessaire.

## 9) Ce qu’il faut éviter

Éviter :
- les gros changements multi-responsabilités ;
- les implémentations qui sautent directement à la couche `application` sans cadrage métier ;
- les refactorings mélangés à des changements fonctionnels non liés ;
- les commits non testés.

Ce document définit la baseline de workflow pour le projet.
Quand un arbitrage est nécessaire, privilégier la clarté, la testabilité, et le respect des frontières architecturales.
