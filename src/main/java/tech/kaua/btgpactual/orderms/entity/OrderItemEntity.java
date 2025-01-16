package tech.kaua.btgpactual.orderms.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import tech.kaua.btgpactual.orderms.listener.dto.OrderItemEventDTO;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemEntity {

    private String product;

    private Integer quantity;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    public OrderItemEntity() {
    }

    public OrderItemEntity(String product, Integer quantity, BigDecimal price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItemEntity(OrderItemEventDTO orderItemEventDTO) {
        this.product = orderItemEventDTO.produto();
        this.quantity = orderItemEventDTO.quantidade();
        this.price = orderItemEventDTO.preco();
    }

}
