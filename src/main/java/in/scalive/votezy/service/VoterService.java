package in.scalive.votezy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.DuplicateResourceException;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VoterService {
    final 	private  VoterRepository voterRepository;
	final  private CandidateRepository candidateRepository;	

	
	public Voter registerVoter(Voter voter) {
		if(voterRepository.existsByEmail(voter.getEmail())) {
			throw new DuplicateResourceException("Voter with email id:"+ voter.getEmail()+" already exists");
		}
		
		return voterRepository.save(voter);
	}
	
	public List<Voter> getAllVoters(){
		return voterRepository.findAll();
	}
	
	public Voter getVoterById(Long id) {
		Voter voter=voterRepository.findById(id).orElse(null);
		if(voter==null) {
			throw new ResourceNotFoundException("Voter with id:"+ id+" not found");
		}
		return voter;
	}
	
	public Voter updateVoter(Long id, Voter updateVoter) {
		Voter voter=voterRepository.findById(id).orElse(null);
		if(voter==null) {
			throw new ResourceNotFoundException("Voter with id:"+ id +" not found");
		}
		
		if(updateVoter.getName()!=null)
		{
		voter.setName(updateVoter.getName());
		}
		if(updateVoter.getEmail()!=null) {
			voter.setEmail(updateVoter.getEmail());
		}
		
		return voterRepository.save(voter);
	}
	
	@Transactional
	public void deleteVoter(Long id) {
		Voter voter=voterRepository.findById(id).orElse(null);
		if(voter==null) {
			throw new ResourceNotFoundException("Cannot delete voter with id :"+ id +" as it does not exist");
		}
		Vote vote=voter.getVote();
		
		if(vote!=null) {
			Candidate candidate=vote.getCandidate();
			candidate.setVoteCount(candidate.getVoteCount()-1);
			candidateRepository.save(candidate);
		}
		voterRepository.delete(voter);
	}
}
