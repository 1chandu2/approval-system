package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.constant.TaskStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskResponse {
    private long id;
    private String title;
    private String description;
    private TaskStatus status;
    private List<UserResponse> approvers;
}
