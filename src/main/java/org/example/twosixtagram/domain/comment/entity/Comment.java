package org.example.twosixtagram.domain.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="comment")
public class Comment {
    @Id
    private Long id;
}
