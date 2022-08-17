package pdtg.lsmscbeerordersrvc.domain;

/**
 * Created by Diego T. 17-08-2022
 */
public enum BeerOrderEvents {
    VALIDATE_ORDER, VALIDATION_PASSED, VALIDATION_FAILED,
    ALLOCATION_SUCCESS, ALLOCATION_NO_INVENTORY, ALLOCATION_FAILED,
    BEER_ORDER_PICKED_UP
}
