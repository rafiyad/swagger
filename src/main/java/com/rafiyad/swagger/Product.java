package com.rafiyad.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product object")
public record Product(
        @Schema(description = "Unique identifier for the product.", example = "1", required = true) String id,
        @Schema(description = "Name of the product.", example = "Laptop", required = true) String name,
        @Schema(description = "Price of the product.", example = "1200.00", required = true) double price) {
}
