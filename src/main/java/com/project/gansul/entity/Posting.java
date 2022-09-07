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
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "posting")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Posting extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String createId;

    @Column(length = 255, nullable = false)
    private String subject;

    @Column(nullable = false)
    private TextArea cn;

    @Column(length = 30, nullable = false)
    private String updatedId;

    @Column(length = 50, nullable = false)
    private String kindUp;

    @Column(length = 50, nullable = false)
    private String kindDown;

    @Column(length = 10, nullable = false)
    private String horoscope;

    @Column(nullable = false)
    private Long viewCnt;
}
