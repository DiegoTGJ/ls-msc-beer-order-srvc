package pdtg.ls.brewery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Diego T. 17-08-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateBeerOrderRequest {
    BeerOrderDto beerOrderDto;
}
