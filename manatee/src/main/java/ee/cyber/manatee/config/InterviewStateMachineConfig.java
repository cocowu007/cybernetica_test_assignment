package ee.cyber.manatee.config;


import ee.cyber.manatee.statemachine.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;


@Slf4j
@Configuration
@EnableStateMachineFactory
public class InterviewStateMachineConfig
        extends StateMachineConfigurerAdapter<InterviewState, InterviewEvent> {

    @Autowired
    ApplicationStateMachine applicationStateMachine;

    @Override
    public void configure(StateMachineStateConfigurer<InterviewState, InterviewEvent> states)
            throws Exception {
        states.withStates()
                .initial(InterviewState.NEW)
                .states(EnumSet.allOf(InterviewState.class))
                .end(InterviewState.REJECTED)
                .end(InterviewState.ACCEPTED);
    }

    @Override
    public void configure(
            StateMachineTransitionConfigurer<InterviewState, InterviewEvent> transitions)
            throws Exception {
        transitions.withExternal()
               .source(InterviewState.NEW)
               .target(InterviewState.INFORMAL)
               .event(InterviewEvent.SUBMIT)

                .and()
                .withExternal()
                .source(InterviewState.INFORMAL)
                .target(InterviewState.TECHNICAL)
                .event(InterviewEvent.ACCEPT)

               .and()
               .withExternal()
               .source(InterviewState.TECHNICAL)
                .target(InterviewState.BEHAVIOURAL)
                .event(InterviewEvent.ACCEPT)

               .and()
               .withExternal()
               .source(InterviewState.BEHAVIOURAL)
               .target(InterviewState.FINAL)
               .event(InterviewEvent.ACCEPT)

                .and()
                .withExternal()
                .source(InterviewState.FINAL)
                .target(InterviewState.ACCEPTED)
                .event(InterviewEvent.ACCEPT)

                .and()
                .withExternal()
                .source(InterviewState.INFORMAL)
                .target(InterviewState.REJECTED)
                .event(InterviewEvent.REJECT)

                .and()
                .withExternal()
                .source(InterviewState.TECHNICAL)
                .target(InterviewState.REJECTED)
                .event(InterviewEvent.REJECT)

                .and()
                .withExternal()
                .source(InterviewState.BEHAVIOURAL)
                .target(InterviewState.REJECTED)
                .event(InterviewEvent.REJECT)

                .and()
                .withExternal()
                .source(InterviewState.FINAL)
                .target(InterviewState.REJECTED)
                .event(InterviewEvent.REJECT);

    }

    @Override
    public void configure(
            StateMachineConfigurationConfigurer<InterviewState, InterviewEvent> config)
            throws Exception {
        val listenerAdapter =
                new StateMachineListenerAdapter<InterviewState, InterviewEvent>() {
                    @Override
                    public void stateChanged(
                            State<InterviewState, InterviewEvent> from,
                            State<InterviewState, InterviewEvent> to) {
                        log.info("Interview state transition {} -> {}", from, to);
                        if(to.getId() == InterviewState.ACCEPTED) {

                        } else if(to.getId() == InterviewState.REJECTED) {
//                            applicationStateMachine.interview()
                        }
                    }

                    @Override
                    public void eventNotAccepted(Message<InterviewEvent> event) {
                        log.error("Event {} not allowed in current state!", event);
                        throw new IllegalStateException("Event not allowed!");
                    }
                };

        config.withConfiguration().listener(listenerAdapter);
    }
}
