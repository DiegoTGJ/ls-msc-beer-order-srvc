package pdtg.lsmscbeerordersrvc.services.beer;

import pdtg.lsmscbeerordersrvc.services.beer.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Diego T. 06-08-2022
 */
public interface BeerService {
    Optional<BeerDto> getBeerByUpc(String upc);
    Optional<BeerDto> getBeerById(UUID beerId);

}
