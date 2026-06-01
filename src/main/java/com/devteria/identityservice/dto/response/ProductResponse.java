package com.devteria.identityservice.dto.response;



import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Long id;
    String name;
    Double price;
    String description;
    String imageUrl;
    Boolean isAvailable;
}