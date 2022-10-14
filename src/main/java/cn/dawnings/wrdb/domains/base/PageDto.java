package cn.dawnings.wrdb.domains.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.val;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 分页显示包装类.
 *
 * @param <T> the type parameter
 */
@Data
@NoArgsConstructor
@Builder
public class PageDto<T> implements Serializable {
    /**
     * 页码 0 开始.
     */
    @NotNull
    private Integer page;
    /**
     * 页容 .
     */
    @NotNull
    private Integer length;
    /**
     * 总记录数.
     *
     * @DocView.Required
     */
    @JsonProperty("total")
    private Integer total;
    /**
     * 显示总量.
     *
     * @DocView.Required
     */
    @JsonProperty("records")
    private Integer records;
    /**
     * 当页数据.
     *
     * @DocView.Required
     */
    private List<T> data;

    /**
     * Instantiates a new Kzt page.
     *
     * @param page    the display start
     * @param length  the display length
     * @param total   the total records
     * @param records the total display records
     * @param data    the aa data
     */
    public PageDto(Integer page, Integer length, Integer total, Integer records, List<T> data) {
        this.page = page;
        this.length = length;
        this.total = total;
        this.records = records;
        this.data = data;
    }

    /**
     * Of   page.
     *
     * @param <T>     the type parameter
     * @param page    the display start
     * @param length  the display length
     * @param total   the total records
     * @param records the total display records
     * @param data    the aa data
     * @return the kzt page
     */
    public static <T> PageDto<T> of(Integer page, Integer length, Integer total, Integer records, List<T> data) {
        return new PageDto<>(page, length, total, records, data);
    }

    public static <T, Bo extends PageBo> PageDto<T> of(Bo pageBo, List<T> data, Integer total) {
        return new PageDto<>(pageBo == null ? 0 : pageBo.getPage(), pageBo == null ? 0 : pageBo.getLength(), total, data == null ? 0 : data.size(), data);
    }


    public static  <T> PageDto<T> Empty(int total, int records, int page, int length) {
        val objectPageDto = new PageDto<T>();
        objectPageDto.setPage(page);
        objectPageDto.setLength(length);
        objectPageDto.setTotal(total);
        objectPageDto.setRecords(records);
        return objectPageDto;
    }
}
