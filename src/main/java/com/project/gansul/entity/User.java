package com.project.gansul.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String userName;

    @Column(length = 30, nullable = false)
    private String userId;

    @Column(length = 30, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(nullable = false)
    private LocalDateTime birthday;

    @Setter
    @Column(updatable = false)
    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdAt;

    @SuppressWarnings("checkstyle:Indentation")
    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();//DateTimeUtil.getUTCTime();
        this.createdAt = now;
    }
}
