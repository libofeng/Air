/*
 * 版权所有: 版权所有 (C) 2014-2024
 * 公    司: 北京帮付宝网络科技有限公司
 *
 * CoreWebConfig
 *
 * 创建时间: 2015-02-26 09:53
 *
 * 作者: 李柏锋 (工号: AB044566)
 */

package cn.libofeng.air.core.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述:
 * <p>核心的Spring Web配置</p>
 * <p>版本: 1.0.0</p>
 */
@Configuration
@ComponentScan(
        basePackages = "com.ab.bfb.sunnycoffee.core.web.handler",
        excludeFilters = {@ComponentScan.Filter(Configuration.class)})
public class CoreWebConfig {

}
