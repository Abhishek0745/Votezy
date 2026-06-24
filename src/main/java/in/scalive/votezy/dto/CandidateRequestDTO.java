package in.scalive.votezy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// ✅ NEW FILE: Accept this for add/update instead of raw Candidate entity
@Data
public class CandidateRequestDTO {

    @NotBlank(message = "Candidate name is required")
    private String name;

    @NotBlank(message = "Party name is required")
    private String party;
}