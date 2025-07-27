package com.rafiyad.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @RouterOperations({
        @RouterOperation(
                path = "/api/products",
                method = RequestMethod.GET,
                beanClass = ProductHandler.class,
                beanMethod = "getAllProducts",
                operation = @Operation(
                        summary = "Get all products",
                        description = "Retrieve a list of all available products.",
                        responses = {
                                @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                                        content = @Content(schema = @Schema(implementation = Product.class)))
                        }
                )
        ),
        @RouterOperation(
                path = "/api/products/{id}",
                method = RequestMethod.GET,
                beanClass = ProductHandler.class,
                beanMethod = "getProductById",
                operation = @Operation(
                        summary = "Get a product by ID",
                        description = "Retrieve a single product by its unique ID.",
                        parameters = {
                                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to retrieve.", required = true)
                        },
                        responses = {
                                @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
                                        content = @Content(schema = @Schema(implementation = Product.class))),
                                @ApiResponse(responseCode = "404", description = "Product not found")
                        }
                )
        ),
        @RouterOperation(
                path = "/api/products",
                method = RequestMethod.POST,
                beanClass = ProductHandler.class,
                beanMethod = "createProduct",
                operation = @Operation(
                        summary = "Create a new product",
                        description = "Add a new product to the store.",
                        requestBody = @RequestBody(description = "Product object that needs to be added to the store", required = true,
                                content = @Content(schema = @Schema(implementation = CreateProductRequest.class))),
                        responses = {
                                @ApiResponse(responseCode = "201", description = "Product created successfully",
                                        content = @Content(schema = @Schema(implementation = Product.class))),
                                @ApiResponse(responseCode = "400", description = "Invalid input")
                        }
                )
        ),
        @RouterOperation(
                path = "/api/products/{id}",
                method = RequestMethod.PUT,
                beanClass = ProductHandler.class,
                beanMethod = "updateProduct",
                operation = @Operation(
                        summary = "Update an existing product",
                        description = "Update an existing product by its ID.",
                        parameters = {
                                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to update.", required = true)
                        },
                        requestBody = @RequestBody(description = "Updated product object", required = true,
                                content = @Content(schema = @Schema(implementation = Product.class))),
                        responses = {
                                @ApiResponse(responseCode = "200", description = "Product updated successfully",
                                        content = @Content(schema = @Schema(implementation = Product.class))),
                                @ApiResponse(responseCode = "404", description = "Product not found"),
                                @ApiResponse(responseCode = "400", description = "Invalid input")
                        }
                )
        ),
        @RouterOperation(
                path = "/api/products/{id}",
                method = RequestMethod.DELETE,
                beanClass = ProductHandler.class,
                beanMethod = "deleteProduct",
                operation = @Operation(
                        summary = "Delete a product",
                        description = "Delete a product by its ID.",
                        parameters = {
                                @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to delete.", required = true)
                        },
                        responses = {
                                @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                                @ApiResponse(responseCode = "404", description = "Product not found")
                        }
                )
        )
    })
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        return route(GET("/api/products").and(accept(MediaType.APPLICATION_JSON)), productHandler::getAllProducts)
                .andRoute(GET("/api/products/{id}").and(accept(MediaType.APPLICATION_JSON)), productHandler::getProductById)
                .andRoute(POST("/api/products").and(accept(MediaType.APPLICATION_JSON)), productHandler::createProduct)
                .andRoute(PUT("/api/products/{id}").and(accept(MediaType.APPLICATION_JSON)), productHandler::updateProduct)
                .andRoute(DELETE("/api/products/{id}").and(accept(MediaType.APPLICATION_JSON)), productHandler::deleteProduct);
    }
}
