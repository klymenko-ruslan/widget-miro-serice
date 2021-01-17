package com.miro.widgetservice.service;

import com.miro.widgetservice.model.Widget;
import com.miro.widgetservice.repository.WidgetRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class WidgetServiceTest {

    @MockBean
    private WidgetRepository widgetRepository;

    @MockBean
    private IdGeneratorService idGeneratorService;

    @MockBean
    private WidgetCacheService widgetCacheService;

    private WidgetService widgetService;

    @Before
    public void setUp() {
        widgetService = new WidgetService(widgetRepository, idGeneratorService, widgetCacheService);
    }

    @Test
    public void testCreateWidgetPositive() {
        Widget widgetForCreation = new Widget(0, 0, 1,1);
        Mockito.when(widgetRepository.createWidget(widgetForCreation))
                .thenReturn(widgetForCreation);
        Mockito.when(widgetCacheService.cacheWidget(widgetForCreation))
                .thenReturn(widgetForCreation);
        Mockito.when(idGeneratorService.getNextId())
                .thenReturn(1l);
        Assert.assertEquals(widgetForCreation, widgetService.createWidget(widgetForCreation));
    }

    @Test(expected = RuntimeException.class)
    public void testCreateWidgeNull() {
        Widget widgetForCreation = null;
        Assert.assertEquals(null, widgetService.createWidget(widgetForCreation));
    }

    @Test
    public void testFindWidgetPositive() {
        Widget widgetForSearch = new Widget(0, 0, 1,1);
        Assert.assertEquals(null, widgetService.findWidget(0l));
        Mockito.when(widgetCacheService.getWidget(0l))
                .thenReturn(widgetForSearch);
        Assert.assertEquals(widgetForSearch, widgetService.findWidget(0l));
    }

    @Test
    public void testFindWidgetNonExisting() {
        Mockito.when(widgetCacheService.getWidget(0l))
                .thenReturn(null);
        Assert.assertEquals(null, widgetService.findWidget(0l));
    }
}
