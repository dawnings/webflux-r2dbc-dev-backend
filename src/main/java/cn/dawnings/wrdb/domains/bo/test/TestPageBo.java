package cn.dawnings.wrdb.domains.bo.test;

import cn.dawnings.wrdb.domains.base.PageBo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TestPageBo extends PageBo {
    private String nameLikeAll;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TestPageBo extends PageBo {
        private String nameLikeAll;
    }
}
