package com.miro.widgetservice.service;

import com.miro.widgetservice.model.Widget;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WidgetCacheService {
    private Map<Long, Widget> widgetCache = new HashMap<>();


    public Widget getWidget(Long id) {
        return widgetCache.get(id);
    }

    public Widget cacheWidget(Widget widget) {
        if(widget == null) return null;
        widgetCache.put(widget.getId(), widget);
        return widget;
    }

    public Widget clearWidgetCache(Long id) {
        return widgetCache.remove(id);
    }
}
