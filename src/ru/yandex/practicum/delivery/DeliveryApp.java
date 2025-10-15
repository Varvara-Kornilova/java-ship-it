package ru.yandex.practicum.delivery;

import java.util.Scanner;

public class DeliveryApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ParcelController parcelController = new ParcelController();

        boolean running = true;
        while (running) {
            showMenu();
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Пожалуйста, введите команду в виде числа от 0 до 3.");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        parcelController.addParcel();
                        break;
                    case 2:
                        parcelController.sendParcels();
                        break;
                    case 3:
                        parcelController.calculateCosts();
                        break;
                    case 4:
                        parcelController.reportStatus();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите номер команды (0, 1, 2 или 3).\n");
            }
        }
    }

    private static void showMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1 — Добавить посылку");
        System.out.println("2 — Отправить все посылки");
        System.out.println("3 — Посчитать стоимость доставки");
        System.out.println("4 - Отследить все посылки");
        System.out.println("0 — Завершить");
    }
}

