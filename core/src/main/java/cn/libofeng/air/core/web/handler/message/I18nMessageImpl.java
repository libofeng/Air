/*
 * 版权所有: 版权所有 (C) 2014-2024
 * 公    司: 北京帮付宝网络科技有限公司
 *
 * I18nMessageHandlerImpl
 *
 * 创建时间: 2015-02-26 09:50
 *
 * 作者: 李柏锋 (工号: AB044566)
 */

package cn.libofeng.air.core.web.handler.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述:
 * <p>I18n消息处理的默认实现</p>
 * <p>版本: 1.0.0</p>
 */
@Component
public class I18nMessageImpl implements I18nMessage {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public String get(HttpServletRequest request, String key) {
        return messageSource.getMessage(key, null, localeResolver.resolveLocale(request));
    }

    @Override
    public String get(HttpServletRequest request, String key, Object[] args) {
        return messageSource.getMessage(key, args, localeResolver.resolveLocale(request));
    }

    @Override
    public String get(HttpServletRequest request, String key, Object[] args, String defaultMessage) {
        return messageSource.getMessage(key, args, defaultMessage, localeResolver.resolveLocale(request));
    }
}
