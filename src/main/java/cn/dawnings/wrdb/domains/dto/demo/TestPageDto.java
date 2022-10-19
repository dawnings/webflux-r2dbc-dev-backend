package cn.dawnings.wrdb.domains.dto.demo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class TestPageDto implements Serializable {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
}
