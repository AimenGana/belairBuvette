# Application Testing Philosophy

## 1) Objectif et périmètre

Ce document définit la stratégie de test du module `application/`.

### Rôle du module `application`

Dans l’architecture hexagonale du projet, le module `application` est l’adaptateur entrant (API/transport). Il :
- exposer les endpoints vers l’extérieur ;
- convertir les requêtes externes en objets utilisables par le domaine ;
- déléguer l’exécution au cœur métier (`domain`) ;
- convertir les résultats métier en réponses API.

### Ce que ces tests doivent garantir

Les tests du module `application` valident le comportement observable au niveau API :
- contrat de réponse (status, payload, erreurs) ;
- mapping entrée/sortie (DTO ↔ objets du domaine) ;
- validation des entrées et gestion des erreurs côté API.

### Ce que ces tests ne doivent pas couvrir

Ces tests ne remplacent pas :
- les tests de logique métier (couverts dans `domain`) ;
- les tests d’intégration technique des adaptateurs sortants (couverts dans `infrastructure`).

### Positionnement dans la pyramide de tests

Pour ce module, la priorité est donnée aux tests d’intégration au niveau API :
- plus proches du comportement utilisateur que des tests unitaires de contrôleurs isolés ;
- plus ciblés que des tests end-to-end globaux.

## 2) Principes directeurs des tests `application`

### 2.1 Contrat API d’abord

Chaque test doit vérifier un comportement **observable par un client API** :
- code HTTP ;
- structure et contenu du body ;
- format des erreurs ;
- en-têtes pertinents si applicables.

Éviter les assertions sur des détails d’implémentation internes.

### 2.2 Isolation de la logique métier

Le module `application` orchestre, il ne porte pas la logique métier profonde. Les tests `application` doivent :
- vérifier que la requête API est correctement traduite vers l’appel métier ;
- vérifier que la réponse métier est correctement exposée côté API ;
- ne pas dupliquer les scénarios métier déjà couverts dans `domain`.

### 2.3 Déterminisme et rapidité

Les tests doivent être :
- déterministes (pas de dépendance aléatoire, pas d’horloge implicite non contrôlée) ;
- rapides à exécuter localement et en CI ;
- indépendants les uns des autres.

### 2.4 Lisibilité (Given-When-Then)

Les cas de test suivent systématiquement la structure :
- **Given** : contexte initial et données d’entrée ;
- **When** : appel API testé ;
- **Then** : assertions sur la réponse et les interactions attendues.

Cette structure est obligatoire.

## 3) Types de tests attendus dans `application`

### 3.1 Tests d’intégration API (prioritaires)

Ces tests sont la base de la stratégie du module. Ils vérifient le comportement d’un endpoint de bout en bout dans la couche `application` :
- réception d’une requête HTTP ;
- validation et mapping de la requête ;
- délégation au cas d’usage (port primaire) ;
- transformation du résultat en réponse HTTP.

Objectif : garantir le contrat API exposé aux consommateurs.

### 3.2 Tests unitaires ciblés (optionnels)

Des tests unitaires sont autorisés seulement s’ils apportent une vraie valeur locale, par exemple :
- logique de mapping complexe dans un mapper dédié ;
- sérialisation/désérialisation spécifique d’un DTO ;
- validation personnalisée non triviale.

Ils ne doivent jamais devenir majoritaires.

### 3.3 Ce qu’on exclut explicitement

Dans ce module, on évite :
- des tests E2E complets impliquant toute l’infrastructure externe ;
- des tests qui revalident toute la logique métier interne du domaine ;
- des tests trop couplés à des détails techniques internes (fragiles au refactoring).

### 3.4 Couverture minimale

Pour chaque endpoint, couvrir au minimum :
- un scénario nominal (succès) ;
- un scénario d’entrée invalide (erreur de validation) ;
- un scénario d’erreur métier significative (si applicable) mappée vers une réponse HTTP cohérente.

## 4) Environnement technique des tests `application`

### 4.1 Stack de base

Le repository est configuré avec :
- Java 21 (toolchain Gradle) ;
- JUnit Jupiter 5.10.2 ;
- tâche standard `test` via Gradle.

Commandes de référence :
- lancer tous les tests : `./gradlew test` ;
- lancer uniquement `application` : `./gradlew :application:test`.

### 4.2 Bibliothèques de test recommandées

Pour les tests API du module `application`, utiliser :
- framework de test : JUnit 5 ;
- assertions : AssertJ (recommandé pour la lisibilité) ;
- driver HTTP de test : MockMvc (si Spring MVC) ou WebTestClient (si WebFlux) ;
- mocks/fakes des dépendances du contrôleur : outils natifs du framework de test (ex: `@MockBean`) ou doubles explicites.

> Tant que le stack web n’est pas figé dans ce starter, ce document pose une orientation.
> Dès que le framework web est choisi, fixer un seul driver officiel (MockMvc ou WebTestClient) et l’appliquer partout.

### 4.3 Contraintes d’exécution

Les tests `application` doivent :
- s’exécuter sans dépendance à un service externe réel ;
- ne pas nécessiter de conteneur Docker (réservé aux tests `infrastructure`) ;
- rester reproductibles localement et en CI avec la même commande Gradle.

## 5) Structure standard d’un test API

### 5.1 Convention de nommage

Nom de méthode recommandé :

`given<Contexte>_when<ActionAPI>_then<RésultatAttendu>()`

Exemples :
- `givenValidPayload_whenCreateOrder_thenReturns201AndBody()`
- `givenMissingRequiredField_whenCreateOrder_thenReturns400WithValidationError()`

### 5.2 Pattern Given-When-Then obligatoire

Dans chaque test :
- **Given** : préparation des entrées + comportement attendu des dépendances ;
- **When** : appel HTTP sur l’endpoint ;
- **Then** : assertions sur status, body, et éléments contractuels attendus.

### 5.3 Assertions minimales attendues

Pour tout scénario, vérifier au minimum :
- le code HTTP ;
- le type de réponse (JSON ou autre attendu) ;
- les champs clés du payload (pas uniquement “non null”).

Pour les erreurs, vérifier aussi :
- la structure du body d’erreur ;
- le code d’erreur fonctionnel/technique attendu (si défini) ;
- un message exploitable côté client (sans fuite de détails techniques sensibles).

### 5.4 Isolation des cas de test

Chaque test doit être autonome :
- pas de dépendance à l’ordre d’exécution ;
- pas de partage d’état mutable entre tests ;
- setup local explicite dans le test ou dans des helpers dédiés.

## 6) Doubles de test et frontières de dépendances

### 6.1 Règle générale

Dans les tests du module `application`, les dépendances vers le domaine sont remplacées par des doubles (mocks ou fakes) afin de :
- isoler le comportement API ;
- éviter les effets de bord techniques ;
- garder des tests rapides et déterministes.

### 6.2 Ce qui peut être doublé

Typiquement, on double :
- les use cases / handlers exposés par le domaine ;
- les services techniques transverses non essentiels au contrat API (horloge, id generator, etc.), si présents.

### 6.3 Ce qui ne doit pas être doublé dans ces tests

On ne redéfinit pas manuellement :
- le protocole HTTP ;
- le mécanisme de sérialisation JSON utilisé par le framework ;
- les composants de validation standard du framework.

L’objectif est de tester un vrai comportement API, pas une simulation “maison” du framework web.

### 6.4 Vérification des interactions

La vérification d’interactions avec les use cases est autorisée, mais doit rester sobre :
- vérifier l’appel attendu (commande transmise, nombre d’appels) ;
- éviter les assertions ultra-couplées à l’implémentation interne.

La priorité reste l’assertion du **résultat observable côté API**.

## 7) Matrice de couverture minimale par endpoint

Pour chaque endpoint exposé par `application`, la suite de tests doit couvrir au minimum les catégories suivantes.

### 7.1 Scénario nominal

Valider le “happy path” complet :
- requête valide ;
- appel métier attendu ;
- réponse HTTP conforme au contrat.

### 7.2 Erreurs de validation d’entrée

Couvrir les cas d’entrée invalide pertinents :
- champ requis manquant ;
- format invalide (ex: type, pattern, borne) ;
- contrainte de taille/valeur non respectée.

Attendu : réponse d’erreur stable et lisible pour le client.

### 7.3 Erreurs métier mappées côté API

Quand un use case signale une erreur métier (ex: ressource absente, règle métier violée), les tests doivent vérifier :
- le mapping vers le bon statut HTTP ;
- la structure de l’erreur exposée ;
- l’absence de fuite de détails internes (stacktrace, message technique brut).

### 7.4 Cas limites utiles

Ajouter des cas limites quand ils ont une valeur métier/API claire, par exemple :
- collections vides ;
- pagination en borne (première/dernière page) ;
- paramètres optionnels absents.

Ne pas surcharger la suite avec des cas redondants.

## 8) Données de test et organisation des fixtures

### 8.1 Données explicites et utiles

Les données de test doivent être :
- lisibles ;
- minimales ;
- orientées métier quand c’est pertinent.

Éviter les payloads massifs ou bruités qui cachent l’intention du scénario.

### 8.2 Helpers et builders de test

Quand plusieurs tests partagent les mêmes formes de requêtes ou réponses, factoriser via :
- des builders de test ;
- des factory methods ;
- des helpers locaux au package de test.

La factorisation doit améliorer la lisibilité, jamais masquer le comportement testé.

### 8.3 Règle de proximité

Conserver au plus près du test :
- les valeurs importantes pour comprendre le scénario ;
- les variations propres au cas testé.

Extraire uniquement ce qui est répétitif et stable.

### 8.4 Données déterministes

Ne pas utiliser dans les tests :
- des UUID aléatoires non maîtrisés ;
- `now()` sans contrôle ;
- des valeurs générées qui compliquent les assertions.

Préférer des identifiants, dates et valeurs fixes, nommés explicitement.

## 9) Anti-patterns et maintenance de la suite de tests

### 9.1 Anti-patterns à éviter

Éviter absolument :
- des tests qui valident plusieurs comportements indépendants à la fois ;
- des assertions vagues (`isOk`, `notNull`) sans vérification du contrat utile ;
- des tests copiés-collés avec de faibles variations ;
- des vérifications trop fines sur l’implémentation interne du contrôleur ;
- des tests cassants à cause de détails de formatting non contractuels.

### 9.2 Un test = une intention principale

Chaque test doit porter une intention claire et nommée. S’il nécessite trop de préparation ou trop d’assertions hétérogènes, il doit être découpé.

### 9.3 Quand modifier un test existant

On modifie un test existant si le contrat attendu évolue réellement. On ajoute un nouveau test si l’on couvre un nouveau comportement.

Cette règle évite de transformer un test en “fourre-tout” au fil du temps.

### 9.4 Signal avant volume

Une petite suite de tests précise, stable et lisible vaut mieux qu’une grosse suite redondante.
Le but n’est pas de maximiser le nombre de tests, mais la confiance apportée par la suite.

## 10) Template de test recommandé

Le template ci-dessous sert de référence de structure. Il doit être adapté au framework web réellement utilisé dans le module `application`.

```java
@Test
void givenValidPayload_whenCreateResource_thenReturns201AndResponseBody() {
    // Given
    var requestBody = """
        {
          "name": "Citronnade",
          "price": 4.50
        }
        """;

    var useCaseResult = new CreateResourceResult("resource-123", "Citronnade", 4.50);
    when(createResourceUseCase.handle(any(CreateResourceCommand.class)))
        .thenReturn(useCaseResult);

    // When
    var response = performPost("/resources", requestBody);

    // Then
    assertThat(response.statusCode()).isEqualTo(201);
    assertThat(response.contentType()).isEqualTo("application/json");
    assertThat(response.body().jsonPath().getString("id")).isEqualTo("resource-123");
    assertThat(response.body().jsonPath().getString("name")).isEqualTo("Citronnade");
}
```

### Points à retenir sur ce template

- le **Given** expose clairement les données d’entrée ;
- le **When** contient un seul appel API ;
- le **Then** vérifie le contrat utile, pas seulement un succès générique ;
- le test reste centré sur le comportement HTTP observable.

---

Ce document définit la baseline de test pour `application`.
En cas de divergence entre confort local et cohérence globale, la cohérence globale du module prime.
