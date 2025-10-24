package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DeliveryApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ParcelController parcelController = new ParcelController();
        boolean running = true;

        while (running) {
            System.out.println(showMenu());
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
                    case 4 -> parcelController.changeCurrentDeliveryLocation();
                    case 5 -> parcelController.showBoxContents();
                    case 6 -> parcelController.showCurrentDeliveryLocation();
                    case 7 -> parcelController.reportDeliveryCompleted();
                    case 8 -> parcelController.printAllParcels();
                    case 9 -> parcelController.showArchive();
                    case 0 -> {
                        System.out.println(sayGoodBye());
                        running = false;
                    }
                    default -> System.out.println("Неверный выбор. \n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите номер команды (от 0 до 5). \n");
            }
        }
    }

    public static String showMenu() {
        return """
                ╔════════════════════════════════════════╗
                ║                 МЕНЮ                   ║
                ╠════════════════════════════════════════╣
                ║ 1 — Добавить посылку                   ║
                ║ 2 — Отправить все посылки              ║
                ║ 3 — Посчитать стоимость доставки       ║
                ║ 4 — Изменить статус посылки            ║
                ║ 5 — Показать содержимое коробки        ║
                ║ 6 — Отследить посылку                  ║
                ║ 7 — Сообщить о поступлении             ║
                ║ 8 — Показать все активные посылки      ║
                ║ 9 — Посмотреть архив                   ║
                ║                                        ║
                ║ 0 — Завершить работу                   ║
                ╚════════════════════════════════════════╝
                """;
    }

    private static String sayGoodBye() {
        return """
                
                   💙 Спасибо за работу!
                   Всего доброго и удачи!
                
                """;
    }
}

