# Testing Guidelines

## Domain Module Testing Guidelines

Entry point: tests must drive the system through a use-case handler.

### High-level Principles

- Entry point: tests must drive the system through a use-case handler (the "primary" port implementation).
- Dependency Inversion: tests must depend only on abstraction (interfaces/ports), never on concrete technical implementations.
- No mocking frameworks: provide lightweight, in-process fakes for secondary ports.
- Fakes must implement a `TestState<T>` interface so tests can seed initial state.
- Tests follow Given-When-Then (Behavior Driven) structure.

### TestState contract

```java
public interface TestState<T, ID> {
    void add(T item);
    Optional<T> find(ID id);
    List<T> findAll();
}
```

### Fake repository example

```java
public class FakeOrderRepository implements OrderRepository, TestState<Order, String> {
    private final List<Order> store = new ArrayList<>();

    @Override
    public void add(Order item) {
        store.removeIf(o -> o.id().equals(item.id()));
        store.add(item);
    }

    @Override
    public Optional<Order> find(String id) {
        return store.stream().filter(o -> o.id().equals(id)).findFirst();
    }

    @Override
    public List<Order> findAll() { return List.copyOf(store); }
}
```

### Test structure (Given-When-Then)

```java
@Test
void givenValidBasket_whenCreateOrder_thenOrderCreatedAndPaymentRequested() {
    // Given
    fixture.state1().add(/* pre-existing entity */);

    // When
    var result = fixture.useCase().create(/* command */);

    // Then
    assertThat(result).isSuccessful();
    assertThat(fixture.state1().findAll()).hasSize(1);
}
```

## API Testing Guidelines (Application Module)

- Contract-driven: assert response shape and status against the documented API contract.
- Use mocked use-cases and deterministic fakes (WireMock) for fast, deterministic tests.
- Use RestAssured or lightweight HTTP clients as the test driver.

## Infrastructure Testing Guidelines

- Use Testcontainers for all driven adapters.
- Validate Flyway/Liquibase migrations against a clean DB started by Testcontainers.

```java
@Testcontainers
class UserRepositoryIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("buvette_test")
        .withUsername("test")
        .withPassword("test");

    @Test
    void savesAndLoadsUser() {
        // your test
    }
}
```