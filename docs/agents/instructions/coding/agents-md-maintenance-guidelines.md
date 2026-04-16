# AGENTS.md Maintenance Guidelines

## 1) Objectif

Ce document définit comment maintenir `AGENTS.md` et les autres fichiers d’instructions agent du repository.

Le but est de garder des consignes :
- cohérentes ;
- applicables ;
- non contradictoires ;
- réellement reliées à des fichiers existants.

## 2) Principe directeur

Une instruction agent doit être claire, actionnable, et vérifiable.

Éviter les consignes vagues, redondantes, ou impossibles à appliquer de manière fiable.

## 3) Quand mettre à jour les instructions agent

Mettre à jour `AGENTS.md` ou les fichiers associés quand change :
- l’architecture du projet ;
- le workflow de développement ;
- la stratégie de test ;
- les conventions de revue ou de documentation ;
- les chemins de fichiers référencés.

## 4) Règles de rédaction

Privilégier :
- des règles courtes ;
- des verbes d’action ;
- des formulations non ambiguës ;
- des sections à responsabilité claire.

Une règle importante doit être explicite.
Une règle exceptionnelle ne doit pas être écrite comme une norme générale.

## 5) Cohérence interne

Lors d’une modification, vérifier systématiquement :
- qu’aucune règle n’en contredit une autre ;
- que les fichiers référencés existent ;
- que les noms de modules et chemins sont exacts ;
- que les consignes restent alignées avec le codebase réel.

## 6) Niveau de détail

`AGENTS.md` doit rester un point d’entrée.
Les détails longs ou spécialisés doivent être déplacés dans des fichiers dédiés sous `docs/agents/instructions/`.

## 7) Ce qu’il faut éviter

Éviter :
- les doublons entre plusieurs fichiers d’instructions ;
- les règles trop dépendantes d’un outil non encore présent ;
- les références cassées ;
- les formulations décoratives sans impact opérationnel.

## 8) Validation après modification

Après chaque mise à jour importante :
- relire le document comme un contrat d’usage ;
- vérifier les chemins référencés ;
- supprimer les instructions obsolètes ;
- resserrer les sections trop longues ou trop floues.

Ce document définit la baseline de maintenance des instructions agent pour le repository.
