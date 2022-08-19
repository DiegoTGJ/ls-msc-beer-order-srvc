package pdtg.lsmscbeerordersrvc.services.testcomponents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pdtg.ls.brewery.model.events.AllocateOrderRequest;
import pdtg.ls.brewery.model.events.AllocateOrderResult;
import pdtg.lsmscbeerordersrvc.config.JmsConfig;

/**
 * Created by Diego T. 18-08-2022
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderAllocationListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(Message<AllocateOrderRequest> msg){
        AllocateOrderRequest request = msg.getPayload();
        log.info("Received Order Allocation Request for orderId: "+request.getBeerOrderDto().getId());
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE,
                AllocateOrderResult.builder()
                        .allocationError(false)
                        .beerOrderDto(request.getBeerOrderDto())
                        .pendingInventory(false)
                        .build());
    }
}
