package pdtg.lsmscbeerordersrvc.services.beer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Diego T. 06-08-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerDto implements Serializable {

    static final long serialVersionUID = -7819536086997519795L;

    private String upc;
    private String beerName;
    private String beerStyle;
    private BigDecimal price;
    private UUID beerId;
    private Integer orderQuantity = 0;
}
