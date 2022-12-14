package pdtg.lsmscbeerordersrvc.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pdtg.ls.brewery.model.events.ValidateOrderResult;
import pdtg.lsmscbeerordersrvc.config.JmsConfig;
import pdtg.lsmscbeerordersrvc.services.BeerOrderManager;

/**
 * Created by Diego T. 17-08-2022
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOrderResponseListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESULT_QUEUE)
    public void listen(ValidateOrderResult validateOrderResult){
            log.info("Received validation result for Order Id: "+validateOrderResult.getOrderId());
            beerOrderManager.validateResult(validateOrderResult);
    }
}
