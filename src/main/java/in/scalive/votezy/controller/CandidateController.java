package in.scalive.votezy.controller;

import in.scalive.votezy.dto.CandidateRequestDTO;
import in.scalive.votezy.dto.CandidateResponseDTO;
import in.scalive.votezy.service.CandidateService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ✅ MODIFIED FILE:
// - Now uses CandidateRequestDTO and CandidateResponseDTO
// - Add/Update/Delete protected as ADMIN only
// - GET endpoints are public (configured in SecurityConfig)
@RestController
@RequestMapping("/api/candidate")
@CrossOrigin
@AllArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    // PUBLIC: Anyone can view candidates
    @GetMapping
    public ResponseEntity<List<CandidateResponseDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    // PUBLIC: View single candidate
    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponseDTO> getCandidateById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    // ADMIN only: Add candidate
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponseDTO> addCandidate(@RequestBody @Valid CandidateRequestDTO dto) {
        return new ResponseEntity<>(candidateService.addCandidate(dto), HttpStatus.CREATED);
    }

    // ADMIN only: Update candidate
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CandidateResponseDTO> updateCandidate(
            @PathVariable Long id,
            @RequestBody @Valid CandidateRequestDTO dto) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, dto));
    }

    // ADMIN only: Delete candidate
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.ok("Candidate with ID: " + id + " deleted successfully");
    }
}