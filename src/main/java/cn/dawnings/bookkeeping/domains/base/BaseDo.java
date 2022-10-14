package cn.dawnings.bookkeeping.domains.base;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 */
@Data
public abstract class BaseDo implements Serializable {
    @Id
    protected long id;

    @CreatedBy
    @Column("created_by")
    protected Long createdBy;
    @LastModifiedBy
    @Column("modified_by")
    protected Long modifiedBy;

    @CreatedDate
    @Column("created_date")
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column("modified_date")
    protected LocalDateTime modifiedDate;
    @Column("org_id")
    protected Long orgId;
    @Version
    protected Long version;
}
