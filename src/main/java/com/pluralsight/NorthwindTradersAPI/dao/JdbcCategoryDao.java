package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao {

    private final DataSource dataSource;

    @Autowired
    public JdbcCategoryDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> getAll(){
        List<Category> categories = new ArrayList<>();

        String sql = """
                SELECT CategoryID, CategoryName
                FROM Categories
                """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()){
                Category category = new Category(
                        resultSet.getInt("CategoryID"),
                        resultSet.getString("CategoryName")
                );
                categories.add(category);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return categories;
    }
    @Override
    public Category getById(int id){
        String sql = """
                SELECT CategoryID, CategoryName
                FROM Categories
                WHERE CategoryID = ?
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return new Category(
                            resultSet.getInt("CategoryID"),
                            resultSet.getString("CategoryName")
                    );
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
