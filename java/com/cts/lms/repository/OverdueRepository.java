package com.cts.lms.repository;

import com.cts.lms.entity.Overdue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OverdueRepository extends JpaRepository<Overdue, Long> {

    /**
     * Finds all overdue records for a specific member with a pending fine status.
     *
     * @param memberId the ID of the member
     * @param fineStatus the status of the fine (e.g., "Pending")
     * @return a list of Overdue objects
     */
    List<Overdue> findByMemberIdAndFineStatus(String memberId, String fineStatus);

    Overdue findByMemberIdAndBookIdAndFineStatus(String memberId, Long bookId, String fineStatus);
}
