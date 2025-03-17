package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.constant.TaskStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@Data
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name="user_id", nullable = false)
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<User> approvers;
}
