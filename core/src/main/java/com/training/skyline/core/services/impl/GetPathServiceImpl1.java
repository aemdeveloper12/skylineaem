package com.training.skyline.core.services.impl;

import com.training.skyline.core.services.GetPathService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceRanking;

@Component(service = GetPathService.class, immediate = true)
@ServiceRanking(value=999)
public class GetPathServiceImpl1 implements GetPathService {

    @Override
    public String getPathOfTheAsset() {
        return "/content/myAsset/Path1";
    }
}
