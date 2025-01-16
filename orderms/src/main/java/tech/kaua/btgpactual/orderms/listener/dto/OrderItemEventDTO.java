package tech.kaua.btgpactual.orderms.listener.dto;

import java.math.BigDecimal;

public record OrderItemEventDTO(String produto,
                                Integer quantidade,
                                BigDecimal preco) {
}
