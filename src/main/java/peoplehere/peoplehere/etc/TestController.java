package peoplehere.peoplehere.etc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.domain.TourCategory;
import peoplehere.peoplehere.dto.TourInfoDTO;
import peoplehere.peoplehere.repository.CategoryRepository;
import peoplehere.peoplehere.repository.TourCategoryRepository;
import peoplehere.peoplehere.repository.TourRepository;
import peoplehere.peoplehere.service.TourService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TourService tourService;
    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final TourCategoryRepository tourCategoryRepository;

    @GetMapping("/tours")
    public String getTours(@RequestParam String category1, @RequestParam String category2,
                               @RequestParam String category3) {
        List<Category> categoryList = new ArrayList<>();
//        Category c1 = new Category(category1);
//        Category c2 = new Category(category2);
//        Category c3 = new Category(category3);
//        categoryRepository.save(c1);
//        categoryRepository.save(c2);
//        categoryRepository.save(c3);
        Category c1 = categoryRepository.findByName(category1);
        Category c2 = categoryRepository.findByName(category2);
        Category c3 = categoryRepository.findByName(category3);
        categoryList.add(c1);
        categoryList.add(c2);
        categoryList.add(c3);
        List<Tour> tours = tourService.findAllToursByCategory(categoryList);
        for (Tour tour : tours) {
            System.out.println(tour.getId());
        }
        return "ok";
    }

    @PostMapping("/tours/new")
    public String addTours(@RequestParam String category) {
        Category findCategory = categoryRepository.findByName(category);
        Tour tour = new Tour();
        tourRepository.save(tour);
        TourCategory tourCategory = new TourCategory(tour, findCategory);
        tourCategoryRepository.save(tourCategory);
        return "ok";
    }
}
