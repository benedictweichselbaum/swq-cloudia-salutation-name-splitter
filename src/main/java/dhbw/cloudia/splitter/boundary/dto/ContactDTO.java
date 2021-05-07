package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private String gender;
    private String address;
    private String salutation;
    private String title;
    private String firstName;
    private String lastName;
}
