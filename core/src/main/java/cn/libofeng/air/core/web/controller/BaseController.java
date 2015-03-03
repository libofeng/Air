/*
 * <p>版权所有: 版权所有 (C) 2014-2024</p>
 * <p>公    司: 北京帮付宝网络科技有限公司</p>
 *
 * BaseController
 *
 * 创建时间: 2015-01-12 10:03
 *
 * 作者: 李柏锋 (工号: AB044566)
 * 
 * 功能描述: 
 * 基础控制器，包含基本的方法封装
 *
 * 版本: 1.0.0
 */

package cn.libofeng.air.core.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected static final String FORM_ATTR_NAME = "form";
    private static final String REDIRECT_PREFIX = "redirect:";
    private static final String FORWARD_PREFIX = "forward:";


    /**
     * 将请求转发到指定的地址
     *
     * @param path 新的资源地址
     * @return Spring MVC的转发字符窜
     */
    protected String forwardTo(String path) {
        logger.debug("Forward to ... {}", path);
        return FORWARD_PREFIX + path;
    }

    /**
     * 重定向到指定的Url
     *
     * @param url 新的资源Url
     * @return Spring MVC的重定向字符窜
     */
    protected String redirectTo(String url) {
        logger.debug("Redirecting to ... {}", url);
        return REDIRECT_PREFIX + url;
    }
}
