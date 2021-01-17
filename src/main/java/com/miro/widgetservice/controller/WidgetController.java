package com.miro.widgetservice.controller;

import com.miro.widgetservice.model.Widget;
import com.miro.widgetservice.service.WidgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(WidgetController.BASE_URI)
public class WidgetController {

    public static final String BASE_URI = "/widgets";

    Logger logger = LoggerFactory.getLogger(WidgetController.class);

    private WidgetService widgetService;

    @Autowired
    public WidgetController(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Widget> findWidget(@PathVariable("id") Long id) {
        logger.info(System.currentTimeMillis() + ", find widget request: " + id);
        Widget widget = widgetService.findWidget(id);
        logger.info(System.currentTimeMillis() + ", find widget response: " + id + ", " + widget);
        if(widget == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(widget, HttpStatus.OK);
    }

    @GetMapping
    public Collection<Widget> findWidgets(@RequestParam(name = "offset", defaultValue = "0", required = false) Integer offset,
                                          @RequestParam(name = "limit", defaultValue = "10", required = false) Integer limit,
                                          @RequestParam(name = "leftX", required = false) Integer leftX,
                                          @RequestParam(name = "leftY", required = false) Integer leftY,
                                          @RequestParam(name = "rightX", required = false) Integer rightX,
                                          @RequestParam(name = "rightY", required = false) Integer rightY) {
        logger.info(System.currentTimeMillis() + ", find widgets request. Offset: " + offset + ", limit: " + limit + ", leftX: " + leftX + ", leftY: " + leftY + ", rightX: " + rightX + ", rightY: " + rightY);
        Collection<Widget> response = widgetService.findWidgets(offset, limit, leftX, leftY, rightX, rightY);
        logger.info(System.currentTimeMillis() + ", find widgets response. Offset: " + offset + ", limit: " + limit + ", leftX: " + leftX + ", leftY: " + leftY + ", rightX: " + rightX + ", rightY: " + rightY + ". " + response);
        return widgetService.findWidgets(offset, limit, leftX, leftY, rightX, rightY);
    }

    @PostMapping
    public Widget createWidget(@RequestBody Widget widget) {
        logger.info(System.currentTimeMillis() + ", create widget request: " + widget);
        Widget widgetResponse = widgetService.createWidget(widget);
        logger.info(System.currentTimeMillis() + ", create widget response: " + widgetResponse);
        return widgetResponse;
    }

    @PutMapping
    public ResponseEntity<Widget> updateWidget(@RequestBody Widget widget) {
        logger.info(System.currentTimeMillis() + ", update widget request: " + widget);
        Widget updatedWidget = widgetService.updateWidget(widget);
        logger.info(System.currentTimeMillis() + ", update widget response: " + updatedWidget);
        if(updatedWidget == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updatedWidget, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Widget> deleteWidget(@PathVariable("id") Long id) {
        logger.info(System.currentTimeMillis() + ", delete widget request: " + id);
        Widget widget = widgetService.deleteWidget(id);
        logger.info(System.currentTimeMillis() + ", delete widget response: " + widget);
        if(widget == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(widget, HttpStatus.OK);
    }
}
