package com.miro.widgetservice.service;

import com.miro.widgetservice.exception.ValidationException;
import com.miro.widgetservice.model.Widget;
import com.miro.widgetservice.repository.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class WidgetService {

    private int maxZIndex = 0;

    private WidgetRepository widgetRepository;

    private IdGeneratorService idGeneratorService;

    private WidgetCacheService widgetCacheService;

    @Autowired
    public WidgetService(WidgetRepository widgetRepository,
                         IdGeneratorService idGeneratorService,
                         WidgetCacheService widgetCacheService) {
        this.widgetRepository = widgetRepository;
        this.idGeneratorService = idGeneratorService;
        this.widgetCacheService = widgetCacheService;
    }

    public Widget findWidget(Long id) {
        return widgetCacheService.getWidget(id);
    }

    public Widget createWidget(Widget widget) {
        if(widget != null) {
            widget.setId(idGeneratorService.getNextId());
        }
        validate(widget);
        updateZIndexes(widget);
        return widgetCacheService.cacheWidget(widgetRepository.createWidget(widget));
    }

    public Widget updateWidget(Widget widget) {
        validate(widget);
        updateZIndexes(widget);
        return widgetCacheService.cacheWidget(widgetRepository.updateWidget(widget));
    }

    public Widget deleteWidget(Long id) {
        widgetRepository.deleteWidget(id);
        return widgetCacheService.clearWidgetCache(id);
    }

    public Collection<Widget> findWidgets(Integer offset, Integer limit,
                                                       Integer leftX, Integer leftY,
                                                       Integer rightX, Integer rightY) {
        return widgetRepository.findWidgets(offset, limit, leftX, leftY, rightX, rightY);
    }

    private synchronized void updateZIndexes(Widget widget) {
        if(widget.getzIndex() == null) {
            widget.setzIndex(++maxZIndex);
        }
        if(widget.getzIndex() > maxZIndex) {
            maxZIndex = widget.getzIndex();
        }
        Collection<Widget> widgets = widgetRepository.findWidgets();
        int prevZIndex = -1;
        for(Widget currentWidget : widgets) {
            if(prevZIndex != -1 && prevZIndex != currentWidget.getzIndex()) {
                break;
            }
            if(prevZIndex != -1 || currentWidget.getzIndex() == widget.getzIndex()) {
                currentWidget.setzIndex(currentWidget.getzIndex() + 1);
                widgetRepository.updateWidget(currentWidget);
                if(currentWidget.getzIndex() > maxZIndex) {
                    maxZIndex = currentWidget.getzIndex();
                }
                prevZIndex = currentWidget.getzIndex();
            }
        }
    }

    private void validate(Widget widget) {
        if(widget == null ||
                widget.getWidth() == null || widget.getWidth() <= 0 ||
                widget.getHeight() == null || widget.getHeight() <= 0 ||
                widget.getxCoordinate() == null || widget.getyCoordinate() == null) {
            throw new ValidationException();
        }
    }
}
