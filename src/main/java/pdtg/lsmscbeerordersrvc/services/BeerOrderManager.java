package pdtg.lsmscbeerordersrvc.services;

import org.springframework.transaction.annotation.Transactional;
import pdtg.ls.brewery.model.events.ValidateOrderResult;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;

/**
 * Created by Diego T. 17-08-2022
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    @Transactional
    void validateResult(ValidateOrderResult result);
}
