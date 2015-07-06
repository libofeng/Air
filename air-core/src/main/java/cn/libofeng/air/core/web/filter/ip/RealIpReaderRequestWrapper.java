package cn.libofeng.air.core.web.filter.ip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class RealIpReaderRequestWrapper extends HttpServletRequestWrapper {
    public static final String MARK_COMMA = ",";
    private String ipHeaderName;

    public RealIpReaderRequestWrapper(HttpServletRequest request, String ipHeaderName) {
        super(request);
        this.ipHeaderName = ipHeaderName;
    }

    @Override
    public String getRemoteAddr() {
        // no valid ip header name specified, return default
        if (ipHeaderName == null || ipHeaderName.length() == 0) {
            return super.getRemoteAddr();
        }

        String ip = getHeader(ipHeaderName);
        if (ip != null) {
            int commaIndex = ip.indexOf(MARK_COMMA);
            // multiple ip values set by proxies, get the 1st one.
            // this is based on the format of "client1, proxy1, proxy2,..."
            if (commaIndex > 0) {
                ip = ip.substring(0, commaIndex);
            }
        }

        return ip;
    }
}
