package dhbw.cloudia.splitter.boundary;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.StringContactInputDTO;
import dhbw.cloudia.splitter.control.SalutationNameParsingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/contact")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SalutationNameSplitterAPI {

    private final SalutationNameParsingService salutationNameParsingService;

    @PostMapping
    public ResponseEntity<ContactDTO> transformContact(@RequestBody StringContactInputDTO contactInput){
        return ResponseEntity.ok(this.salutationNameParsingService.parseContact(contactInput));
    }
}
