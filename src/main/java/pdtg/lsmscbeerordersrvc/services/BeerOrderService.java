package pdtg.lsmscbeerordersrvc.services;



import org.springframework.data.domain.Pageable;
import pdtg.ls.brewery.model.BeerOrderDto;
import pdtg.ls.brewery.model.BeerOrderPagedList;

import java.util.UUID;

public interface BeerOrderService {
    BeerOrderPagedList listOrders(UUID customerId, Pageable pageable);

    BeerOrderDto placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    BeerOrderDto getOrderById(UUID customerId, UUID orderId);

    void pickupOrder(UUID customerId, UUID orderId);
}
