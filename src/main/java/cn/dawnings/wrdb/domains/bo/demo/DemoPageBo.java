package cn.dawnings.wrdb.domains.bo.demo;

import cn.dawnings.wrdb.domains.base.PageBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DemoPageBo extends PageBo {
    private String nameLikeAll;
}
