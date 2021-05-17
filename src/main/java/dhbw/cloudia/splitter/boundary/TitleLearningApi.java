package dhbw.cloudia.splitter.boundary;

import dhbw.cloudia.splitter.boundary.dto.NewTitleDTO;
import dhbw.cloudia.splitter.control.service.TitleLearningService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-API controller representing the API for the application part that
 * enables the learning of new titles.
 */
@RestController
@RequestMapping(value = "/title-learning")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TitleLearningApi {

    private final TitleLearningService titleLearningService;

    /**
     * API-Method managing getting a new title that needs to be learned.
     * Delegates task to service.
     * @param newTitleDTO new title in DTO
     * @return HTTP 200 if everything was fine
     */
    @PostMapping
    public ResponseEntity<Void> learnNewTitle(@RequestBody NewTitleDTO newTitleDTO) {
        this.titleLearningService.learnNewTitle(newTitleDTO);
        return ResponseEntity.ok().build();
    }
}
