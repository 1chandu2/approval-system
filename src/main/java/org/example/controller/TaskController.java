package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.JwtUtil;
import org.example.dto.ApproveResponse;
import org.example.dto.CommentRequest;
import org.example.dto.CommentResponse;
import org.example.dto.TaskResponse;
import org.example.entity.Task;
import org.example.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(path = "/", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> createTask(@RequestBody Task task, @RequestParam(name = "approverIds") List<Long> approverIds, @RequestHeader("Authorization") String token) {
        String userName = getUserNameFromToken(token);
        TaskResponse taskResponse = taskService.createTask(task, approverIds, userName);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @RequestMapping(path = "/approve", method = RequestMethod.POST)
    public ResponseEntity<ApproveResponse> approveTask(@RequestParam(name = "taskId") Long taskId, @RequestHeader("Authorization") String token) {
        String userName = getUserNameFromToken(token);

        taskService.approveTask(taskId, userName);
        return ResponseEntity.status(HttpStatus.OK).body(new ApproveResponse(taskId, "You have successfully approved the task"));
    }

    @RequestMapping(path = "/comment", method = RequestMethod.POST)
    public ResponseEntity<CommentResponse> commentOnTask(@Valid @RequestBody CommentRequest commentRequest,
                                                         @RequestParam(name = "taskId") Long taskId,
                                                         @RequestHeader("Authorization") String token) {
        String userName = getUserNameFromToken(token);

        taskService.commentOnTask(commentRequest.getMessage(), taskId, userName);
        return ResponseEntity.status(HttpStatus.OK).body(new CommentResponse(taskId,
                "You have successfully commented on task"));
    }

    private String getUserNameFromToken(String token) {
        String jwt = token.substring(7);
        return jwtUtil.ExtractUserName(jwt);
    }
}
