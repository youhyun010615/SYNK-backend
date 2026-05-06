package com.synk.backend.repository;

import com.synk.backend.entity.Mission;
import com.synk.backend.entity.Submission;
import com.synk.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByMission(Mission mission);

    Optional<Submission> findByUserAndMission(User user, Mission mission);

    boolean existsByUserAndMission(User user, Mission mission);
}
