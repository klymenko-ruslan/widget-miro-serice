package com.miro.widgetservice.controller;

import com.miro.widgetservice.WidgetServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = WidgetServiceApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WidgetControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testGetNotFound() throws Exception {
        mvc.perform(get(WidgetController.BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testGetPositive() throws Exception {
        mvc.perform(post(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 1, \"height\": 1, \"zIndex\": 1}"
        )).andExpect(status().isOk());

        mvc.perform(get(WidgetController.BASE_URI + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreateWidgetWrongValidation() throws Exception {
        mvc.perform(post(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 0, \"height\": 0, \"zIndex\": 1}"
        )).andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWidget() throws Exception {
        mvc.perform(post(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 1, \"height\": 1, \"zIndex\": 1}"
        )).andExpect(status().isOk());

        mvc.perform(put(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"id\": 1, \"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 1, \"height\": 1, \"zIndex\": 1}"
        )).andExpect(status().isOk());
    }

    @Test
    public void testUpdateWidgetNotFound() throws Exception {
        mvc.perform(put(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"id\": -1, \"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 1, \"height\": 1, \"zIndex\": 1}"
        )).andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteWidget() throws Exception {
        mvc.perform(post(WidgetController.BASE_URI).contentType(MediaType.APPLICATION_JSON).content(
                "{\"xCoordinate\": 1, \"yCoordinate\": 1, \"width\": 1, \"height\": 1, \"zIndex\": 1}"
        )).andExpect(status().isOk());
        mvc.perform(delete(WidgetController.BASE_URI + "/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testDeleteWidgetNotFound() throws Exception {
        mvc.perform(delete(WidgetController.BASE_URI + "/-1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}
