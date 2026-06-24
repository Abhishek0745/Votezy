package in.scalive.votezy.repository;

import in.scalive.votezy.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// ✅ MODIFIED FILE: Added findByEmail for JWT authentication
public interface VoterRepository extends JpaRepository<Voter, Long> {
    boolean existsByEmail(String email);
    Optional<Voter> findByEmail(String email); // ✅ NEW - needed for login
}