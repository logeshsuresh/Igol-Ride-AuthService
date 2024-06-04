package com.example.IgolAuthService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseModel {

    @Id // this annotation makes the id property a primary key for the table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Identity - auto-increment
    protected Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Format of date object to be stored
    @CreatedDate // This annotation tells spring only handle it object creation
    protected Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Format of date object to be stored
    @LastModifiedDate // This annotation tells spring only handle it for object update
    protected Date updatedAt;

}
