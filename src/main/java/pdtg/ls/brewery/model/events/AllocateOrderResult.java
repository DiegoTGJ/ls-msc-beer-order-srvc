package pdtg.ls.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pdtg.ls.brewery.model.BeerOrderDto;

/**
 * Created by Diego T. 18-08-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllocateOrderResult {
    private BeerOrderDto beerOrderDto;
    private boolean allocationError;
    private boolean pendingInventory;
}
