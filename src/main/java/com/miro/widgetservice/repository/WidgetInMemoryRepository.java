package com.miro.widgetservice.repository;

import com.miro.widgetservice.model.Widget;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("memory")
public class WidgetInMemoryRepository implements WidgetRepository {

    @Value(value = "${widget.pagination.maxlimit}")
    private Integer maxLimit;

    @Value(value = "${widget.pagination.defaultlimit}")
    private Integer defaultLimit;

    private List<Widget> widgets = new ArrayList<>();

    @Override
    public Widget getWidget(Long id) {
        for(Widget currentWidget: widgets) {
            if(currentWidget.getId() == id) return currentWidget;
        }
        return null;
    }

    @Override
    public Widget createWidget(Widget widget) {
        List<Widget> widgetsCopy = new ArrayList<>(this.widgets);
        Widget createdWidget = createWidget(widgetsCopy, widget);
        this.widgets = widgetsCopy;
        return createdWidget;
    }

    private Widget createWidget(List<Widget> widgets, Widget widget) {
        widget.setLastModificationTimestamp(System.currentTimeMillis());
        boolean isInserted = false;
        for(int i = 0; i < widgets.size(); i++) {
            if(widget.compareTo(widgets.get(i)) <= 0) {
                widgets.add(i, widget);
                isInserted = true;
                break;
            }
        }
        if(!isInserted) {
            widgets.add(widget);
        }
        return widget;
    }

    @Override
    public Widget updateWidget(Widget widget) {
        int storedWidgetPosition = findWidgetPosition(widget.getId());
        if(storedWidgetPosition == -1) return null;
        List<Widget> widgetsCopy = new ArrayList<>(this.widgets);
        Widget storedWidget = widgetsCopy.get(storedWidgetPosition);
        widgetsCopy.remove(storedWidgetPosition);
        storedWidget.setzIndex(widget.getzIndex());
        storedWidget.setxCoordinate(widget.getxCoordinate());
        storedWidget.setyCoordinate(widget.getyCoordinate());
        storedWidget.setHeight(widget.getHeight());
        storedWidget.setWidth(widget.getWidth());
        createWidget(widgetsCopy, storedWidget);
        this.widgets = widgetsCopy;
        return storedWidget;
    }

    @Override
    public Widget deleteWidget(Long id) {
        int widgetPosition = findWidgetPosition(id);
        if(widgetPosition == -1) return null;
        Widget widget = widgets.get(widgetPosition);
        widgets.remove(widget);
        return widget;
    }

    private int findWidgetPosition(long id) {
        for(int i = 0; i < widgets.size(); i++) {
            if(widgets.get(i).getId() == id) return i;
        }
        return -1;
    }

    @Override
    public Collection<Widget> findWidgets() {
        return widgets;
    }

    @Override
    public Collection<Widget> findWidgets(Integer offset, Integer limit, Integer leftX, Integer leftY, Integer rightX, Integer rightY) {
        Collection<Widget> widgets = this.widgets;
        if(leftX != null && leftY != null && rightX != null && rightY != null) {
            widgets = filterWidgets(leftX, leftY, rightX, rightY);
        }
        return widgets.stream()
                .skip((offset == null || offset < 0) ? 0 : offset)
                .limit((limit == null || limit < 0) ? defaultLimit : limit > maxLimit ? maxLimit : limit)
                .sorted(Widget.Z_INDEX_COMPARATOR)
                .collect(Collectors.toList());
    }

    private Collection<Widget> filterWidgets(Integer leftX, Integer leftY, Integer rightX, Integer rightY) {
        if(widgets.isEmpty()) return Collections.EMPTY_LIST;
        int left = 0;
        int right = widgets.size() - 1;
        while(left <= right) {
            int middle = left + ((right - left) / 2);
            Widget current = widgets.get(middle);
            if(current.getxCoordinate() < leftX) {
                left = middle + 1;
            }
            else if(current.getxCoordinate() + current.getWidth() > rightX) {
                right = middle - 1;
            }
            else {
                break;
            }
        }
        return widgets.subList(left, right + 1).stream()
                      .filter(it -> it.getxCoordinate() >= leftX && it.getxCoordinate() + it.getWidth() <= rightX && it.getyCoordinate() >= leftY && it.getyCoordinate() + it.getHeight() <= rightY)
                      .collect(Collectors.toList());
    }
}
