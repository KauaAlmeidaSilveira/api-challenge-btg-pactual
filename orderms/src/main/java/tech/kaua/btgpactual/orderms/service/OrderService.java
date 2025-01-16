package tech.kaua.btgpactual.orderms.service;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import tech.kaua.btgpactual.orderms.controller.dto.OrderResponse;
import tech.kaua.btgpactual.orderms.entity.OrderEntity;
import tech.kaua.btgpactual.orderms.entity.OrderItemEntity;
import tech.kaua.btgpactual.orderms.listener.dto.OrderCreatedEventDTO;
import tech.kaua.btgpactual.orderms.listener.dto.OrderItemEventDTO;
import tech.kaua.btgpactual.orderms.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEventDTO event) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderId(event.codigoPedido());
        orderEntity.setCustomerId(event.codigoCliente());
        orderEntity.setItems(getOrdersItems(event.itens()));
        orderEntity.setTotal(getTotal(event.itens()));
        orderRepository.save(orderEntity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);

        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
        var aggregations = newAggregation(
                match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
        var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

        return new BigDecimal(response.getUniqueMappedResult().get("total").toString()) ;
    }

    private static List<OrderItemEntity> getOrdersItems(List<OrderItemEventDTO> items) {
        return items.stream()
                .map(OrderItemEntity::new)
                .toList();
    }

    private static BigDecimal getTotal(List<OrderItemEventDTO> items) {
        return items.stream()
                .map(item -> item.preco().multiply(BigDecimal.valueOf(item.quantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
