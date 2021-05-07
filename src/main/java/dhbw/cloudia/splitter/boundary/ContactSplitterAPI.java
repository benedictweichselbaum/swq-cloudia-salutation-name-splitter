package dhbw.cloudia.splitter.boundary;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.facade.ContactSalutationSplitterFacade;
import dhbw.cloudia.splitter.control.service.ContactSplitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller class representing the API of the application.
 * Holds method for receiving the contact splitter request.
 */
@RestController
@RequestMapping(value = "/contact")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactSplitterAPI {

    private final ContactSalutationSplitterFacade contactSalutationSplitterFacade;

    /**
     * API method receiving requests for splitting and parsing a contact.
     * route: /contact
     * HTTP-Method: POST
     * @param contactInput Input transfer object holding the contact string
     * @return parsed and split contact in output transfer object
     */
    @PostMapping
    public ResponseEntity<ContactDTO> splitContact(@RequestBody ContactStringInputTO contactInput){
        return ResponseEntity.ok(this.contactSalutationSplitterFacade.parseContactStringInput(contactInput));
    }
}
