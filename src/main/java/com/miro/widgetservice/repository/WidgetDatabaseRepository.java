package com.miro.widgetservice.repository;

import com.miro.widgetservice.model.Widget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
@Profile("database")
public class WidgetDatabaseRepository implements WidgetRepository {

    @Value(value = "${widget.pagination.maxlimit}")
    private Integer maxLimit;

    @Value(value = "${widget.pagination.defaultlimit}")
    private Integer defaultLimit;

    private JdbcTemplate jdbcTemplate;

    private final String GET_BY_ID = "SELECT * FROM widget WHERE id = {id};";

    private final String CREATE = "INSERT INTO widget(id, xCoordinate, yCoordinate, width, height, lastModificationTimestamp, zIndex) " +
            "VALUES({id}, {xCoordinate}, {yCoordinate}, {width}, {height}, {lastModificationTimestamp}, {zIndex});";

    private final String UPDATE = "UPDATE WIDGET SET xCoordinate = {xCoordinate}, yCoordinate = {yCoordinate}, width = {width}, height = {height}, lastModificationTimestamp = {lastModificationTimestamp}, zIndex = {zIndex} WHERE id = {id};";

    private final String DELETE = "DELETE widget WHERE id = {id};";

    private final String SELECT_ALL = "SELECT * FROM widget ORDER BY zIndex;";

    @Autowired
    public WidgetDatabaseRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Widget getWidget(Long id) {
        List<Widget> result = jdbcTemplate.query(GET_BY_ID.replace("{id}", id.toString()), new WidgetMapper());
        if(result.isEmpty()) return null;
        return result.get(0);
    }

    @Override
    public Widget createWidget(Widget widget) {
        widget.setLastModificationTimestamp(System.currentTimeMillis());
        jdbcTemplate.execute(CREATE.replace("{id}", widget.getId().toString())
                .replace("{xCoordinate}", widget.getxCoordinate().toString())
                .replace("{yCoordinate}", widget.getyCoordinate().toString())
                .replace("{width}", widget.getWidth().toString())
                .replace("{height}", widget.getHeight().toString())
                .replace("{lastModificationTimestamp}", widget.getLastModificationTimestamp().toString())
                .replace("{zIndex}", widget.getzIndex().toString()));
        return widget;
    }

    @Override
    public Widget updateWidget(Widget widget) {
        widget.setLastModificationTimestamp(System.currentTimeMillis());
        int result = jdbcTemplate.update(UPDATE.replace("{id}", widget.getId().toString())
                .replace("{xCoordinate}", widget.getxCoordinate().toString())
                .replace("{yCoordinate}", widget.getyCoordinate().toString())
                .replace("{width}", widget.getWidth().toString())
                .replace("{height}", widget.getHeight().toString())
                .replace("{lastModificationTimestamp}", widget.getLastModificationTimestamp().toString())
                .replace("{zIndex}", widget.getzIndex().toString()));
        return result == 0 ? null : widget;
    }

    @Override
    public Widget deleteWidget(Long id) {
        Widget widget = getWidget(id);
        if(widget != null)
            jdbcTemplate.execute(DELETE.replace("{id}", id.toString()));
        return widget;
    }

    @Override
    public Collection<Widget> findWidgets() {
        return jdbcTemplate.query(SELECT_ALL, new WidgetMapper());
    }

    @Override
    public Collection<Widget> findWidgets(Integer offset, Integer limit, Integer leftX, Integer leftY, Integer rightX, Integer rightY) {
        StringBuilder query = new StringBuilder("SELECT * FROM widget ");
        if(leftX != null && leftY != null && rightX != null && rightY != null) {
            query.append("WHERE xCoordinate >= " + leftX)
                 .append(" AND xCoordinate + width <= " + rightX)
                 .append(" AND yCoordinate >= " + leftY)
                 .append(" AND yCoordinate + height <= " + rightY)
                 .append(" ");
        }
        query.append("ORDER BY zIndex ");
        query.append("LIMIT " + ((limit == null || limit < 0) ? defaultLimit : limit > maxLimit ? maxLimit : limit))
             .append(" OFFSET " + ((offset == null || offset < 0) ? 0 : offset))
             .append(";");
        return jdbcTemplate.query(query.toString(), new WidgetMapper());
    }

    class WidgetMapper implements RowMapper<Widget> {
        public Widget mapRow(ResultSet rs, int rowNum) throws SQLException {
            Widget widget = new Widget();
            widget.setId(rs.getLong("id"));
            widget.setxCoordinate(rs.getInt("xCoordinate"));
            widget.setyCoordinate(rs.getInt("yCoordinate"));
            widget.setHeight(rs.getInt("height"));
            widget.setWidth(rs.getInt("width"));
            widget.setzIndex(rs.getInt("zIndex"));
            widget.setLastModificationTimestamp(rs.getLong("lastModificationTimestamp"));
            return widget;
        }
    }
}
