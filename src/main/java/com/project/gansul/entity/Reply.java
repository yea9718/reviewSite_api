package com.project.gansul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.awt.*;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "reply")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reply extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @Column(length = 30, nullable = false)
    private String createdId;

    @Column(length = 30, nullable = false)
    private String updatedId;

    @Column(nullable = false)
    private TextArea cn;
}
