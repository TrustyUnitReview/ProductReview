package org.products.productreviews.web.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class WebUtil {

    /**
     * Returns the viewName to return for coming back to the sender url
     *
     * @param request Instance of {@link HttpServletRequest} or use an injected instance
     * @return Optional with the view name. Recommended to use an alternative url with
     * {@link Optional#orElse(java.lang.Object)}
     */
    public static Optional<String> getPreviousPageByRequest(HttpServletRequest request)
    {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }
}
