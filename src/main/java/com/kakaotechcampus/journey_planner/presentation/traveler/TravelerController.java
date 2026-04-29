package com.kakaotechcampus.journey_planner.presentation.traveler;

import com.kakaotechcampus.journey_planner.application.message.MessageService;
import com.kakaotechcampus.journey_planner.application.plan.PlanService;
import com.kakaotechcampus.journey_planner.application.traveler.TravelerService;
import com.kakaotechcampus.journey_planner.presentation.traveler.dto.response.TravelerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.kakaotechcampus.journey_planner.domain.message.MessageType.TRAVELER;

@Controller
@RequiredArgsConstructor
@MessageMapping("/plans/{planId}/travelers")
public class TravelerController {

    private final PlanService planService;
    private final MessageService messageService;
    private static final String DESTINATION = "travelers";
    private final TravelerService travelerService;

    @MessageMapping("/init")
    public void initTravelers(@DestinationVariable Long planId) {
        List<TravelerResponse> travelerResponses = planService.getInvitedTravelers(planId);
        messageService.sendInitMessage(TRAVELER, planId, DESTINATION, travelerResponses);
    }

    @MessageMapping("/{travelerId}/delete")
    public void deleteTraveler(@DestinationVariable Long planId, @DestinationVariable Long travelerId) {
        travelerService.deleteTraveler(travelerId);
        messageService.sendDeleteMessage(TRAVELER, planId, DESTINATION, travelerId);
    }
}
