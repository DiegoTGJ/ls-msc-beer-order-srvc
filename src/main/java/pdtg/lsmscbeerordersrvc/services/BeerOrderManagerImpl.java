package pdtg.lsmscbeerordersrvc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderEvents;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;
import pdtg.lsmscbeerordersrvc.repositories.BeerOrderRepository;
import reactor.core.publisher.Mono;


/**
 * Created by Diego T. 17-08-2022
 */
@Service
@RequiredArgsConstructor
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEvents> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        sendBeerOrderEvent(savedBeerOrder,BeerOrderEvents.VALIDATE_ORDER);
        return savedBeerOrder;
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEvents event){
        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = build(beerOrder);
        Message<BeerOrderEvents> msg = MessageBuilder.withPayload(event)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
    }


    private StateMachine<BeerOrderStatusEnum, BeerOrderEvents> build(BeerOrder beerOrder){
        StateMachine<BeerOrderStatusEnum, BeerOrderEvents> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
        sm.stopReactively().subscribe();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachineReactively(
                            new DefaultStateMachineContext<>(beerOrder.getOrderStatus(),null,null,null))
                            .subscribe();
                });
        sm.startReactively().subscribe();
        return sm;
    }
}
