package peoplehere.peoplehere.etc;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import peoplehere.peoplehere.domain.Category;
import peoplehere.peoplehere.domain.Tour;
import peoplehere.peoplehere.dto.TourInfoDTO;
import peoplehere.peoplehere.service.TourService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TourService testService;

}
