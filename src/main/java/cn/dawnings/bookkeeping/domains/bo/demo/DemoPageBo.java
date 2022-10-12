package cn.dawnings.bookkeeping.domains.bo.demo;

import cn.dawnings.bookkeeping.domains.base.PageBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DemoPageBo extends PageBo {
    private String nameLikeAll;
}
