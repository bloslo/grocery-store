# Online Grocery Store

The application is making use of simple rules and actions in order to calculate discounts.

## Getting started

To run the application with Maven:

```
mvn spring-boot:run
```

OR with Maven Wrapper:

On Unix systems:

```
./mvnw spring-boot:run
```

On Windows:

```
mvnw.cmd spring-boot:run
```

To run all tests from the command line:

```
mvn clean test
```

To run the tests with Maven wrapper replace `mvn` with `./mvnw` or `mvnw.cmd`, depending on your operating system.

## Testing

Get all rules using pagination.

```
curl -X GET "localhost:8080/api/v1/rules?page=0&size=5"
```

Get all items and their prices.

```
curl -X GET localhost:8080/api/v1/prices
```

Create an order.

```
curl -H "Content-Type: application/json" -d '{"orderItems":[{"name":"Bread","quantity":3,"produced":[2024,5,26]},{"name":"Vegetables","quantity":200,"produced":null},{"name":"Dutch beer","quantity":6,"produced":null}]}' localhost:8080/api/v1/order
```
