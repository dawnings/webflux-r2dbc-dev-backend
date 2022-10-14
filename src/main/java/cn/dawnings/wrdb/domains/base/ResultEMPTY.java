package cn.dawnings.wrdb.domains.base;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 空返回包装.
 */
@Data
@NoArgsConstructor
public class ResultEMPTY {
    /**
     * 错误码，成功：200，失败 500.
     */
    protected Integer code;
    /**
     * 消息
     */
    protected String message;


}
