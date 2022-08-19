package pdtg.lsmscbeerordersrvc.services.testcomponents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import pdtg.ls.brewery.model.events.ValidateBeerOrderRequest;
import pdtg.ls.brewery.model.events.ValidateOrderResult;
import pdtg.lsmscbeerordersrvc.config.JmsConfig;

/**
 * Created by Diego T. 18-08-2022
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(Message<ValidateBeerOrderRequest> msg){
        ValidateBeerOrderRequest request = msg.getPayload();
        log.info("Received Order Validation Request for orderId: "+request.getBeerOrderDto().getId());
        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE,
                ValidateOrderResult.builder()
                        .isValid(true)
                        .orderId(request.getBeerOrderDto().getId())
                        .build());
    }
}
