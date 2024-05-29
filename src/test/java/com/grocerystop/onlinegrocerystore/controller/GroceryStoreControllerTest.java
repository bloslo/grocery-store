package com.grocerystop.onlinegrocerystore.controller;

import com.grocerystop.onlinegrocerystore.dto.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
public class GroceryStoreControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    void getRules() {
        Response response = given().queryParam("page", 0)
                .queryParam("size", 5)
                .get("/api/v1/rules")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        RuleDto rule = jsonPath.getObject("content[0]", RuleDto.class);
        assertNotNull(rule);

        assertThat(rule.description()).isEqualTo("5% discount for vegetables that weight between 0g and 100g");
        assertThat(rule.rule()).isEqualTo("name = 'Vegetables' and quantity > 0 and quantity <= 100");

        List<RuleDto> rules = jsonPath.getList("content", RuleDto.class);
        assertThat(rules).hasSize(5);
    }

    @Test
    void getCurrentPrices() {
        Response response = given().get("/api/v1/prices")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        List<ItemDto> items = jsonPath.getList("content", ItemDto.class);
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(5);
    }

    @Test
    void createOrder() {
        OrderItemDto bread = new OrderItemDto("Bread", 3, LocalDate.now().minusDays(3));
        OrderItemDto vegetables = new OrderItemDto("Vegetables", 200, null);
        OrderItemDto beers = new OrderItemDto("Dutch beer", 6, null);

        OrderRequest orderRequest = new OrderRequest(List.of(bread, vegetables, beers));
        Response response = given().log().body()
                .body(orderRequest)
                .contentType("application/json")
                .post("/api/v1/order")
                .then()
                .statusCode(201)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();
        OrderDetails orderDetails = jsonPath.getObject("", OrderDetails.class);

        assertThat(orderDetails).isNotNull();
        assertThat(orderDetails.total()).isEqualTo(BigDecimal.valueOf(7.86).setScale(2));
        assertThat(orderDetails.items()).hasSize(3);
        assertThat(orderDetails.receiptItems()).hasSize(3);
    }
}
