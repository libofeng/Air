package cn.libofeng.air.core.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;


public abstract class MessageAwareController extends BaseController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    protected String getMessage(HttpServletRequest request, String code) {
        return messageSource.getMessage(code, null, localeResolver.resolveLocale(request));
    }

    protected String getMessage(HttpServletRequest request, String code, Object[] args) {
        return messageSource.getMessage(code, args, localeResolver.resolveLocale(request));
    }

    protected String getMessage(HttpServletRequest request, String code, Object[] args, String defaultMessage) {
        return messageSource.getMessage(code, args, defaultMessage, localeResolver.resolveLocale(request));
    }
}
