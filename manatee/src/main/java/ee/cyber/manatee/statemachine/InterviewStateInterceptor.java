package ee.cyber.manatee.statemachine;


import ee.cyber.manatee.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

import static ee.cyber.manatee.statemachine.InterviewStateMachineImpl.SCHEDULE_ID_HEADER;


@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewStateInterceptor
        extends StateMachineInterceptorAdapter<InterviewState, InterviewEvent> {
    private final ScheduleRepository scheduleRepository;

    @Override
    public void preStateChange(
            State<InterviewState, InterviewEvent> state,
            Message<InterviewEvent> message,
            Transition<InterviewState, InterviewEvent> transition,
            StateMachine<InterviewState, InterviewEvent> stateMachine
    ) {
        Optional.ofNullable(message)
                .flatMap(eventMessage -> Optional.ofNullable(
                        eventMessage.getHeaders()
                                    .get(SCHEDULE_ID_HEADER, Integer.class)))
                .flatMap(scheduleRepository::findById)
                .ifPresentOrElse(scheduleInterview -> {
                    scheduleInterview.setInterviewState(state.getId());
                    scheduleInterview.setUpdatedOn(OffsetDateTime.now());

                    scheduleRepository.save(scheduleInterview);
                }, () -> {
                    log.error("Statemachine preStateChange failed! Possible empty message or "
                                      + "invalid schedule id detected.");
                    throw new IllegalArgumentException("Statemachine preStateChange failed!");
                });
    }
}
