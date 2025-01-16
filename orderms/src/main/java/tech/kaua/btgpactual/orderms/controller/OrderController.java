package tech.kaua.btgpactual.orderms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.kaua.btgpactual.orderms.controller.dto.ApiResponse;
import tech.kaua.btgpactual.orderms.controller.dto.OrderResponse;
import tech.kaua.btgpactual.orderms.controller.dto.PaginationResponse;
import tech.kaua.btgpactual.orderms.service.OrderService;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "size", defaultValue = "10") Integer pageSize) {

        Page<OrderResponse> orders = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));

        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);

        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                orders.getContent(),
                PaginationResponse.fromPage(orders)
        ));
    }

}
