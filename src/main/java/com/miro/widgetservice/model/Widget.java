package com.miro.widgetservice.model;

import java.util.Comparator;
import java.util.Objects;

public class Widget implements Comparable<Widget>{

    public static final Comparator<Widget> Z_INDEX_COMPARATOR = (a, b) -> a.getzIndex() < b.getzIndex() ? -1 : a.getzIndex() > b.getzIndex() ? 1 : 0;

    private Long id;
    private Integer xCoordinate;
    private Integer yCoordinate;
    private Integer width;
    private Integer height;
    private Long lastModificationTimestamp;
    private Integer zIndex;

    public Widget(){}

    public Widget(Long id, Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, Integer zIndex) {
        this.id = id;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public Widget(Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, Integer zIndex) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public Widget(Integer xCoordinate, Integer yCoordinate, Integer width, Integer height) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Integer xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Integer getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Integer yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Integer getzIndex() {
        return zIndex;
    }

    public void setzIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getLastModificationTimestamp() {
        return lastModificationTimestamp;
    }

    public void setLastModificationTimestamp(Long lastModificationTimestamp) {
        this.lastModificationTimestamp = lastModificationTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Widget widget = (Widget) o;
        return Objects.equals(id, widget.id) &&
                Objects.equals(xCoordinate, widget.xCoordinate) &&
                Objects.equals(yCoordinate, widget.yCoordinate) &&
                Objects.equals(width, widget.width) &&
                Objects.equals(height, widget.height) &&
                Objects.equals(lastModificationTimestamp, widget.lastModificationTimestamp) &&
                Objects.equals(zIndex, widget.zIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xCoordinate, yCoordinate, width, height, lastModificationTimestamp, zIndex);
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", width=" + width +
                ", height=" + height +
                ", lastModificationTimestamp=" + lastModificationTimestamp +
                ", zIndex=" + zIndex +
                '}';
    }

    @Override
    public int compareTo(Widget widget) {
        return this.getxCoordinate() < widget.getxCoordinate() ? -1 : this.getxCoordinate() > widget.getxCoordinate() ? 1 :
                this.getyCoordinate() < widget.getyCoordinate() ? -1 : this.getyCoordinate() > widget.getyCoordinate() ? 1 : 0;
    }
}
