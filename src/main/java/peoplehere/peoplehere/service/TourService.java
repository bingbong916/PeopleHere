package peoplehere.peoplehere.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.dto.TourInfoDTO;
import peoplehere.peoplehere.repository.TourRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TourService {

    private final TourRepository tourRepository;

    @Transactional(readOnly = true)
    public Optional<Tour> findTourById(Long id) {
        Optional<Tour> tour = tourRepository.findById(id);
        return tour;
    }

    @Transactional(readOnly = true)
    public List<Tour> findAllTours() {
        return tourRepository.findAll();
    }

}
