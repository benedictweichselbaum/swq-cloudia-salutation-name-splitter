package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object holding the outgoing parsed and split contact.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO implements DataTransferObject {

    private String letterSalutation;
    private String salutation;
    private String title;
    private String gender;
    private String firstName;
    private String lastName;
}
