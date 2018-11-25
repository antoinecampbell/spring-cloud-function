package com.antoinecampbell.cloud.springcloudfunction.item;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@CheckItem
public class Item {

    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    private String name;

    @Size(max = 255)
    private String description;

    public static class Mapper implements RowMapper<Item> {
        @Override
        public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
            Item item = new Item();
            item.id = rs.getLong("id");
            item.name = rs.getString("name");
            item.description = rs.getString("description");

            return item;
        }
    }
}
