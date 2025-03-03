package org.example;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

public class ProductRepository {

    public Collection <Product> getAll(){
        Connection connection = DbConnection.getConnection();
        Collection<Product> products = new LinkedList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM products");
        ) {
            while (rs.next()) {
                products.add(mapRowProducts(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    private Product mapRowProducts (ResultSet rs) throws SQLException{
        Long article = rs.getLong("article");
        String prodName = rs.getString("prodname");
        String color = rs.getString("color");
        int price = rs.getInt("price");
        int inStock = rs.getInt("instock");
        Product product = new Product();
        product.setArticle(article);
        product.setProdName(prodName);
        product.setColor(color);
        product.setPrice(price);
        product.setInStock(inStock);
        return product;
    }
    private Product mapProductsList (ResultSet rs) throws SQLException{
        Long article = rs.getLong("article");
        String prodName = rs.getString("prodname");
        String color = rs.getString("color");
        Product product = new Product();
        product.setArticle(article);
        product.setProdName(prodName);
        product.setColor(color);
        return product;
    }

    public Collection <Product> getById (int id){
        Connection connection = DbConnection.getConnection();
        Collection<Product> products = new LinkedList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT products.article, products.prodname, products.color, positions.order_id from products\n" +
                        "LEFT JOIN positions ON products.article = positions.article\n" +
                        "WHERE order_id=" + id +'\n'+
                        "ORDER BY products.article ASC;");
        ) {
            while (rs.next()) {
                products.add(mapProductsList(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;

    }





}
