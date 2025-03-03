package org.example;

import java.util.Objects;

public class Position {
    private int order_id;
    private Long article;
    private int quantity;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        if (article >= 3251615 && article <= 3251620){
            this.article = article;
        } else this.article = -1L;


    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity>0){
            this.quantity = quantity;
        } else this.quantity = -1;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return order_id == position.order_id && Objects.equals(article, position.article);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order_id, article);
    }
}
