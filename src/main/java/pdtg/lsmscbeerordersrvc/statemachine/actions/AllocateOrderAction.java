package pdtg.lsmscbeerordersrvc.statemachine.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import pdtg.ls.brewery.model.events.AllocateOrderRequest;
import pdtg.lsmscbeerordersrvc.config.JmsConfig;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderEvents;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;
import pdtg.lsmscbeerordersrvc.repositories.BeerOrderRepository;
import pdtg.lsmscbeerordersrvc.services.BeerOrderManagerImpl;
import pdtg.lsmscbeerordersrvc.web.mappers.BeerOrderMapper;

import java.util.UUID;

/**
 * Created by Diego T. 17-08-2022
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEvents> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEvents> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findById(UUID.fromString(beerOrderId)).get();

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE,
                AllocateOrderRequest.builder()
                        .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                        .build());
        log.info("Sent Allocation Request for order id: "+beerOrderId);
    }

}
