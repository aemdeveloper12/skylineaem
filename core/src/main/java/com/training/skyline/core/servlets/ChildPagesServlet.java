package com.training.skyline.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class ,
          property = {
        "sling.servlet.paths=/bin/childpages",
        "sling.servlet.methods=POST"})
public class ChildPagesServlet extends SlingAllMethodsServlet {

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        String parentPath = request.getParameter("path");

        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        Page parentPage = pageManager.getPage(parentPath);

        if (parentPage == null) {
            response.getWriter().write("Invalid path");
            return;
        }

        try {
            Iterator<Page> children = parentPage.listChildren();

            while (children.hasNext()) {
                Page childPage = children.next();

                Resource contentResource = childPage.getContentResource();

                if (contentResource != null) {
                    ModifiableValueMap properties = contentResource.adaptTo(ModifiableValueMap.class);

                    String title = properties.get("jcr:title", String.class);

                    if (title != null && !title.endsWith("-updated")) {
                        properties.put("jcr:title", title + "-updated");
                    }
                }
            }

            resolver.commit();
            response.getWriter().write("Page titles updated successfully");

        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}


