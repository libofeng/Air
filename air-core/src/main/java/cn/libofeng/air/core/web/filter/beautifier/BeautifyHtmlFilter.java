package cn.libofeng.air.core.web.filter.beautifier;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter that replaces all occurrences of a given string with a
 * replacement.
 * This is an abstract class: you <I>must</I> override the getTargetString
 * and getReplacementString methods in a subclass.
 * The first of these methods specifies the string in the response
 * that should be replaced. The second of these specifies the string
 * that should replace each occurrence of the target string.
 */
public class BeautifyHtmlFilter implements Filter {
    private static final Log log = LogFactory.getLog(BeautifyHtmlFilter.class);

    private static final String CONFIG_ENCODING_NAME = "encoding";
    private static final String CONFIG_CONTENT_TYPE_NAME = "contentType";
    private static final String CONFIG_DEBUG_NAME = "debug";


    private static final String CHAR_NEW_LINE = "\n";
    private static final String JS_LINE_COMMENT_START = "//";
    private static final String SPACER = " ";
    private static final Pattern CHAR_NEW_LINE_PATTERN = Pattern.compile(CHAR_NEW_LINE);
    private static final Pattern SPACERS_PATTERN = Pattern.compile(" +");
    private static final Pattern JS_COMMENT_START_PATTERN = Pattern.compile("<!-- Begin");
    private static final Pattern JS_COMMENT_END_PATTERN = Pattern.compile("//End -->");

    private FilterConfig filterConfig;
    private String defaultEncoding = "utf-8";
    private String contentType = "text/html";
    private Boolean debug = false;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        BeautifyResponseWrapper responseWrapper = new BeautifyResponseWrapper((HttpServletResponse) response);
        // Invoke resource, accumulating output in the wrapper.
        chain.doFilter(request, responseWrapper);
        // Get character array representing output.
        char[] responseChars = responseWrapper.toCharArray();

        boolean beautifyMatched = false;

        String charsetName = defaultEncoding;
        if (responseWrapper.getContentType() != null) {
            HttpContentType httpContentType = new HttpContentType(responseWrapper.getContentType());
            if (httpContentType.getEncoding() != null) {
                charsetName = httpContentType.getEncoding();
            }

            if (debug && log.isDebugEnabled()) {
                log.debug("Response content type:" + httpContentType.getType());
                log.debug("Response content charset:" + httpContentType.getEncoding());
            }

            if (contentType.contains(httpContentType.getType())) {
                if (debug && log.isDebugEnabled()) {
                    log.debug("Requesting URI:" + ((HttpServletRequest) request).getRequestURI());
                }
                beautifyMatched = true;
                responseChars = beautify(responseChars);
            }
        }

        ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bytesStream, charsetName);
        outputStreamWriter.write(responseChars);
        outputStreamWriter.close();

        if (beautifyMatched) {
            // Update the Content-Length header.
            if (debug && log.isDebugEnabled()) {
                log.debug("Set content-length:" + bytesStream.size());
            }
            response.setContentLength(bytesStream.size());
        }

        OutputStream outputStream = response.getOutputStream();
        bytesStream.writeTo(outputStream);
    }

    /**
     * 　　 * Store the FilterConfig object in case subclasses want it.
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        String encodingConfig = filterConfig.getInitParameter(CONFIG_ENCODING_NAME);
        if (encodingConfig != null && encodingConfig.length() > 0) {
            defaultEncoding = encodingConfig;
            if (log.isDebugEnabled()) {
                log.debug("Config encoding:" + defaultEncoding);
            }
        }

        String contentTypeConfig = filterConfig.getInitParameter(CONFIG_CONTENT_TYPE_NAME);
        if (contentTypeConfig != null && contentTypeConfig.length() > 0) {
            contentType = contentTypeConfig;
            if (log.isDebugEnabled()) {
                log.debug("Config content type:" + contentType);
            }
        }

        String debugConfig = filterConfig.getInitParameter(CONFIG_DEBUG_NAME);
        if (debugConfig != null && debugConfig.length() > 0) {
            debug = Boolean.TRUE.toString().equalsIgnoreCase(debugConfig);
            if (log.isDebugEnabled()) {
                log.debug("Config debug:" + debug);
            }
        }
    }

    protected FilterConfig getFilterConfig() {
        return (filterConfig);
    }

    public void destroy() {
    }

    /**
     * remove spacer and new lines in response document
     *
     * @param responseChars response chars
     * @return spacer and new lines filtered content
     */
    private char[] beautify(char[] responseChars) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String response = getFilteredHtml(responseChars);

        StringBuilder htmlBuilder = new StringBuilder();
        String[] lines = CHAR_NEW_LINE_PATTERN.split(response);

        for (String line : lines) {
            if (line.trim().length() <= 0) continue;

            htmlBuilder.append(line);
            if (line.contains(JS_LINE_COMMENT_START)) {
                htmlBuilder.append(CHAR_NEW_LINE);
            }
        }

        stopWatch.stop();
        if (debug && log.isDebugEnabled()) {
            log.debug("Beautify filter time consuming: " + stopWatch.getLastTaskTimeMillis() + "ms");
        }

        return htmlBuilder.toString().toCharArray();
    }

    /**
     * remove spacer and new lines in response document
     *
     * @param responseChars response chars
     * @return spacer and new lines filtered content
     */
    private String getFilteredHtml(char[] responseChars) {
        String response = new String(responseChars);

        Matcher matcher;

        matcher = JS_COMMENT_START_PATTERN.matcher(response);
        response = matcher.replaceAll("");

        matcher = JS_COMMENT_END_PATTERN.matcher(response);
        response = matcher.replaceAll("");

        matcher = SPACERS_PATTERN.matcher(response);
        response = matcher.replaceAll(SPACER);
        return response;
    }
}
