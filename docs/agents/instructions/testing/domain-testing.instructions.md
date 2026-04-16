# Domain Testing Philosophy

## 1) Objectif et périmètre

Ce document définit la stratégie de test du module `domain/`.

Dans l’architecture hexagonale du projet, le module `domain` contient :
- les entités et value objects ;
- les ports ;
- les services métier ;
- les use cases.

Les tests `domain` doivent valider le comportement métier du cœur de l’application, indépendamment des détails techniques.

## 2) Principe directeur

Le domaine se teste par ses cas d’usage.

Le point d’entrée d’un test de domaine doit être un use case, un handler, ou un service métier exposant un comportement fonctionnel complet.
Éviter les tests centrés sur des getters, des détails d’implémentation, ou des méthodes purement techniques sans valeur métier.

## 3) Frontières à respecter

Les tests du module `domain` doivent :
- dépendre uniquement des abstractions du domaine ;
- remplacer les ports secondaires par des doubles simples ;
- rester indépendants de tout framework web, ORM, base de données, ou conteneur.

Les tests `domain` ne doivent jamais dépendre de classes du module `infrastructure`.

## 4) Doubles de test

Privilégier des fakes en mémoire plutôt que des mocks complexes.

Règles :
- pas de framework de mocking si un fake simple suffit ;
- les fakes doivent être lisibles, déterministes, et limités au besoin du test ;
- si un fake stocke de l’état, exposer des opérations simples pour initialiser et inspecter cet état.

Contrat recommandé pour les fakes de stockage :

```java
public interface TestState<T, ID> {
    void add(T item);
    Optional<T> find(ID id);
    List<T> findAll();
}
```

Exemple :

```java
public class FakeOrderRepository implements OrderRepository, TestState<Order, String> {
    private final List<Order> store = new ArrayList<>();

    @Override
    public void add(Order item) {
        store.removeIf(existing -> existing.id().equals(item.id()));
        store.add(item);
    }

    @Override
    public Optional<Order> find(String id) {
        return store.stream().filter(order -> order.id().equals(id)).findFirst();
    }

    @Override
    public List<Order> findAll() {
        return List.copyOf(store);
    }
}
```

## 5) Structure des tests

Utiliser systématiquement le format Given-When-Then.

- **Given** : état initial métier et dépendances du use case ;
- **When** : exécution du comportement métier ;
- **Then** : résultat fonctionnel + effets métier observables.

Convention de nommage recommandée :

`given<Contexte>_when<ActionMétier>_then<RésultatAttendu>()`

Exemple :

`givenAvailableStock_whenCreateOrder_thenOrderIsCreated()`

## 6) Couverture attendue

Pour chaque use case, couvrir au minimum :
- un scénario nominal ;
- les règles métier principales ;
- les cas limites utiles ;
- les erreurs métier significatives.

La suite peut être volontairement redondante si cette redondance augmente la confiance sur une règle métier importante.

## 7) Ce qu’il faut éviter

Éviter :
- les tests pilotés par des détails techniques ;
- les assertions vagues ;
- les mocks sur-spécifiés ;
- les tests qui traversent l’infrastructure réelle ;
- les tests qui revalident la sérialisation HTTP ou la persistance SQL.

## 8) Exemple de test

```java
@Test
void givenValidBasket_whenCreateOrder_thenOrderCreatedAndPaymentRequested() {
    // Given
    fixture.orderRepository().add(existingOrder());

    // When
    var result = fixture.createOrderUseCase().handle(validCommand());

    // Then
    assertThat(result).isSuccessful();
    assertThat(fixture.orderRepository().findAll()).hasSize(2);
}
```

Ce document définit la baseline de test pour `domain`.
Le domaine doit rester la couche la plus simple à tester et la plus protégée des détails techniques.
