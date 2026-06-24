package in.scalive.votezy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// ✅ NEW FILE: Return this instead of raw Candidate entity
@Data
@AllArgsConstructor
public class CandidateResponseDTO {
    private Long id;
    private String name;
    private String party;
    private int voteCount;
}