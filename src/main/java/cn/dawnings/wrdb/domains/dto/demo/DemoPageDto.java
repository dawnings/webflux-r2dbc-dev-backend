package cn.dawnings.wrdb.domains.dto.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DemoPageDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    protected Long createdBy;
    protected Long modifiedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createdDate = LocalDateTime.now();
    //        @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime modifiedDate = LocalDateTime.now();
    @JsonSerialize(using = ToStringSerializer.class)
    protected Long orgId;
    @JsonSerialize(using = ToStringSerializer.class)
    protected Long version;
}
