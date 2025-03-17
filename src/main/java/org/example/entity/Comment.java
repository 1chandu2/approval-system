package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private User commenter;

    private String message;

    public Comment(String message, Task task, User user) {
        this.message = message;
        this.task = task;
        this.commenter = user;
    }
}
