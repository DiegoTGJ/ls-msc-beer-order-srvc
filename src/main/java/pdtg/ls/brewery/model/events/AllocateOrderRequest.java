package pdtg.ls.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pdtg.ls.brewery.model.BeerOrderDto;

/**
 * Created by Diego T. 17-08-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateOrderRequest {
    private BeerOrderDto beerOrderDto;
}
