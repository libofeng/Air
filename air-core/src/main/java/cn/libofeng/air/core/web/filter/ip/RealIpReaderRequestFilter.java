package cn.libofeng.air.core.web.filter.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <filter>
 * <filter-name>HttpIpModifierFilter</filter-name>
 * <filter-class>com.ab.bfb.web.filter.security.ip.RealIpReaderRequestFilter</filter-class>
 * <init-param>
 * <param-name>ip-header-name</param-name>
 * <param-value>X_FORWARDED_FOR</param-value>
 * </init-param>
 * </filter>
 * <filter-mapping>
 * <filter-name>HttpIpModifierFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 */
public class RealIpReaderRequestFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RealIpReaderRequestFilter.class);

    private static final String CONFIG_IP_HEADER_NAME_KEY = "ip-header-name";

    private String ipHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("==== Init Real Ip Reader Filter ... ====");
        String initIpHeaderName = filterConfig.getInitParameter(CONFIG_IP_HEADER_NAME_KEY);
        if (initIpHeaderName != null && initIpHeaderName.length() > 0) {
            ipHeaderName = initIpHeaderName;
        }
        logger.info("==== Init Real Ip Reader Filter ... ipHeaderName={} ====", ipHeaderName);
        logger.info("==== Init Real Ip Reader Filter done ====");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        RealIpReaderRequestWrapper requestWrapper = new RealIpReaderRequestWrapper(httpServletRequest, ipHeaderName);
        chain.doFilter(requestWrapper, response);
    }

    @Override
    public void destroy() {

    }
}
