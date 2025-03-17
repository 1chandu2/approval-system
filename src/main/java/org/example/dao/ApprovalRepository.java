package org.example.dao;

import org.example.entity.Approval;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {

    List<Approval> findByTask(Task task);
    boolean existsByTaskAndApprover(Task task, User approver);
}
