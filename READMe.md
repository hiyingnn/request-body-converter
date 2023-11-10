# Overview
- This project aims to explore the different ways that I could inject an object into a request body, given the `referencingId`. In here we model it as such:
  - Given a `parent` resource:
    - ``` java
        @Document("parent")
        public record Parent(@Id String id, String name) {
        }
      ```
      - ```json
        {
          "name": "bob"
        }
        ```
        - We want the `child` resource to have the injected parent resource, given the `id` in the `requestBody`.
        - ``` java
              @Document("child")
              public record Child(@Id String id,
                  String name,
                  Parent parent) {
              }
          ```
        - ```json
              {
                "name": "bob-child",
                "parent": "654dd6a61045064756ed514f"
              }
          ```

## Attempt 1 - Jackson Deserializer
- Via extending a `StdConverter`
```java
@Component
@RequiredArgsConstructor
public class ParentJacksonConverter extends StdConverter<String, Parent> {

  private final ParentRepository parentRepository;

  @Override
  public Parent convert(String id) {
    return parentRepository.findById(id).orElseThrow(NoParentException::new);
  }
}
```
- Declaring the parent to e deseralized as such:
``` java
 @Document("child")
  public record Child(@Id String id,
  String name,
  @JsonDeserialize(converter = ParentJacksonConverter.class)
  Parent parent) {
}
```

### Evaluation:
1. Handling exceptions
2. Responsibility of conversion

