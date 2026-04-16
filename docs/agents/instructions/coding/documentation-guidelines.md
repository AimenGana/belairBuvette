# Documentation Guidelines

## 1) Objectif

La documentation doit aider à comprendre, utiliser, modifier, et faire évoluer le projet.

Elle doit réduire l’ambiguïté, pas répéter le code sans valeur ajoutée.

## 2) Quand documenter

Documenter lorsqu’un changement introduit ou modifie :
- une règle métier importante ;
- un contrat API ;
- une convention de développement ;
- une décision d’architecture ;
- une instruction agent ou un workflow spécifique.

## 3) Ce qu’une bonne documentation doit faire

Une bonne documentation doit être :
- concise ;
- exacte ;
- située au bon endroit ;
- orientée usage et décision.

Elle doit expliquer en priorité :
- le pourquoi ;
- le quoi ;
- les contraintes ;
- les conséquences utiles.

## 4) Ce qu’il faut éviter

Éviter :
- les longs blocs vagues ;
- les évidences déjà claires dans le code ;
- les exemples non maintenus ;
- les documents qui dérivent du comportement réel du système.

## 5) Style recommandé

Privilégier :
- des phrases courtes ;
- des listes quand elles clarifient ;
- des titres explicites ;
- des exemples concrets si cela réduit l’ambiguïté.

Quand une règle est prescriptive, l’écrire clairement.

## 6) Lieu de documentation

Utiliser :
- `README.md` pour la vue d’ensemble et le démarrage ;
- `FEATURES.md` pour les capacités et le périmètre fonctionnel ;
- `docs/` pour les instructions, conventions, décisions, et contenus détaillés ;
- le code pour les commentaires strictement locaux et utiles.

## 7) Maintenance

Toute documentation modifiée doit rester alignée avec le comportement réel du codebase.
Si une documentation n’est plus vraie, la corriger ou la supprimer.

## 8) Règle de qualité

Mieux vaut une documentation courte, exacte et maintenue qu’un document complet mais faux.

Ce document définit la baseline documentaire du repository.
