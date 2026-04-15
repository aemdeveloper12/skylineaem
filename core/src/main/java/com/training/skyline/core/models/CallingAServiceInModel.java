package com.training.skyline.core.models;

import com.training.skyline.core.services.GetPathService;
import com.training.skyline.core.services.ReadDummyJson;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class CallingAServiceInModel {
    private String pathOfAsset;
    private String datafromUrl;

    @OSGiService(filter="(component.name=com.training.skyline.core.services.impl.GetPathServiceImpl1)")
    GetPathService getPathService;

    @OSGiService
    ReadDummyJson readDummyJson;

    @PostConstruct
    public void init() throws  Exception{
        pathOfAsset = getPathService.getPathOfTheAsset();
        datafromUrl = readDummyJson.getDatafromDummyJsonApi();
    }
}
