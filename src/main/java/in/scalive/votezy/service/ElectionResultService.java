package in.scalive.votezy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.ElectionResultRepository;
import in.scalive.votezy.repository.VoteRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ElectionResultService {
	
    final 	private ElectionResultRepository electionResultRepository;
	final   private  VoteRepository voteRepository;
    final 	private  CandidateRepository candidateRepository;
	

	
	public ElectionResult declareElectionResult(String electionName) {
		Optional<ElectionResult> existingResult=this.electionResultRepository.findByElectionName(electionName);
		if(existingResult.isPresent()) {
			return  existingResult.get();
		}
		
		if(voteRepository.count()==0) {
			throw new IllegalStateException("Cannotdeclare the result as no votes have been cased!");
		}
		List<Candidate>allCandidates=candidateRepository.findAllByOrderByVoteCountDesc();
		if(allCandidates.isEmpty()) {
			throw new ResourceNotFoundException("No candidates available");
		}
		
		Candidate winner=allCandidates.get(0);
		int totalVotes=0;
		for(Candidate candidate:allCandidates) {
			totalVotes+=candidate.getVoteCount();
		}
		ElectionResult result=new ElectionResult();
		result.setElectionName(electionName);	
		result.setWinner(winner);
		result.setTotalVotes(totalVotes);
		
		return electionResultRepository.save(result);
	}
	
	public List<ElectionResult> getAllResults(){
		return electionResultRepository.findAll();
	}
}
