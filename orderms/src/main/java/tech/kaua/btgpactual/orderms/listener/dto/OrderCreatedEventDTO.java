package tech.kaua.btgpactual.orderms.listener.dto;

import java.util.List;

public record OrderCreatedEventDTO(Long codigoPedido,
                                   Long codigoCliente,
                                   List<OrderItemEventDTO> itens) {
}
