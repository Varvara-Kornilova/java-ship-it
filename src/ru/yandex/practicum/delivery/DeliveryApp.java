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
                    case 4 -> parcelController.changeCurrentDeliveryLocation();
                    case 5 -> parcelController.showBoxContents();
                    case 6 -> parcelController.showCurrentDeliveryLocation();
                    case 7 -> parcelController.reportDeliveryCompleted();
                    case 8 -> parcelController.printAllParcels();
                    case 9 -> parcelController.showArchive();
                    case 0 -> { sayGoodBye(); running = false;
                    }
                    default -> System.out.println("Неверный выбор. \n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите номер команды (от 0 до 5). \n");
            }
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                 МЕНЮ                   ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1 — Добавить посылку                   ║");
        System.out.println("║ 2 — Отправить все посылки              ║");
        System.out.println("║ 3 — Посчитать стоимость доставки       ║");
        System.out.println("║ 4 — Изменить статус посылки            ║");
        System.out.println("║ 5 — Показать содержимое коробки        ║");
        System.out.println("║ 6 — Отследить посылку                  ║");
        System.out.println("║ 7 — Сообщить о поступлении             ║");
        System.out.println("║ 8 — Показать все активные посылки      ║");
        System.out.println("║ 9 — Посмотреть архив                   ║");
        System.out.println("║                                        ║");
        System.out.println("║ 0 — Завершить работу                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("\nВаш выбор ➤ ");
    }

    private static void sayGoodBye() {
        System.out.println();
        System.out.println("   💙 Спасибо за работу!   ");
        System.out.println("   Всего доброго и удачи!  ");
        System.out.println();
    }
}

