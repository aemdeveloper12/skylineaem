package com.training.skyline.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Getter
@Model(adaptables= Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ScratchImageWithTextModel {

    @ValueMapValue
    @Required
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String toplefttext;

    @ValueMapValue
    private String bottomlefttext;

    @ValueMapValue
    private String centered;

    @ValueMapValue
    private String toprighttext;

    @ValueMapValue
    private String bottomrighttext;
}
