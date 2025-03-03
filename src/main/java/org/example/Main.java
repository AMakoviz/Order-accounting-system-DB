package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository();

//        Печать списка продуктов
        for (Product i:productRepository.getAll()){
            System.out.println(i);
        }

//        Печать списка продуктов c заданным id
        System.out.println("Введите номер id");
        Scanner sc = new Scanner(System.in);
        String string = sc.nextLine();
        for (Product i:productRepository.getById(Integer.parseInt(string))){
            System.out.println(i);
        }
//
//      Создание нового заказа и вывод всех заказов на экран
        OrderRepository orderRepository = new OrderRepository();
        orderRepository.createNewOrder();


        for (Order i: orderRepository.printOrder()){
            System.out.println(i);
        }

    }

}