package cn.dawnings.bookkeeping.domains.dto.demo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class DemoPageDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    private String name;
}
