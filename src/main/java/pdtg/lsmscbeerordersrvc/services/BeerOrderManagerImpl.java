package pdtg.lsmscbeerordersrvc.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdtg.ls.brewery.model.BeerOrderDto;
import pdtg.ls.brewery.model.events.ValidateOrderResult;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderEvents;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;
import pdtg.lsmscbeerordersrvc.repositories.BeerOrderRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;


/**
 * Created by Diego T. 17-08-2022
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEvents> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;
    public static final String ORDER_ID_HEADER = "ORDER_ID_HEADER";

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        sendBeerOrderEvent(savedBeerOrder,BeerOrderEvents.VALIDATE_ORDER);
        return savedBeerOrder;
    }

    @Transactional
    @Override
    public void validateResult(ValidateOrderResult result){
        BeerOrder beerOrder = beerOrderRepository.getReferenceById(result.getOrderId());
        if (result.isValid()){
            sendBeerOrderEvent(beerOrder,BeerOrderEvents.VALIDATION_PASSED);
            BeerOrder validatedOrder = beerOrderRepository.getReferenceById(result.getOrderId());
            sendBeerOrderEvent(validatedOrder,BeerOrderEvents.ALLOCATE_ORDER);
        }else {
            sendBeerOrderEvent(beerOrder, BeerOrderEvents.VALIDATION_FAILED);
        }
    }

    @Override
    public void processAllocation(BeerOrderDto beerOrderDto, boolean allocationError, boolean pendingInventory) {
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(beerOrderDto.getId());

        if (!beerOrderOptional.isPresent()){
            log.error("Order with Id: "+beerOrderDto.getId()+" Not Found.");
            return;
        }
        beerOrderOptional.ifPresent(beerOrder -> {
            if (allocationError){
                sendBeerOrderEvent(beerOrder,BeerOrderEvents.ALLOCATION_FAILED);
            } else if (pendingInventory) {
                sendBeerOrderEvent(beerOrder,BeerOrderEvents.ALLOCATION_NO_INVENTORY);
                updateAllocatedQty(beerOrderDto,beerOrder);
            }else {
                sendBeerOrderEvent(beerOrder,BeerOrderEvents.ALLOCATION_SUCCESS);
                updateAllocatedQty(beerOrderDto,beerOrder);
            }
        });
    }

    private void updateAllocatedQty(BeerOrderDto beerOrderDto,BeerOrder beerOrder) {
        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            if (beerOrderLineDto.getId() == beerOrderLine.getId()){
                beerOrderLine.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated());
            }
        }));
        beerOrderRepository.saveAndFlush(beerOrder);
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEvents event){
        StateMachine<BeerOrderStatusEnum,BeerOrderEvents> sm = build(beerOrder);
        Message<BeerOrderEvents> msg = MessageBuilder.withPayload(event)
                .setHeader(ORDER_ID_HEADER,beerOrder.getId().toString())
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
    }


    private StateMachine<BeerOrderStatusEnum, BeerOrderEvents> build(BeerOrder beerOrder){
        StateMachine<BeerOrderStatusEnum, BeerOrderEvents> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
        sm.stopReactively().subscribe();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(beerOrderStateChangeInterceptor);
                    sma.resetStateMachineReactively(
                            new DefaultStateMachineContext<>(beerOrder.getOrderStatus(),null,null,null))
                            .subscribe();

                });
        sm.startReactively().subscribe();
        return sm;
    }
}
