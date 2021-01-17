package com.miro.widgetservice.service;

import com.miro.widgetservice.model.Widget;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WidgetCacheServiceTest {

    @Autowired
    private WidgetCacheService widgetCacheService;

    @Test
    public void testCacheWidget() {
        Assert.assertNull(widgetCacheService.getWidget(1l));
        Widget widget = new Widget(1,1,1,1);
        widget.setId(1l);
        Assert.assertEquals(widget, widgetCacheService.cacheWidget(widget));
    }

    @Test
    public void testCacheAndGetWidget() {
        Assert.assertNull(widgetCacheService.getWidget(2l));
        Widget widget = new Widget(1,1,1,1);
        widget.setId(2l);
        widgetCacheService.cacheWidget(widget);
        Assert.assertEquals(widget, widgetCacheService.getWidget(2l));
    }

    @Test
    public void testCacheGetAndRemoveWidget() {
        Assert.assertNull(widgetCacheService.getWidget(3l));
        Widget widget = new Widget(1,1,1,1);
        widget.setId(3l);
        widgetCacheService.cacheWidget(widget);
        Assert.assertEquals(widget, widgetCacheService.getWidget(3l));
        widgetCacheService.clearWidgetCache(3l);
        Assert.assertEquals(null, widgetCacheService.getWidget(3l));
    }

}
