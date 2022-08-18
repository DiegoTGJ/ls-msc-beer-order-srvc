package pdtg.lsmscbeerordersrvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderEvents;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;

import java.util.EnumSet;

/**
 * Created by Diego T. 17-08-2022
 */
@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class BeerOderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEvents> {

    private final Action<BeerOrderStatusEnum,BeerOrderEvents> validateOrderAction;
    private final Action<BeerOrderStatusEnum,BeerOrderEvents> allocateOrderAction;

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEvents> states) throws Exception {
        states.withStates()
                .initial(BeerOrderStatusEnum.NEW)
                .states(EnumSet.allOf(BeerOrderStatusEnum.class))
                .end(BeerOrderStatusEnum.DELIVERED)
                .end(BeerOrderStatusEnum.PICKED_UP)
                .end(BeerOrderStatusEnum.DELIVERY_EXCEPTION)
                .end(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                .end(BeerOrderStatusEnum.ALLOCATION_EXCEPTION);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEvents> transitions) throws Exception {
        transitions.withExternal()
                .source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATION_PENDING)
                .event(BeerOrderEvents.VALIDATE_ORDER)
                .action(validateOrderAction)
                .and().withExternal()
                .source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATED)
                .event(BeerOrderEvents.VALIDATION_PASSED)
                .and().withExternal()
                .source(BeerOrderStatusEnum.NEW).target(BeerOrderStatusEnum.VALIDATION_EXCEPTION)
                .event(BeerOrderEvents.VALIDATION_FAILED)
                .and().withExternal()
                .source(BeerOrderStatusEnum.VALIDATED).target(BeerOrderStatusEnum.ALLOCATION_PENDING)
                .event(BeerOrderEvents.VALIDATE_ORDER)
                .action(allocateOrderAction)
                .and().withExternal()
                .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.ALLOCATED)
                .event(BeerOrderEvents.ALLOCATION_SUCCESS)
                .and().withExternal()
                .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.ALLOCATION_EXCEPTION)
                .event(BeerOrderEvents.ALLOCATION_FAILED)
                .and().withExternal()
                .source(BeerOrderStatusEnum.ALLOCATION_PENDING).target(BeerOrderStatusEnum.PENDING_INVENTORY)
                .event(BeerOrderEvents.ALLOCATION_NO_INVENTORY);
    }
}
