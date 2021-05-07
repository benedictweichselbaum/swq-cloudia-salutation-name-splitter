package dhbw.cloudia.splitter.boundary;

import dhbw.cloudia.splitter.boundary.dto.ContactInputTO;
import dhbw.cloudia.splitter.boundary.dto.ContactOutputTO;
import dhbw.cloudia.splitter.control.facade.ContactSplitterFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-Controller class representing the API of the application.
 * Holds method for receiving the contact splitter request.
 */
@RestController
@RequestMapping("/contact-splitter")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContactSplitterApi {

    private final ContactSplitterFacade contactSplitterFacade;

    /**
     * API method receiving requests for splitting and parsing a contact.
     * route: /contact-splitter
     * HTTP-Method: POST
     * @param contactInputTO Input transfer object holding the address string
     * @return parsed and split address in output transfer object
     */
    @PostMapping
    public ResponseEntity<ContactOutputTO> parseContact(@RequestBody ContactInputTO contactInputTO) {
        return ResponseEntity.ok(this.contactSplitterFacade.parseAddressToOutputTO(contactInputTO));
    }
}
