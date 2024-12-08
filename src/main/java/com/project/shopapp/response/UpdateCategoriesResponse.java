package com.project.shopapp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoriesResponse {
    @JsonProperty("message")
    String message;
    @JsonProperty("token")
    String token;
}
