package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Input DTO holding salutation string that gets parsed
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactStringInputTO {
    private String contactString;
}
