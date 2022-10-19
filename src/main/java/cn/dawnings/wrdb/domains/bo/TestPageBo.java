package cn.dawnings.wrdb.domains.bo;

import cn.dawnings.wrdb.domains.base.PageBo;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class TestPageBo extends PageBo {
    private String nameLikeAll;
}
