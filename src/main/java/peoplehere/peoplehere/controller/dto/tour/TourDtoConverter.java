package peoplehere.peoplehere.controller.dto.tour;

import peoplehere.peoplehere.domain.Tour;

public class TourDtoConverter {
    public static Tour postTourRequestToTour(PostTourRequest postTourRequest) {
        Tour tour = new Tour(postTourRequest.getName(), postTourRequest.getBudget(),
                postTourRequest.getTime(), postTourRequest.getImageUrl(),postTourRequest.getContent());
        return tour;

    }

    public static GetTourResponse TourToGetTourResponse(Tour tour) {
        GetTourResponse getTourResponse = new GetTourResponse();
        getTourResponse.setId(tour.getId());
        getTourResponse.setName(tour.getName());
        getTourResponse.setBudget(tour.getBudget());
        getTourResponse.setStartDate(tour.getStartDate());
        getTourResponse.setImageUrl(tour.getImageUrl());
        getTourResponse.setContent(tour.getContent());
        return getTourResponse;
    }
}
