package ee.cyber.manatee.model;


import ee.cyber.manatee.statemachine.InterviewState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInterview {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Integer applicationId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InterviewState interviewState;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Interviewer interviewer;

    @NotNull
    private OffsetDateTime updatedOn;
}
