package ee.cyber.manatee.statemachine;


import org.springframework.statemachine.StateMachine;


public interface InterviewStateMachine {

    StateMachine<InterviewState, InterviewEvent> rejectInterview(
            Integer scheduleInterviewId);

    StateMachine<InterviewState, InterviewEvent> acceptInterview(
            Integer scheduleInterviewId);
}
