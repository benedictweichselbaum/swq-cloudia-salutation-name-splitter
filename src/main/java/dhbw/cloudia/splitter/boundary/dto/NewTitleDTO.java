package dhbw.cloudia.splitter.boundary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * New title TO that gets transferred from the client
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewTitleDTO {
    private String newTitle;
}
