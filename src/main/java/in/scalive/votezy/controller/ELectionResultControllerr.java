package in.scalive.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.dto.ElectionResultRequestDTO;
import in.scalive.votezy.dto.ElectionResultResponseDTO;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.service.ElectionResultService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/election-result")
@CrossOrigin
public class ELectionResultControllerr {
	private ElectionResultService electionResultService;

	@Autowired
	public ELectionResultControllerr(ElectionResultService electionResultService) {
		this.electionResultService = electionResultService;
	}
	
	@PostMapping("/declare")
	public ResponseEntity<ElectionResultResponseDTO> declareElectionResult(@RequestBody @Valid ElectionResultRequestDTO electionResult){
		ElectionResult result=this.electionResultService.declareElectionResult(electionResult.getElectionName());
		ElectionResultResponseDTO responseDTO=new ElectionResultResponseDTO();
		responseDTO.setElectionName(result.getElectionName());
		responseDTO.setTotalVotes(result.getTotalVotes());
		responseDTO.setWinnerId(result.getWinner().getId());
		responseDTO.setWinnerVotes(result.getWinner().getVoteCount());
		return new ResponseEntity<ElectionResultResponseDTO>(responseDTO,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<ElectionResult>> getAllResults(){
		List<ElectionResult>results=this.electionResultService.getAllResults();
		return new ResponseEntity<List<ElectionResult>>(results,HttpStatus.OK);
	}
}
