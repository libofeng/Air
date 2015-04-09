package cn.libofeng.air.core.web.tags;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class PaginationTag extends TagSupport {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String EQUALS_MARK = "=";
    private static final String QUESTION_MARK = "?";
    private static final String AND_MARK = "&";
    private static final String SLASH_MARK = "/";

    private String url;
    private String paramName;
    private int pageNo;
    private int totalPages;
    private int numberButtons = 10;

    // button/link text
    private String first = "|&lt;";
    private String prev = "&lt;";
    private String next = "&gt;";
    private String last = "&gt;|";

    private String ellipsis = "...";


    @Override
    public int doStartTag() throws JspException {
        // data validation
        if (numberButtons < 0) {
            logger.warn("numberButtons:{} is less than 0, reset it to 0.", numberButtons);
            numberButtons = 0;
        }
        if (totalPages < 1) {
            logger.warn("totalPages:{} is less than 1, reset it to 1.", totalPages);
            totalPages = 1;
        }
        if (pageNo < 1) {
            logger.warn("pageNo:{} is less than 1, reset it to 1.", pageNo);
            pageNo = 1;
        } else if (pageNo > totalPages) {
            logger.warn("pageNo:{} is greater than totalPages, reset it to totalPages", pageNo);
            pageNo = totalPages;
        }

        StringBuilder stringBuilder = new StringBuilder();

        // build pagination html
        stringBuilder.append("<div class=\"pagination\"> ");

        stringBuilder.append(buildFirstButton());
        stringBuilder.append(buildPreviousButton());

        // dynamical number buttons
        buildNumberButtons(stringBuilder);

        stringBuilder.append(buildNextButton());
        stringBuilder.append(buildLastButton());

        stringBuilder.append("</div>");

        JspWriter out = pageContext.getOut();
        try {
            out.print(stringBuilder);
        } catch (IOException e) {
            logger.error("Error when output pagination tag.", e);
        }


        return EVAL_PAGE;
    }

    /**
     * build Url for buttons
     *
     * @param no page NO.
     * @return page Url
     */
    private String buildUrl(int no) {
        logger.debug("buildUrl  ... ");
        String fullUrl;
        if (paramName == null || paramName.length() == 0) {
            logger.debug("no paramName, build restful url.");
            // /aa/bb/cc/{pageNo}
            if (url.endsWith(SLASH_MARK)) {
                fullUrl = url + no;
            } else {
                fullUrl = url + SLASH_MARK + no;
            }
        } else if (url.contains(QUESTION_MARK)) {
            fullUrl = url + AND_MARK + paramName + EQUALS_MARK + no;
        } else {
            fullUrl = url + QUESTION_MARK + paramName + EQUALS_MARK + no;
        }

        logger.debug("url built:{}.", fullUrl);

        return fullUrl;
    }

    /**
     * build number button html
     *
     * @param no page NO.
     * @return button html
     */
    private String buildNumberButton(int no) {
        logger.debug("buildNumberButton  ... ");
        if (no == pageNo) {
            logger.debug("pageNo:{} is current page, show current button ", no);
            return "<span class=\"button no current\">" + no + "</span>";
        } else {
            logger.debug("show page button of pageNo::{}", no);
            return "<a class=\"button no\" href=\"" + buildUrl(no) + "\">" + no + "</a>";
        }
    }

    /**
     * build number buttons based on pageNo and totalPages.
     *
     * @param stringBuilder StringBuilder
     */
    private void buildNumberButtons(StringBuilder stringBuilder) {
        logger.debug("buildNumberButtons  ... ");
        if (numberButtons == 0) {
            logger.debug("numberButtons=0, ignore number buttons.");
            return;
        }

        if (numberButtons >= totalPages) {
            // number button count more than total page count.
            logger.debug("numberButtons>=totalPages, show all page number buttons.");
            for (int i = 1; i <= totalPages; i++) {
                stringBuilder.append(buildNumberButton(i));
            }
        } else {
            int startNo, endNo;
            int halfButtons = numberButtons / 2;

            if ((pageNo - halfButtons > 0) && (pageNo + halfButtons <= totalPages)) {
                // there is enough pages for number buttons
                startNo = pageNo - halfButtons;
                endNo = startNo + numberButtons - 1;

            } else if (pageNo - halfButtons <= 0) {
                startNo = 1;
                endNo = startNo + numberButtons - 1;
            } else {
                endNo = totalPages;
                startNo = endNo - numberButtons + 1;
            }

            // build button and ellipsis
            if (startNo > 1 && ellipsis != null && ellipsis.length() > 0) {
                logger.debug("show prefix ellipsis:{}", ellipsis);
                stringBuilder.append("<span class=\"ellipsis\">").append(ellipsis).append("</span>");
            }
            for (int i = startNo; i <= endNo; i++) {
                stringBuilder.append(buildNumberButton(i));
            }
            if (endNo < totalPages && ellipsis != null && ellipsis.length() > 0) {
                logger.debug("show suffix ellipsis:{}", ellipsis);
                stringBuilder.append("<span class=\"ellipsis\">").append(ellipsis).append("</span>");
            }
        }
    }

    /**
     * build First button html
     *
     * @return button html
     */
    private String buildFirstButton() {
        logger.debug("buildFirstButton ... ");
        if (first == null || first.length() == 0) {
            logger.debug("no first button text, ignored. first:{}", first);
            return "";
        }

        if (pageNo == 1) {
            logger.debug("the current page is first page, disable first button. first:{}", first);
            return "<span class=\"button first disabled\">" + first + "</span>";
        } else {
            logger.debug("show first button. first:{}", first);
            return "<a class=\"button first\" href=\"" + buildUrl(1) + "\">" + first + "</a>";
        }
    }

    /**
     * build Last button html
     *
     * @return button html
     */
    private String buildLastButton() {
        logger.debug("buildLastButton ... ");
        if (last == null || last.length() == 0) {
            logger.debug("no last button text, ignored. last:{}", last);
            return "";
        }

        if (pageNo == totalPages) {
            logger.debug("the current page is last page, disable last button. first:{}", last);
            return "<span class=\"button last disabled\">" + last + "</span>";
        } else {
            logger.debug("show last button. last:{}", last);
            return "<a class=\"button last\" href=\"" + buildUrl(totalPages) + "\">" + last + "</a>";
        }
    }

    /**
     * build Previous button html
     *
     * @return button html
     */
    private String buildPreviousButton() {
        logger.debug("buildPreviousButton ... ");
        if (prev == null || prev.length() == 0) {
            logger.debug("no previous button text, ignored. prev:{}", prev);
            return "";
        }

        int no = pageNo - 1;
        if (no <= 0 || no > totalPages) {
            logger.debug("no valid previous page, disable previous button. prev:{}", prev);
            return "<span class=\"button prev disabled\">" + prev + "</span>";
        } else {
            logger.debug("show previous button. prev:{}", prev);
            return "<a class=\"button prev\" href=\"" + buildUrl(no) + "\">" + prev + "</a>";
        }
    }

    /**
     * build Next button html
     *
     * @return button html
     */
    private String buildNextButton() {
        logger.debug("buildNextButton ... ");
        if (next == null || next.length() == 0) {
            logger.debug("no next button text, ignored. next:{}", next);
            return "";
        }

        int no = pageNo + 1;
        if (no <= 0 || no > totalPages) {
            logger.debug("no valid next page, disable next button. next:{}", next);
            return "<span class=\"button next disabled\">" + next + "</span>";
        } else {
            logger.debug("show next button. next:{}", next);
            return "<a class=\"button next\" href=\"" + buildUrl(no) + "\">" + next + "</a>";
        }
    }

    // setters
    public void setUrl(String url) {
        this.url = url;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setNumberButtons(int numberButtons) {
        this.numberButtons = numberButtons;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setEllipsis(String ellipsis) {
        this.ellipsis = ellipsis;
    }
}
