package com.react.voteproject.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

    //작성시간
    @CreatedDate
    @Column(nullable = false,updatable = false)
    protected LocalDateTime createdDate;

    //수정시간
    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedDate;
}
