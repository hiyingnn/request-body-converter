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

## Complexities
- While there are many resources on how to resolve a `@PathVariable`, our use-case is not very common:
  - Reading from request body
  - Generic approach:
    - Fetch `referenceId` 
    - Fetch/convert to domain object

## Technical Approach

### Read from request body
We are only able to read from the input stream once. As such, if we want to read the request body multiple times, we have to cache (best during the filter). For now, we will need to read the input stream at least twice:

1. Reqeust Body Advice (for the actual domain object)
2. 1 time for each parent we want to fetch

- As such, we will need to introduce:
  - `CachedHttpServletRequest` and `CachedHttpBodyServletRequest` - extends the usual `ServletReqeust` 
  - `ContentCachingFilter` - filter to cache

## Generic approach - get reference
To have a generic approach of getting the `id` field reference, we can introduce a `@Reference` annotation, with the `idField` containing the field of the reference.

```java
   @PostMapping("child")
    public Child createChild(
            @RequestBody
            Child child,
            @Reference(
            idField = "parentId"
    ) Parent parent) {
      Child createdChild = childRepository.save(child);
      log.info("Parent found {}", parent);
      log.info("Child created {}", createdChild);
      return createdChild;
```
We can then utilise an implementation of the `HandlerMethodArgumentResolver`, to do the following
1. Obtain the request body via the cached `CachedHttpServletRequest`
2. Use `JsonPath` to fetch the field, given the field in `@Reference` annotation
3. Use `DomainClassConverter` to generically obtain the domain class
   - Here, we also throw an exception if the parent cannot be found


## Generic approach - fetch and get domain object
`DomainClassConverter` is a `spring-data` generic converter, which enables us to convert from an `id` to an `entity`.
In the `HandlerMethodArgumentResolver`, we are also able to generically map it to the class, given the methodParameters' parameter type
```java
Class<?> clazz = methodParameter.getParameterType();
Object foundReference = domainClassConverter.convert(referenceId, TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(clazz));
```

    
## Resources
- Docker Spring Boot 3.1:
  - https://softwaremill.com/do-you-still-need-testcontainers-with-spring-boot-3-1/
  - https://www.naiyerasif.com/post/2023/09/08/improved-local-development-using-containers-in-spring-boot-3-1/
  - Note that does not work well with docker-compose: v2.23.0

- Argument Resolver:
  - https://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-creating-a-custom-handlermethodargumentresolver/
  - https://medium.com/trabe/make-spring-mvc-work-for-you-injecting-custom-method-arguments-fc68934cabeb
  - https://reflectoring.io/spring-boot-argumentresolver/

- Cache request body: 
  - https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
---
