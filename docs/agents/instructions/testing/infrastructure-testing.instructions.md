# Infrastructure Testing Philosophy

## 1) Objectif et périmètre

Ce document définit la stratégie de test du module `infrastructure/`.

Dans l’architecture hexagonale du projet, le module `infrastructure` contient les implémentations techniques des ports du domaine : persistance, clients externes, messaging, configuration technique, et autres adaptateurs sortants.

Les tests `infrastructure` doivent valider que ces adaptateurs remplissent correctement leur contrat au contact de la technique réelle.

## 2) Principe directeur

Un test d’infrastructure doit vérifier un adaptateur réel contre une frontière technique réelle ou réaliste.

On teste ici la compatibilité entre :
- le contrat du domaine ;
- l’implémentation technique ;
- la technologie sous-jacente utilisée.

## 3) Niveau de test attendu

La stratégie par défaut du module `infrastructure` est le test d’intégration.

Ces tests doivent privilégier :
- une vraie base de données via Testcontainers pour la persistance ;
- de vrais fichiers de configuration et mappings ;
- des simulateurs externes réalistes si un système tiers doit être isolé.

Éviter les tests purement unitaires d’adaptateurs quand ils ne donnent pas plus de confiance qu’un vrai test d’intégration.

## 4) Règles pour la persistance

Pour tout adaptateur de persistance :
- utiliser Testcontainers ;
- démarrer d’une base propre ;
- appliquer les migrations réelles si le projet utilise Flyway ou Liquibase ;
- vérifier lecture, écriture, recherche, et cas limites significatifs.

Ne jamais considérer un repository comme suffisamment testé s’il n’a été validé qu’avec des mocks.

## 5) Règles pour les autres adaptateurs

Pour un client HTTP, message broker, ou autre système externe :
- tester le mapping de requêtes et réponses ;
- tester la gestion des erreurs techniques significatives ;
- utiliser un stub réaliste si la dépendance réelle n’est pas exécutable localement.

Le stub ne doit pas masquer les problèmes de contrat que l’adaptateur est censé gérer.

## 6) Structure des tests

Utiliser Given-When-Then.

- **Given** : environnement technique prêt, données initiales, configuration ;
- **When** : appel de l’adaptateur ;
- **Then** : état technique observé + contrat métier respecté.

Convention de nommage recommandée :

`given<ContexteTechnique>_when<ActionAdaptateur>_then<RésultatAttendu>()`

## 7) Couverture minimale

Pour chaque adaptateur, couvrir au minimum :
- un scénario nominal ;
- un cas de lecture/écriture ou requête/réponse ;
- une erreur technique significative ;
- un cas limite important pour le mapping ou la configuration.

## 8) Ce qu’il faut éviter

Éviter :
- les mocks de base de données ;
- les assertions sur des détails SQL non contractuels ;
- les tests trop couplés à la structure interne de l’adaptateur ;
- les tests lents par mauvaise initialisation ou données excessives.

## 9) Exemple avec Testcontainers

```java
@Testcontainers
class UserRepositoryIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("buvette_test")
        .withUsername("test")
        .withPassword("test");

    @Test
    void givenExistingUser_whenFindById_thenReturnsUser() {
        // Given
        insertUser("user-123", "Alice");

        // When
        var result = repository.findById("user-123");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Alice");
    }
}
```

Ce document définit la baseline de test pour `infrastructure`.
Dans ce module, la confiance vient d’un contact réel avec la technique, pas d’une simulation trop abstraite.
