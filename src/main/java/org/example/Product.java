package org.example;

import java.util.Objects;

public class Product {
    private Long article;
    private String prodName;
    private String color;
    private int price;
    private int inStock;

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(article, product.article);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(article);
    }

    @Override
    public String toString() {
        StringBuilder print = new StringBuilder();
        print.append("Products: " +
                "article=" + article +
                ", prodName='" + prodName + '\'');
        if (color != null){
            print.append(", color='" + color+ '\'');
        }
        if (price != 0 && inStock != 0) {
            print.append(", price=" + price +
                    ", inStock=" + inStock +
                    '}');
        }
        return print.toString();
    }
}
