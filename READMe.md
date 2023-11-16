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


## Attempt 2 - `RequestBodyAdvice`
```java
@RequiredArgsConstructor
@ControllerAdvice
public class ParentRequestBodyAdvice extends RequestBodyAdviceAdapter {

  private final ParentRepository parentRepository;

  @Override
  public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    return Objects.equals(targetType.getTypeName(), Child.class.getTypeName());
  }

  @Override
  public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
    Child child = (Child) body;
    // given that we aren't able to deserialize into a String, we need to
    Parent parentId = child.parent();
    Optional<Parent> parent = parentRepository.findById(parentId.id());
    Child updatedChild = child.toBuilder()
      .parent(parent.get())
      .build();
    return updatedChild;
  }
}
```
- This means that the request body must be more complicated:
  - ```json
    {
    "name": "bob-child2",
    "parent": {
    "id": "654dd6a61045064756ed514f"
    }
    }
  ```


## Resources
- Docker Spring Boot 3.1:
  - https://softwaremill.com/do-you-still-need-testcontainers-with-spring-boot-3-1/
  - https://www.naiyerasif.com/post/2023/09/08/improved-local-development-using-containers-in-spring-boot-3-1/
  - Note that does not work well with docker-compose: v2.23.0
