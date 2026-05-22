package in.scalive.votezy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResponseDTO {
	private String message;
	private boolean success;
	private Long voterId;
	private Long candidateId;
	
}
