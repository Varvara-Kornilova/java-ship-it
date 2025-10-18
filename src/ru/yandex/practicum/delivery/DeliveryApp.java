package ru.yandex.practicum.delivery;

import java.util.Scanner;

public class DeliveryApp {

    private final Scanner scanner = new Scanner(System.in);
    private final ParcelController parcelController = new ParcelController();
    private boolean running = true;

    public static void main(String[] args) {
        DeliveryApp app = new DeliveryApp();
        app.run();
    }

    private void run() {
        while (running) {
            showMenu();
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Пожалуйста, введите команду в виде числа от 0 до 5. \n");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> parcelController.addParcel();
                    case 2 -> parcelController.sendParcels();
                    case 3 -> parcelController.calculateCosts();
                    case 4 -> parcelController.reportStatus();
                    case 5 -> parcelController.showBoxContents();
                    case 0 -> running = false;
                    default -> System.out.println("Неверный выбор. \n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите номер команды (от 0 до 5). \n");
            }
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("Выберите действие:");
        System.out.println("1 — Добавить посылку");
        System.out.println("2 — Отправить все посылки");
        System.out.println("3 — Посчитать стоимость доставки");
        System.out.println("4 - Отследить все посылки");
        System.out.println("5 - Показать содержимое коробки");
        System.out.println("0 — Завершить \n");
    }
}

