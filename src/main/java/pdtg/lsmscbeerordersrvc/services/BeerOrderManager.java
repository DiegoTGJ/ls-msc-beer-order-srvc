package pdtg.lsmscbeerordersrvc.services;

import pdtg.lsmscbeerordersrvc.domain.BeerOrder;

/**
 * Created by Diego T. 17-08-2022
 */
public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
