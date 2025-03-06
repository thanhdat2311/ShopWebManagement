
package com.project.shopapp.response;

import com.project.shopapp.models.Order;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListResponse {
    private List<Order> orders;
    private int TotalPages;
}


