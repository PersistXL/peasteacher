package com.peas.hsf.exception;

import com.peas.hsf.model.Message;
import lombok.Getter;

/**
 * 参数校验异常
 *
 * @author dyh on 2015/2/11.
 */
public class CheckerException extends RuntimeException {
    private static final long serialVersionUID = -1475565109300353732L;

    @Getter
    private Message info;

    /**
     * 构造器
     */
    public CheckerException(Message message) {
        super(message.getMessage());
        this.info = message;
    }
}
