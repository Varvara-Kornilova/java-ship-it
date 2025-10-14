package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParcelController {

    Scanner scanner = new Scanner(System.in);
    List<Parcel> allParcels = new ArrayList<>();

    public void addParcel() {
        // Подсказка: спросите тип посылки и необходимые поля, создайте объект и добавьте в allParcels
        System.out.println("Выберите тип посылки:");
        int parcelType = chooseParcelType();
        String description = readValidDescription();
        int weight = readValidWeight();
        String deliveryAddress = readValidDeliveryAddress();
        int sendDay = readValidSendDay();

        if (parcelType == 1) {
            //создаем StandardParcel
            StandardParcel standardParcel = new StandardParcel(description, weight, deliveryAddress, sendDay);
            System.out.println(standardParcel.toString());

        } else if (parcelType == 2) {
            //создаем PerishableParcel с доп параметром для срока годности
            int timeToLive = readValidTimeToLive();
            PerishableParcel perishableParcel = new PerishableParcel(description, weight, deliveryAddress, sendDay, timeToLive);
            System.out.println(perishableParcel.toString());

        } else {
            //создаем FragileParcel

        }

    }

    public void sendParcels() {
        // Пройти по allParcels, вызвать packageItem() и deliver()
    }

    public void calculateCosts() {
        // Посчитать общую стоимость всех доставок и вывести на экран
    }

    public int chooseParcelType() {
        while (true) {
            System.out.println("Введите число, соответствующее типу посылки: \n"
                    + "1. Обычная посылка \n"
                    + "2. Скоропортящаяся посылка \n"
                    + "3. Хрупкая посылка \n");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Пожалуйста, введите число от 1 до 3х.");
                continue;
            }

            try {
                int cmd = Integer.parseInt(input);

                switch (cmd) {
                    case 1:
                        printParcelRequiredInfo();
                        return 1;
                    case 2:
                        printParcelRequiredInfo();
                        System.out.println("сколько дней может храниться посылка");
                        return 2;
                    case 3:
                        printParcelRequiredInfo();
                        return 3;
                    default:
                        System.out.println("Такой команды нет, повторите ввод.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите число (1, 2 или 3).\n");
            }
        }
    }

    public static void printParcelRequiredInfo() {
        System.out.println("Необходимые данные: \n" +
                "информация о посылке, \n" +
                "вес (целое число), \n" +
                "адрес получателя, \n" +
                "день отправления\n");
    }

    private String readValidDescription() {
        while (true) {
            System.out.println("Введите название посылки");
            String description = scanner.nextLine().trim();

            if (description.isEmpty()) {
                System.out.println("Это поле не может быть пустым. Введите название посылки");
                continue;
            }

            return description;
        }
    }

    private int readValidWeight() {
        while (true) {
            System.out.println("Введите вес (целое число больше нуля");
            String input = scanner.nextLine().trim();
            try {
                int weight = Integer.parseInt(input);

                if (weight < 0) {
                    System.out.println("Число не может быть отрицательным, повторите ввод");
                    continue;
                }
                return weight;
            } catch (NumberFormatException e) {
                System.out.println("Введите вес в формате целого числа");
            }
        }
    }

    private String readValidDeliveryAddress() {
        while (true) {
            System.out.println("Укажите адрес доставки");
            String deliveryAddress = scanner.nextLine().trim();

            if (deliveryAddress.isEmpty()) {
                System.out.println("Это поле не может быть пустым. Введите название посылки");
                continue;
            }

            return deliveryAddress;
        }
    }

    private int readValidSendDay() {
        while (true) {
            System.out.println("Укажите день отправления");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Поле нельзя оставить пустым, повторите ввод");
                continue;
            }

            try {
                int sendDay = Integer.parseInt(input);

                if (sendDay < 1 || sendDay > 30) {
                    System.out.println("День должен быть от 1 до 30. Попробуйте снова.");
                    continue;
                }
                return sendDay;
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число от 1 до 30");
            }
        }
    }

    private int readValidTimeToLive() {
        while (true) {
            System.out.println("Укажите, сколько дней посылка может находиться в дороге прежде, чем испортиться");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Поле нельзя оставить пустым, повторите ввод");
                continue;
            }

            try {
                int timeToLive = Integer.parseInt(input);

                if (timeToLive < 0) {
                    System.out.println("Введите положительное число");
                    continue;
                }

                if (timeToLive == 0) {
                    System.out.println("Доставка занимает минимум 1 день, мы не можем принять такую посылку");
                    continue;
                }
                return timeToLive;
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число от 1 до 30");
            }
        }
    }

}
