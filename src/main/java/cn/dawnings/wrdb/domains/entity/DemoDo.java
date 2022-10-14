package cn.dawnings.wrdb.domains.entity;

import cn.dawnings.wrdb.domains.base.BaseDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table("t_demo")
public class DemoDo extends BaseDo {
    private String name;

}
