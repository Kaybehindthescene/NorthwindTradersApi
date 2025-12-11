package com.pluralsight.NorthwindTradersAPI.dao;

import com.pluralsight.NorthwindTradersAPI.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDao implements ProductDao {

    private final DataSource dataSource;

    @Autowired
    public JdbcProductDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Autowired
    public List<Product>getAll(){
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM Products
                """;

        try(Connection conn = dataSource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                Product product = new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getInt("CategoryID"),
                        rs.getDouble("UnitPrice")
                );
                products.add(product);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return products;
    }
    @Override
    public Product getById(int id){
        String sql = """
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM Products
                WHERE ProductID = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    return new Product(
                            resultSet.getInt("ProductID"),
                            resultSet.getString("ProductName"),
                            resultSet.getInt("CategoryID"),
                            resultSet.getDouble("UnitPrice")
                    );
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Product update(Product product) {
        String sql = """
        UPDATE Products
        SET ProductName = ?, CategoryID = ?, UnitPrice = ?
        WHERE ProductID = ?
    """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getCategoryId());
            stmt.setDouble(3, product.getUnitPrice());
            stmt.setInt(4, product.getProductId());

            stmt.executeUpdate();
            return product;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
