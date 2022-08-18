package pdtg.lsmscbeerordersrvc.services;

import pdtg.ls.brewery.model.BeerOrderDto;
import pdtg.ls.brewery.model.events.ValidateOrderResult;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;

/**
 * Created by Diego T. 17-08-2022
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void validateResult(ValidateOrderResult result);


    void processAllocation(BeerOrderDto beerOrderDto, boolean allocationError, boolean pendingInventory);
}
