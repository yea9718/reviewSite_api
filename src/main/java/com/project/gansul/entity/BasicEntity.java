package com.project.gansul.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@DynamicInsert
@MappedSuperclass
public class BasicEntity implements Serializable {
    @Setter
    @Column(updatable = false)
    @JsonIgnore
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @SuppressWarnings("checkstyle:Indentation")
    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();//DateTimeUtil.getUTCTime();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @SuppressWarnings("checkstyle:Indentation")
    @PreUpdate
    private void preUpdate() {
        LocalDateTime now = LocalDateTime.now(); //DateTimeUtil.getUTCTime();
        this.updatedAt = now;
    }
}
