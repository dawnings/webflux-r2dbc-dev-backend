package cn.dawnings.bookkeeping.domains.bo.demo;

import cn.dawnings.bookkeeping.domains.base.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoUpdateBo extends BaseBo {
    private Long id;
    private String name;
}
