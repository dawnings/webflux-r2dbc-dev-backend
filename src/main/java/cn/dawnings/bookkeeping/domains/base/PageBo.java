package cn.dawnings.bookkeeping.domains.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页请求父类.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBo {
    /**
     * 页码 1 开始.
     */
    @NotNull(message = "页码必须传")
    @Min(0)
    private Integer page;


    /**
     * 页容 .
     */
    @NotNull(message = "每页条数必须传")
    @Min(1)
    private Integer length;

}
