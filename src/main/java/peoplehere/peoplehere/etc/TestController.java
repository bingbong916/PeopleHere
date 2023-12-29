package peoplehere.peoplehere.etc;

import lombok.RequiredArgsConstructor;
<<<<<<< Updated upstream
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.domain.*;
import peoplehere.peoplehere.controller.dto.tour.TourCreateDto;
import peoplehere.peoplehere.repository.CategoryRepository;
import peoplehere.peoplehere.repository.TourCategoryRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.repository.UserRepository;
import peoplehere.peoplehere.service.TourService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
=======
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.service.TourService;

@Controller
>>>>>>> Stashed changes
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TourService tourService;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TourCategoryRepository tourCategoryRepository;

    @GetMapping("/tours")
    public List<Tour> getTours(@RequestParam String category1, @RequestParam String category2,
                            @RequestParam String category3) {
        List<Category> categoryList = new ArrayList<>();
        Category c1 = categoryRepository.findByName(category1);
        Category c2 = categoryRepository.findByName(category2);
        Category c3 = categoryRepository.findByName(category3);
        categoryList.add(c1);
        categoryList.add(c2);
        categoryList.add(c3);
        List<Tour> toursByCategory = tourService.findAllToursByCategory(categoryList);
        return toursByCategory;
    }

    @PostMapping("/categories/new")
    public void addCategory(@RequestParam String name) {
        categoryRepository.save(new Category(name));
    }

    @GetMapping("/tours/hi")
    public Optional<Tour> test(@RequestParam Long id) {
        return tourRepository.findById(id);
    }

    @PostMapping("/tours/new")
    public String addTours(@RequestBody TourCreateDto tourCreateDto) {
        Tour tour = tourService.createTour(tourCreateDto);
        return "ok";
    }
}
