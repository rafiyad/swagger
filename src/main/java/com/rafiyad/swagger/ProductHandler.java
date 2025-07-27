package com.rafiyad.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Tag(name = "Product API", description = "Operations related to products")
public class ProductHandler {

    private final ConcurrentMap<String, Product> products = new ConcurrentHashMap<>();

    public ProductHandler() {
        // Initialize with some data
        products.put("1", new Product("1", "Laptop", 1200.00));
        products.put("2", new Product("2", "Mouse", 25.00));
        products.put("3", new Product("3", "Keyboard", 75.00));
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all available products.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Product.class)))
            })
    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(products.values());
    }

    @Operation(summary = "Get a product by ID", description = "Retrieve a single product by its unique ID.",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to retrieve.", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        Product product = products.get(id);
        if (product != null) {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(product);
        } else {
            return ServerResponse.notFound().build();
        }
    }

    @Operation(summary = "Create a new product", description = "Add a new product to the store.",
            requestBody = @RequestBody(description = "Product object that needs to be added to the store", required = true,
                    content = @Content(schema = @Schema(implementation = CreateProductRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public Mono<ServerResponse> createProduct(ServerRequest request) {
        return request.bodyToMono(CreateProductRequest.class)
                .flatMap(productRequest -> {
                    String id = UUID.randomUUID().toString();
                    Product newProduct = new Product(id, productRequest.name(), productRequest.price());
                    products.put(id, newProduct);
                    return ServerResponse.created(URI.create("/api/products/" + id))
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(newProduct);
                });
    }

    @Operation(summary = "Update an existing product", description = "Update an existing product by its ID.",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to update.", required = true, schema = @Schema(type = "string"))
            },
            requestBody = @RequestBody(description = "Updated product object", required = true,
                    content = @Content(schema = @Schema(implementation = Product.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        if (!products.containsKey(id)) {
            return ServerResponse.notFound().build();
        }
        return request.bodyToMono(Product.class)
                .flatMap(updatedProduct -> {
                    products.put(id, updatedProduct);
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(updatedProduct);
                });
    }

    @Operation(summary = "Delete a product", description = "Delete a product by its ID.",
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the product to delete.", required = true, schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Product not found")
            })
    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        String id = request.pathVariable("id");
        if (products.remove(id) != null) {
            return ServerResponse.noContent().build();
        } else {
            return ServerResponse.notFound().build();
        }
    }
}
