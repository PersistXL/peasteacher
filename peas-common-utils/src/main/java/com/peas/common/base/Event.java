package com.peas.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by peaimage on 2016/5/9.
 */
@Data
@AllArgsConstructor
public class Event {
    /**
     * 事件类型
     */
    private Object eventType;

    /**
     * 事件内容
     */
    private Object eventContent;

    /**
     * 事件来源
     */
    private String eventFrom;
}
