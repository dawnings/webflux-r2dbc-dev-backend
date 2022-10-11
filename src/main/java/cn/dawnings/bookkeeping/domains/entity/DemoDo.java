package cn.dawnings.bookkeeping.domains.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_demo")
public class DemoDo {
    @Id
    private Long id;
    private String name;

}
