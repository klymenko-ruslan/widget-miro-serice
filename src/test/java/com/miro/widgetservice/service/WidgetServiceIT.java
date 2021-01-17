package com.miro.widgetservice.service;

import com.miro.widgetservice.model.Widget;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WidgetServiceIT {

    @Autowired
    private WidgetService widgetService;

    @Test
    public void testCreateWidget() {
        Widget widgetForCreation = new Widget(0, 0, 1,1);
        Widget createdWidget = widgetService.createWidget(widgetForCreation);
        Assert.assertEquals(widgetForCreation, createdWidget);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWidgetWrongValidation() {
        Widget widgetForCreation = new Widget(0, -1, -1,-1);
        Widget createdWidget = widgetService.createWidget(widgetForCreation);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWidgetNull() {
        widgetService.createWidget(null);
    }

    @Test
    public void testCreateAndFindWidget() {
        Assert.assertNull(widgetService.findWidget(1l));
        Widget widgetForCreation = new Widget(0, 0,1, 1, 0);
        widgetService.createWidget(widgetForCreation);
        Assert.assertEquals(widgetForCreation, widgetService.findWidget(1l));
    }

    @Test
    public void testCreateAndFindWidgetsOrderedByZIndex() {
        Widget[] widgetsForCreation = {new Widget(0, 0,1,1, 2),
                new Widget(0, 0,1,1, 1),
                new Widget(0, 0,1,1, 3)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, 100, null, null, null, null);
        Arrays.sort(widgetsForCreation, Widget.Z_INDEX_COMPARATOR);
        int indx = 0;
        for(Widget currentWidget: widgets) {
            Assert.assertEquals(widgetsForCreation[indx++], currentWidget);
        }
    }

    @Test
    public void testCreateAndFindWidgetsWithDuplicateZIndex() {
        Widget[] widgetsForCreation = {new Widget(4, 4,4,4,2),
                new Widget(1, 1,1,1,1),
                new Widget(5, 5,5,5,3),
                new Widget(3, 3,3,3,2),
                new Widget(2, 2,2,2, 2)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, 100, null, null, null, null);
        int indx = 1;
        for(Widget currentWidget: widgets) {
            Assert.assertEquals(Integer.valueOf(indx), currentWidget.getxCoordinate());
            Assert.assertEquals(Integer.valueOf(indx), currentWidget.getxCoordinate());
            Assert.assertEquals(Integer.valueOf(indx++), currentWidget.getzIndex());
        }
    }

    @Test
    public void testCreateAndFindWidgetsWithNoZIndex() {
        Widget[] widgetsForCreation = {new Widget(1, 1,1,1,2),
                new Widget(2, 2,2,2,5),
                new Widget(3, 3,3,3),
                new Widget(4, 4,4,4)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, 100, null, null, null, null);
        Integer expectedZIndexes[] = {2,5,6,7};
        int indx = 0;
        for(Widget currentWidget: widgets) {
            Assert.assertEquals(expectedZIndexes[indx++], currentWidget.getzIndex());
            Assert.assertEquals(Integer.valueOf(indx), currentWidget.getxCoordinate());
            Assert.assertEquals(Integer.valueOf(indx), currentWidget.getyCoordinate());
        }
    }

    @Test
    public void testUpdateWidget() {
        widgetService.createWidget(new Widget(1, 1,1, 1,1));
        int changedX = 11;
        int changedY = 22;
        widgetService.updateWidget(new Widget(1l,changedX, changedY, 1,1, 1));
        Widget widget = widgetService.findWidget(1l);
        Assert.assertEquals(Integer.valueOf(changedX), widget.getxCoordinate());
        Assert.assertEquals(Integer.valueOf(changedY), widget.getyCoordinate());
    }

    @Test
    public void testUpdateWidgetChangedModificationTime() throws InterruptedException {
        Widget originalWidget = new Widget(1, 1,1, 1,1);
        widgetService.createWidget(originalWidget);
        Long originalModificationTime = originalWidget.getLastModificationTimestamp();
        Thread.sleep(10);
        originalWidget.setId(1l);
        widgetService.updateWidget(originalWidget);
        Widget widget = widgetService.findWidget(1l);
        Assert.assertNotEquals(originalModificationTime, widget.getLastModificationTimestamp());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateWidgetNull() {
        widgetService.updateWidget(null);
    }

    @Test
    public void testDeleteWidget() {
        Assert.assertEquals(0, widgetService.findWidgets(0, 100, null, null, null, null).size());
        widgetService.createWidget(new Widget(1,1,1, 1, 1));
        Assert.assertEquals(1, widgetService.findWidgets(0, 100, null, null, null, null).size());
        widgetService.deleteWidget(1l);
        Assert.assertEquals(0, widgetService.findWidgets(0, 100, null, null, null, null).size());
    }

    @Test
    public void testDeleteWidgetNull() {
        Assert.assertNull(widgetService.deleteWidget(1l));
    }

    @Test
    public void testFindOffsetLimit() {
        Widget[] widgetsForCreation = {new Widget(4, 4,4,4,2),
                new Widget(1, 1,1,1,1),
                new Widget(5, 5,5,5,3),
                new Widget(3, 3,3,3,2),
                new Widget(2, 2,2,2, 2)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, 2, null, null, null, null);

        Assert.assertEquals(2, widgets.size());
    }

    @Test
    public void testFindOffsetLimitDefault() {
        int recordsAmount = 100;
        for(int i = 0; i < recordsAmount; i++) {
            widgetService.createWidget(new Widget(4, 4,4,4,2));
        }
        Collection<Widget> widgets = widgetService.findWidgets(null, recordsAmount, null, null, null, null);

        Assert.assertEquals(20, widgets.size());
    }

    @Test
    public void testFindOffsetLimitMax() {
        Widget[] widgetsForCreation = {new Widget(4, 4,4,4,2),
                new Widget(1, 1,1,1,1),
                new Widget(5, 5,5,5,3),
                new Widget(3, 3,3,3,2),
                new Widget(2, 2,2,2, 2),
                new Widget(4, 4,4,4,2),
                new Widget(1, 1,1,1,1),
                new Widget(5, 5,5,5,3),
                new Widget(3, 3,3,3,2),
                new Widget(2, 2,2,2, 2),
                new Widget(4, 4,4,4,2),
                new Widget(1, 1,1,1,1),
                new Widget(5, 5,5,5,3),
                new Widget(3, 3,3,3,2),
                new Widget(2, 2,2,2, 2)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0 , 10, null, null, null, null);

        Assert.assertEquals(10, widgets.size());
    }

    @Test
    public void testFindFiltering() {
        Widget[] widgetsForCreation = {
                new Widget(0, 25,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),
                new Widget(-1, -1,100,100,3),

                new Widget(50, 50,100,100,3),
                new Widget(0, 0,100,100,2),
                new Widget(0, 50,100,100,1)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, null, 0, 0, 100, 150);
        Assert.assertEquals(3, widgets.size());
        Integer expectedCoordinates[][] = {{0, 50}, {0, 0}, {0, 25}};
        int cnt = 0;
        for(Widget actualWidget: widgets) {
            Assert.assertEquals(expectedCoordinates[cnt][0], actualWidget.getxCoordinate());
            Assert.assertEquals(expectedCoordinates[cnt][1], actualWidget.getyCoordinate());
            cnt++;
        }
    }

    @Test
    public void testFindFilteringMissedParams() {
        Widget[] widgetsForCreation = {
                new Widget(-1, -1,100,100,3),
                new Widget(50, 50,100,100,3),
                new Widget(0, 0,100,100,2),
                new Widget(0, 50,100,100,1)};
        for(Widget currentWidget: widgetsForCreation) {
            widgetService.createWidget(currentWidget);
        }
        Collection<Widget> widgets = widgetService.findWidgets(0, null, 0, null, null, null);
        Assert.assertEquals(widgetsForCreation.length, widgets.size());
    }
}
