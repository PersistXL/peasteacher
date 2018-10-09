package com.peas.hsf.http;

/**
 * 媒体类型处理器
 *
 * @author dyh
 */
public interface MediaTypeHandler {
    Client byJson();

    Client byFile();

    Client byXml();

    Client byText();

    Client byOctet();

    Client byForm();

    Client byFormUnEncoded();

    Client by(String by);

}
