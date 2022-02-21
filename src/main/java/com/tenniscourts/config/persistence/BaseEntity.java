package com.tenniscourts.config.persistence;

import com.tenniscourts.audit.CustomAuditEntityListener;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(CustomAuditEntityListener.class)
public class BaseEntity<ID> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @Column
    private String ipNumberUpdate;

    @Column
    private Long userCreate;

    @Column
    private Long userUpdate;

    @Column
    @LastModifiedDate
    private LocalDateTime dateUpdate;

    @Column
    private String ipNumberCreate;

    @Column
    @CreatedDate
    private LocalDateTime dateCreate;

}
