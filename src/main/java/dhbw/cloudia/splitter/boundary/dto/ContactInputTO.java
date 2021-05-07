package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object class holding the incoming address string.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInputTO {
    private String contactInput;
}
