package org.yearup.data.mysql;


import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import javax.websocket.SendHandler;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao (DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId){
        String sql = "SELECT * FROM shopping_cart WHERE user_id = ?;";
        ShoppingCart cart = new ShoppingCart();

        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet rows = statement.executeQuery();

            while (rows.next()){
                cart.add(mapRow(rows));
            }


        } catch (Exception e) {
            throw new RuntimeException();
        }
        return cart;

    }
    public void addProduct(int userId, int productId){
        String sql = "INSERT INTO shopping_cart (user_id, product_id, quantity) " +
                "VALUES (?, ?, ?)";
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setInt(2, productId);
            statement.setInt(3, 1);

            statement.executeUpdate();

        }

        catch (Exception e) {
            throw new RuntimeException();
        }
    }



    protected  ShoppingCartItem mapRow (ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        int quantity = row.getInt("quantity");
        ShoppingCartItem item = new ShoppingCartItem();
        item.setQuantity(quantity);
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            ResultSet productRow = statement.executeQuery();

            if (productRow.next()){
                String name = productRow.getString("name");
                BigDecimal price = productRow.getBigDecimal("price");
                int categoryId = productRow.getInt("category_id");
                String description = productRow.getString("description");
                String color = productRow.getString("color");
                int stock = productRow.getInt("stock");
                boolean isFeatured = productRow.getBoolean("featured");
                String imageUrl = productRow.getString("image_url");

                Product product = new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
                item.setProduct(product);
            }

        }
        return item;
    }


}
