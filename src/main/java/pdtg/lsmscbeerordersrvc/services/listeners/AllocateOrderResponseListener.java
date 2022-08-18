package pdtg.lsmscbeerordersrvc.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import pdtg.ls.brewery.model.events.AllocateOrderResult;
import pdtg.lsmscbeerordersrvc.config.JmsConfig;
import pdtg.lsmscbeerordersrvc.services.BeerOrderManager;

/**
 * Created by Diego T. 18-08-2022
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AllocateOrderResponseListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE)
    public void listen(AllocateOrderResult allocateOrderResult){
        beerOrderManager.processAllocation(allocateOrderResult.getBeerOrderDto(),allocateOrderResult.isAllocationError(),allocateOrderResult.isPendingInventory());
    }

}
