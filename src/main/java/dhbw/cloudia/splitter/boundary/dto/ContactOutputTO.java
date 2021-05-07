package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object holding the outgoing parsed and split address.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactOutputTO {
    private String salutation;
    private String letterSalutation;
    private String title;
    private String sex;
    private String firstName;
    private String surname;
}
