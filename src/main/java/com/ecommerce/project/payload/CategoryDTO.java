package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    // Can add @Schema(description="this is category id") annotation to provide desc. about particular field for Swagger
    private Long categoryId;

    // Can add @Schema(description="this is category name") annotation to provide desc. about particular field for Swagger
    private String categoryName;
}
