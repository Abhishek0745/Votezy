package in.scalive.votezy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.VoterService;
import lombok.AllArgsConstructor;

// ✅ MODIFIED FILE:
// - Registration moved to AuthController (/api/auth/register)
// - Admin-only operations protected with @PreAuthorize
@RestController
@RequestMapping("/api/voters")
@CrossOrigin
@AllArgsConstructor
public class VoterController {

    private final VoterService voterService;

    // ADMIN only: Get all voters
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Voter>> getAllVoters() {
        return ResponseEntity.ok(voterService.getAllVoters());
    }
    	

    // ADMIN only: Get voter by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Voter> getVoterById(@PathVariable Long id) {
        return ResponseEntity.ok(voterService.getVoterById(id));
    }

    // ADMIN only: Update voter
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Voter> updateVoter(@PathVariable Long id, @RequestBody Voter voter) {
        return ResponseEntity.ok(voterService.updateVoter(id, voter));
    }

    // ADMIN only: Delete voter
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVoter(@PathVariable Long id) {
        voterService.deleteVoter(id);
        return ResponseEntity.ok("Voter with id: " + id + " deleted successfully");
    }
 // ✅ NEW: Public-safe count endpoint - any logged-in user (ADMIN or VOTER) can access
    @GetMapping("/count")
    public ResponseEntity<java.util.Map<String, Long>> getVoterCounts() {
        List<Voter> voters = voterService.getAllVoters();

        long total = voters.size();
        long voted = voters.stream().filter(Voter::isHasVoted).count();
        long pending = total - voted;

        java.util.Map<String, Long> counts = new java.util.HashMap<>();
        counts.put("total", total);
        counts.put("voted", voted);
        counts.put("pending", pending);

        return ResponseEntity.ok(counts);
    }
    
}