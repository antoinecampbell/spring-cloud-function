package com.antoinecampbell.cloud.springcloudfunction.item;

import com.antoinecampbell.cloud.springcloudfunction.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insertItem(Item item) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("item")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", item.getName());
        params.put("description", item.getDescription());
        Number key = jdbcInsert.executeAndReturnKey(params);

        return key.longValue();
    }

    public void updateItem(long id, Item item) {
        jdbcTemplate.update("UPDATE item SET name = ?, description = ? WHERE id = ?",
                item.getName(),
                item.getDescription(),
                id);
    }

    public Item getItem(long id) {
        return jdbcTemplate.queryForObject("SELECT id, name, description FROM item WHERE id = ?",
                new Item.Mapper(),
                id);
    }

    public Page<Item> getAllItems(Page.Params pageParams) {
        Integer total = jdbcTemplate.query("SELECT count(id) AS total FROM item", rs -> {
            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        });
        int totalItems = total == null ? 0 : total;
        long offset = (pageParams.getPage() - 1) * pageParams.getSize();
        List<Item> items = jdbcTemplate.query("SELECT id, name, description FROM item LIMIT ? OFFSET ?",
                new Item.Mapper(),
                pageParams.getSize(),
                offset);

        return new Page<>(items, pageParams.getPage(), pageParams.getSize(), totalItems);
    }

    public void deleteItem(long id) {
        jdbcTemplate.update("DELETE FROM item WHERE id = ?", id);
    }
}

