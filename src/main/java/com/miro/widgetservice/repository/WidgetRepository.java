package com.miro.widgetservice.repository;

import com.miro.widgetservice.model.Widget;

import java.util.Collection;

public interface WidgetRepository {

    Widget getWidget(Long id);

    Widget createWidget(Widget widget);

    Widget updateWidget(Widget widget);

    Widget deleteWidget(Long id);

    Collection<Widget> findWidgets();

    Collection<Widget> findWidgets(Integer offset, Integer limit, Integer leftX, Integer leftY, Integer rightX, Integer rightY);
}
