/*
 * 版权所有: 版权所有 (C) 2014-2024
 * 公    司: 北京帮付宝网络科技有限公司
 *
 * MessageHandler
 *
 * 创建时间: 2015-02-26 09:47
 *
 * 作者: 李柏锋 (工号: AB044566)
 */

package cn.libofeng.air.core.web.handler.message;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:
 * <p>获得I18N消息</p>
 * <p>版本: 1.0.0</p>
 */
public interface I18nMessage {
    String get(HttpServletRequest request, String key);

    String get(HttpServletRequest request, String key, Object[] args);

    String get(HttpServletRequest request, String key, Object[] args, String defaultMessage);
}
