package cn.dawnings.bookkeeping.domains.bo.demo;

import cn.dawnings.bookkeeping.domains.base.BaseBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DemoAddBo extends BaseBo {
    private String name;
}
