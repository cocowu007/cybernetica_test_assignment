package ee.cyber.manatee.statemachine;


import ee.cyber.manatee.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewStateMachineImpl implements InterviewStateMachine {

    public static final String SCHEDULE_ID_HEADER = "schedule_id";

    private final ScheduleRepository scheduleRepository;
    private final StateMachineFactory<InterviewState, InterviewEvent> interviewMachineFactory;
    private final InterviewStateInterceptor interviewStateInterceptor;

    @Override
    public StateMachine<InterviewState, InterviewEvent> rejectInterview(Integer scheduleInterviewId) {
        val stateMachine = build(scheduleInterviewId);
        sendEvent(scheduleInterviewId, stateMachine, InterviewEvent.REJECT);
        return stateMachine;
    }

    @Override
    public StateMachine<InterviewState, InterviewEvent> acceptInterview(Integer scheduleInterviewId) {
        val stateMachine = build(scheduleInterviewId);
        sendEvent(scheduleInterviewId, stateMachine, InterviewEvent.ACCEPT);
        return stateMachine;
    }

    private void sendEvent(Integer scheduleInterviewId,
                           StateMachine<InterviewState, InterviewEvent> stateMachine,
                           InterviewEvent interviewEvent) {
        val message = MessageBuilder
                .withPayload(interviewEvent)
                .setHeader(SCHEDULE_ID_HEADER, scheduleInterviewId)
                .build();
        stateMachine.sendEvent(message);
    }


    private StateMachine<InterviewState, InterviewEvent> build(Integer scheduleInterviewId) {
        val schedule = scheduleRepository
                .findById(scheduleInterviewId)
                .orElseThrow(() -> {
                    log.error("Couldn't find the schedule with given id {}", scheduleInterviewId);
                    throw new IllegalArgumentException("Invalid application id");
                });

        val stateMachine = interviewMachineFactory.getStateMachine(String.valueOf(schedule.getId()));
        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                    .doWithAllRegions(sma -> {
                        sma.addStateMachineInterceptor(interviewStateInterceptor);
                        sma.resetStateMachine(new DefaultStateMachineContext<>(
                                schedule.getInterviewState(), null, null, null));
                    });

        stateMachine.start();
        return stateMachine;
    }
}
