package cn.libofeng.air.core;

import java.nio.charset.Charset;

/**
 * 功能描述:
 * <p>核心常量类，存放框架级别的常量数据</p>
 * <p>版本: 1.0.0</p>
 */
public final class Constants {
    public static final String DEFAULT_CHARSET_NAME = "utf-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);

    public static final String SCHEME_HTTP = "HTTP";
}
