package com.training.skyline.core.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Title;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;

@Model(
        adaptables = {Resource.class, SlingHttpServletRequest.class},
        adapters = CustomTitle.class,
        resourceType = "skyline/components/customtitle",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name="jackson",extensions="json")
public class CustomTitleImpl implements CustomTitle {

    @Self
    @Via(type = ResourceSuperType.class)
    private Title coreTitle; //Inject Core Title Sling Model

    @ValueMapValue
    private String path; // our custom dialog path field


    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getText() {
        return coreTitle != null ? coreTitle.getText() : null;
    }

    @Override
    public String getType() {
        return coreTitle != null ? coreTitle.getType() : null;
    }

    @Override
    public  Link getLink() {
        return coreTitle != null ? coreTitle.getLink() : null;
    }

    @Override
    public String getLinkURL() {
        return coreTitle != null ? coreTitle.getLinkURL() : null;
    }

    @Override
    public boolean isLinkDisabled() {
        return coreTitle != null ? coreTitle.isLinkDisabled() : null;
    }
}

