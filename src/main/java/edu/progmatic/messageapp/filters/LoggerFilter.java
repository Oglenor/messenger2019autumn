package edu.progmatic.messageapp.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;

@Component
class LoggerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
                throws IOException, ServletException {

            servletRequest.getParameterMap().forEach((k, v) -> logger.info("paramNam: {}, value: {}",k, v));

            //((HttpServletResponse) servletResponse).addHeader("MyHeader", "Header value");

            filterChain.doFilter(servletRequest, servletResponse);
        }


}
