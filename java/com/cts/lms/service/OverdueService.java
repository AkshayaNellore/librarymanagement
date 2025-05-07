package com.cts.lms.service;

import com.cts.lms.entity.Overdue;
import com.cts.lms.repository.OverdueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverdueService {

    @Autowired
    private OverdueRepository overdueRepository;

    /**
     * Fetches the list of pending fines for a specific member.
     *
     * @param memberId the ID of the member
     * @return a list of Overdue objects with pending fines
     * gets fines by member id
     */
    public List<Overdue> getPendingFinesForMember(String memberId) {
        return overdueRepository.findByMemberIdAndFineStatus(memberId, "Pending");
    }


}
