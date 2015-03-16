package cn.libofeng.air.core.monitor;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.util.StopWatch;


/**
 * Created by libofeng on 3/15/15.
 * slow performance monitor
 */
public class PoorPerformanceMonitor extends PerformanceMonitorInterceptor {
    private static final String LOG_LABEL = "[PoorPerformance]";
    private long whatIsSlow = 100;

    public void setWhatIsSlow(long whatIsSlow) {
        this.whatIsSlow = whatIsSlow;
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        String name = createInvocationTraceName(invocation);
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);
        try {
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            if (whatIsSlow > stopWatch.getTotalTimeMillis()) {
                logger.trace(stopWatch.shortSummary());
            } else {
                logger.warn(LOG_LABEL + stopWatch.shortSummary());
            }
        }
    }
}
