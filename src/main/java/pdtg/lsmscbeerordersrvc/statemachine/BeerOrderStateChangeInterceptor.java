package pdtg.lsmscbeerordersrvc.statemachine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderEvents;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;
import pdtg.lsmscbeerordersrvc.repositories.BeerOrderRepository;
import pdtg.lsmscbeerordersrvc.services.BeerOrderManagerImpl;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Diego T. 17-08-2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEvents> {
    private final BeerOrderRepository beerOrderRepository;

    @Transactional
    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEvents> state, Message<BeerOrderEvents> message, Transition<BeerOrderStatusEnum, BeerOrderEvents> transition, StateMachine<BeerOrderStatusEnum, BeerOrderEvents> stateMachine, StateMachine<BeerOrderStatusEnum, BeerOrderEvents> rootStateMachine) {
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(BeerOrderManagerImpl.ORDER_ID_HEADER,"")))
                .ifPresent(orderId -> {
                    log.info("Saving state for order id: "+orderId+" Status: "+state.getId());
                    BeerOrder beerOrder = beerOrderRepository.findById(UUID.fromString(orderId)).get();
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.saveAndFlush(beerOrder);
                });
    }
}
