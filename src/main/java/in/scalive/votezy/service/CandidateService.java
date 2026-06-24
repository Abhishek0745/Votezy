package in.scalive.votezy.service;

import in.scalive.votezy.dto.CandidateRequestDTO;
import in.scalive.votezy.dto.CandidateResponseDTO;
import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// ✅ MODIFIED FILE: Now uses DTOs instead of raw entities
@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;

    // Helper: Convert entity to response DTO
    private CandidateResponseDTO toDTO(Candidate c) {
        return new CandidateResponseDTO(c.getId(), c.getName(), c.getParty(), c.getVoteCount());
    }

    public CandidateResponseDTO addCandidate(CandidateRequestDTO dto) {
        Candidate candidate = new Candidate();
        candidate.setName(dto.getName());
        candidate.setParty(dto.getParty());
        return toDTO(candidateRepository.save(candidate));
    }

    public List<CandidateResponseDTO> getAllCandidates() {
        return candidateRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public CandidateResponseDTO getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + id + " not found"));
        return toDTO(candidate);
    }

    public CandidateResponseDTO updateCandidate(Long id, CandidateRequestDTO dto) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + id + " not found"));

        if (dto.getName() != null) candidate.setName(dto.getName());
        if (dto.getParty() != null) candidate.setParty(dto.getParty());

        return toDTO(candidateRepository.save(candidate));
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Candidate with id: " + id + " not found"));

        List<Vote> votes = candidate.getVote();
        for (Vote v : votes) {
            v.setCandidate(null);
        }
        candidate.getVote().clear();
        candidateRepository.delete(candidate);
    }
}