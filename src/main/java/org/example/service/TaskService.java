package org.example.service;

import org.example.constant.TaskStatus;
import org.example.dao.ApprovalRepository;
import org.example.dao.CommentRepository;
import org.example.dao.TaskRepository;
import org.example.dao.UserRepository;
import org.example.dto.TaskResponse;
import org.example.dto.UserResponse;
import org.example.entity.Approval;
import org.example.entity.Comment;
import org.example.entity.Task;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmailService emailService;

    public TaskResponse createTask(Task task, List<Long> approverIds, String userName) {
        User creator = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> approvers = userRepository.findAllById(approverIds);
        task.setCreatedBy(creator);
        task.setApprovers(approvers);
        task.setStatus(TaskStatus.PENDING);
        task = taskRepository.save(task);
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());;
        taskResponse.setDescription(task.getDescription());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setApprovers(task.getApprovers().stream().map(approver -> new UserResponse(approver.getId(),
                approver.getFirstName(), approver.getLastName(),
                approver.getEmailId())).toList());

        // Send email to all approvers
        for (User approver : approvers) {
            emailService.sendEmailAsync(approver.getEmailId(), "Task Approval Request", "You have been assigned to approve task: " + task.getTitle());
        }

        return taskResponse;
    }

    public void approveTask(Long taskId, String userName) {
        User approver = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getApprovers().contains(approver)) {
            throw new RuntimeException("User is not an assigned approver for this task");
        }

        boolean alreadyApproved = approvalRepository.existsByTaskAndApprover(task, approver);
        if (alreadyApproved) {
            throw new RuntimeException("User has already approved this task");
        }

        Approval approval = new Approval(task, new User(approver.getId()));
        approvalRepository.save(approval);

        // Notify creator asynchronously
        emailService.sendEmailAsync(task.getCreatedBy().getEmailId(), "Task Approved", "User : " + task.getCreatedBy().getFirstName() + " " + task.getCreatedBy().getFirstName() +  " has approved your task: " + task.getTitle());

        List<Approval> approvals = approvalRepository.findByTask(task);
        if (approvals.size() >= 3) {
            task.setStatus(TaskStatus.APPROVED);
            taskRepository.save(task);

            // Notify all users asynchronously
            String notification = "Task '" + task.getTitle() + "' has been fully approved.";
            emailService.sendEmailAsync(task.getCreatedBy().getEmailId(), "Task Approved", notification);
            for (User approverUser : task.getApprovers()) {
                emailService.sendEmailAsync(approverUser.getEmailId(), "Task Approved", notification);
            }
        }
    }

    public void commentOnTask(String message, Long taskId, String userName) {
        User approver = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getApprovers().contains(approver)) {
            throw new RuntimeException("User is not an assigned approver for this task");
        }

        Comment comment = new Comment(message, task, new User(approver.getId()));
        commentRepository.save(comment);
    }
}
