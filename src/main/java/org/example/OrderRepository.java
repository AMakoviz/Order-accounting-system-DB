package org.example;

import java.sql.*;
import java.util.*;

public class OrderRepository {


    private Order scanOrders(Scanner sc) {
        System.out.println("Введите имя покупателя");
        Order order = new Order();
        order.setName(sc.nextLine());
        System.out.println("Введите контактный номер телефона");
        order.setPhone(sc.nextLine());
        System.out.println("Введите адрес электронной почты");
        order.setEmail(sc.nextLine());
        System.out.println("Введите адрес доставки");
        order.setAdress(sc.nextLine());
        return order;
    }

    private Map<Long, Integer> scanPosition(Scanner sc) {
        System.out.println("Введите артикулы товаров через запятую");
        String article = sc.nextLine();
        Map<Long, Integer> pos = new HashMap<>();
        System.out.println("Введите количество товара для каждой позиции заказа через запятую");
        String count = sc.nextLine();
        ArrayList<Long> articleList = new ArrayList<>();
        ArrayList<Integer> countList = new ArrayList<>();
        int articleCount = checkPosition(article);
        int quantityCount = checkPosition(count);
        if (articleCount!=quantityCount){
            throw new IllegalArgumentException("Количество заказанных товаров не соответствует количеству артикулов");
        }
        if (article.isEmpty() || article.trim().isEmpty()){
            throw new NullPointerException("Значение артикула не может быть пустым");
        } else if (article.contains(",")) {
            for (String i : comma(article)) {
                if (isLong(i)){
                    articleList.add(Long.parseLong(i));
                } else throw new IllegalArgumentException("Артикул должен быть числом");
            }
        } else if (isLong(article)){
            articleList.add(Long.parseLong(article));
        } else throw new IllegalArgumentException("Артикул должен быть числом");

        if (count.isEmpty() || count.trim().isEmpty()){
            throw new NullPointerException("Значение количества товаров не может быть пустым");
        } else if (count.contains(",")) {
            for (String i : comma(count)) {
                if (isInt(i)){
                    countList.add(Integer.parseInt(i));
                } else throw new IllegalArgumentException("Значение количества товаров должен быть числом");

            }
        } else if (isInt(count)) {
            countList.add(Integer.parseInt(count));
        } else throw new IllegalArgumentException("Значение количества товаров должен быть числом");

        for (int i = 0; i <= articleCount; i++) {
            pos.put(articleList.get(i), countList.get(i));
        }
        return pos;

    }
    private int checkPosition(String scanner){
        int a = 0;
        int end;
        while (scanner.contains(",")) {
            end = scanner.indexOf(",");
            scanner = scanner.substring(end + 1);
            a++;
        }
        return a;
    }

    private ArrayList<String> comma(String address) {
        String[] arts = address.trim().split(",");
        ArrayList<String> result = new ArrayList<>();
        for (String art : arts) {
            result.add(art.trim());
        }
        return result;
    }


    private Order createOrder(Order order) {
        Connection connection = DbConnection.getConnection();
        String query = "INSERT INTO orders (order_id, data_ord, persons_name, phone, email, adress, status, data_delivery) \n" +
                "VALUES (DEFAULT, now(), ?, ?, ?, ?, 'P', NULL);";

        try (
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, order.getName());
            if (order.getPhone()==null){
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, order.getPhone());
            }
            statement.setString(2, order.getPhone());
            if (order.getEmail()==null){
                statement.setNull(3, Types.VARCHAR);
            } else {
                statement.setString(3, order.getEmail());
            }
            statement.setString(4, order.getAdress());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getId(order);

    }
    private Order getId (Order order){
        Connection connection = DbConnection.getConnection();
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT MAX(order_id) AS max FROM orders");
        ) {
            if (rs.next()){
                order.setOrder_id(rs.getInt("max"));
            } else {
                throw new SQLException();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return order;
    }

    private void createPosition(Long article, Integer quantity, Order order) {
        Position position = new Position();
        position.setOrder_id(order.getOrder_id());
        position.setQuantity(quantity);
        if (position.getQuantity() == -1) {
            removePerson(getId(order));
            throw new IllegalArgumentException("Количество заказанных товаров не может быть отрицательным или равным нулю");
        }
        position.setArticle(article);
        if (position.getArticle() == -1L){
            removePerson(getId(order));
            throw new IllegalArgumentException("Проверьте правильность введенного артикула");
        }

        Connection connection = DbConnection.getConnection();

        String query = " INSERT INTO positions (order_id, article, price, quantity) \n" +
                "VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, order.getOrder_id());
            statement.setLong(2, article);
            if (price(position) != 0) {
                statement.setInt(3, price(position));
            } else throw new IllegalArgumentException("Товары с введенными артикулами не найдены");
            statement.setInt(4, quantity);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int price(Position position) {
        Connection connection = DbConnection.getConnection();
        int price = 0;
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT products.price FROM products WHERE products.article =" + position.getArticle());
        ) {
            if (rs.next()){
                price = rs.getInt("price");
            } else {
                throw new SQLException("Ошибка: продукт с таким артикулом не найден");
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return price;
    }

    private void create(Order order, Map<Long, Integer> pos) {

        order = createOrder(order);

        for (Map.Entry<Long, Integer> entry : pos.entrySet()) {
            Long article = entry.getKey();
            Integer quantity = entry.getValue();
            createPosition(article, quantity, order);
        }
    }

    public void createNewOrder(){
        Scanner sc = new Scanner(System.in);
        Order order = scanOrders(sc);
        Map <Long, Integer> pos = scanPosition(sc);
        create(order,pos);

    }
    public Collection <Order> printOrder(){
        Connection connection = DbConnection.getConnection();
        Collection<Order> orders = new LinkedList<>();
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM orders");
        ) {
            while (rs.next()) {
                orders.add(mapRowOrders(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return orders;
    }
    private Order mapRowOrders (ResultSet rs) throws SQLException{
        int order_id = rs.getInt("order_id");
        String name = rs.getString("persons_name");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String adress = rs.getString("adress");
        Order order = new Order();
        order.setOrder_id(order_id);
        order.setName(name);
        order.setPhone(phone);
        order.setEmail(email);
        order.setAdress(adress);

        return order;
    }
    private boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void removePerson(Order order){
        Connection connection = DbConnection.getConnection();
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setInt(1, order.getOrder_id());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
