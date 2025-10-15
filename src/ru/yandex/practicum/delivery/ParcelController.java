package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParcelController {

    Scanner scanner = new Scanner(System.in);
    List<Parcel> allParcels = new ArrayList<>();
    List<Trackable> trackableParcels = new ArrayList<>();

    ParcelBox<StandardParcel> standardBox = new ParcelBox<>(50);
    ParcelBox<PerishableParcel> perishableBox = new ParcelBox<>(10);
    ParcelBox<FragileParcel> fragileBox = new ParcelBox<>(5);

    // КОД ДЛЯ ПРОВЕРКИ ЧТОБЫ НЕ ВПИСЫВАТЬ ДАННЫЕ
//    StandardParcel doll = new StandardParcel("Кукла", 1, "Долгопрудный", 5);
//    StandardParcel toy = new StandardParcel("Игрушка", 1, "Зеленоград", 3);
//    PerishableParcel meat = new PerishableParcel("Колбаса", 2, "Клин", 28, 5);
//    PerishableParcel fish = new PerishableParcel("Рыба", 4, "Солнечногорск", 30, 1);
//    FragileParcel aquas = new FragileParcel("Аквариум", 5, "Солнечногорск", 25);
//    FragileParcel glass = new FragileParcel("Стакан", 1, "Высоковск", 15);

//    public void addParcel() {
//
//        standardBox.addParcel(doll);
//        standardBox.addParcel(toy);
//        perishableBox.addParcel(meat);
//        perishableBox.addParcel(fish);
//        fragileBox.addParcel(aquas);
//        fragileBox.addParcel(glass);
//
//        trackableParcels.add(aquas);
//        trackableParcels.add(glass);
//
//        allParcels.add(doll);
//        allParcels.add(toy);
//        allParcels.add(meat);
//        allParcels.add(fish);
//        allParcels.add(aquas);
//        allParcels.add(glass);
//    }


//    //ОСНОВНОЙ КОД КОТОРЫЙ Я ЗАКОММЕНТИРУЮ НА ВРЕМЯ ТЕСТОВЫХ ЗАПИСЕЙ
    public void addParcel() {
        // Подсказка: спросите тип посылки и необходимые поля, создайте объект и добавьте в allParcels
        int parcelType = chooseParcelType();
        String description = readValidDescription();
        int weight = readValidWeight();
        String deliveryAddress = readValidDeliveryAddress();
        int sendDay = readValidSendDay();


        if (parcelType == 1) {
            //создаем StandardParcel
            StandardParcel parcel = new StandardParcel(description, weight, deliveryAddress, sendDay);
            if (standardBox.addParcel(parcel)) {
                allParcels.add(parcel);
                printSuccess();
                System.out.println(parcel.toString());
            }

        } else if (parcelType == 2) {
            //создаем PerishableParcel с доп параметром для срока годности
            int timeToLive = readValidTimeToLive();
            PerishableParcel parcel = new PerishableParcel(description, weight, deliveryAddress, sendDay, timeToLive);
            if (perishableBox.addParcel(parcel)) {
                allParcels.add(parcel);
                printSuccess();
                System.out.println(parcel.toString());
            }

        } else {
            //создаем FragileParcel
            FragileParcel parcel = new FragileParcel(description, weight, deliveryAddress, sendDay);
            if (fragileBox.addParcel(parcel)) {
                allParcels.add(parcel);
                trackableParcels.add(parcel);
                printSuccess();
                System.out.println(parcel.toString());
            }
        }
    }

    public void sendParcels() {
        if (allParcels.isEmpty()) {
            System.out.println("Вы пока не добавили ни одной посылки");
            return;
        }
        for (Parcel parcel : allParcels) {
            parcel.packageItem();
            parcel.deliver();
        }
        // Пройти по allParcels, вызвать packageItem() и deliver()
    }

    public void calculateCosts() {
        // Посчитать общую стоимость всех доставок и вывести на экран
        int totalCost = 0;
        for (Parcel parcel : allParcels) {
            totalCost += parcel.calculateDeliveryCost();
        }
        System.out.println(totalCost);;
    }

    public void reportStatus() {
        if (trackableParcels.isEmpty()) {
            System.out.println("У вас нет отслеживаемых посылок (коробка с хрупкими посылками пуста).");
            return;
        }
        for (Trackable trackable : trackableParcels) {
            Parcel parcel = (Parcel) trackable;
            System.out.print("Введите местоположение посылки <<" + parcel.getDescription() + ">>: ");
            String newLocation = scanner.nextLine().trim();
            trackable.reportStatus(newLocation);
            parcel.setCurrentDeliveryLocation(newLocation);
        }
    }

    public void showBoxContents() {
        int parcelType = chooseParcelType();
        switch (parcelType) {
            case 1:
                System.out.println("Содержимое коробки для обычных посылок:");
                printParcels(standardBox.getAllParcels());
                break;
            case 2:
                System.out.println("Содержимое коробки для скоропортящихся посылок:");
                printParcels(perishableBox.getAllParcels());
                break;
            case 3:
                System.out.println("Содержимое коробки для хрупких посылок:");
                printParcels(fragileBox.getAllParcels());
                break;
        }
    }

    private void printParcels(List<? extends Parcel> parcels) {
        if (parcels.isEmpty()) {
            System.out.println("Коробка пуста.");
        } else {
            for (Parcel parcel : parcels) {
                System.out.println("— " + parcel.getDescription() + " (" + parcel.getWeight() + "кг.)");
            }
        }
    }

     public void printNumbersToChooseType() {
         System.out.println("Введите число, соответствующее типу посылки: \n"
                 + "1. Обычная посылка \n"
                 + "2. Скоропортящаяся посылка \n"
                 + "3. Хрупкая посылка \n");
     }


    public int chooseParcelType() {
        while (true) {
            printNumbersToChooseType();

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Пожалуйста, введите число от 1 до 3х.");
                continue;
            }

            try {
                int cmd = Integer.parseInt(input);

                switch (cmd) {
                    case 1:
                        System.out.println("Выбран тип: обычная посылка.");
                        return 1;
                    case 2:
                        System.out.println("Выбран тип: скоропортящаяся посылка.");
                        return 2;
                    case 3:
                        System.out.println("Выбран тип: хрупкая посылка.");
                        return 3;
                    default:
                        System.out.println("Такой команды нет, повторите ввод.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат. Пожалуйста, введите число (1, 2 или 3).");
            }
        }
    }


    private String readValidDescription() {
        while (true) {
            System.out.print("Введите название посылки: ");
            String description = scanner.nextLine().trim();

            if (description.isEmpty()) {
                printEmptyFieldError();
                continue;
            }

            return description;
        }
    }

    private int readValidWeight() {
        while (true) {
            System.out.print("Введите вес (целое число больше нуля): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                printEmptyFieldError();
                continue;
            }

            try {
                int weight = Integer.parseInt(input);

                if (weight < 0) {
                    System.out.println("Число не может быть отрицательным, повторите ввод.");
                    continue;
                }
                if (weight < 1) {
                    System.out.println("Вес не может быть равен 0.");
                    continue;
                }
                return weight;
            } catch (NumberFormatException e) {
                System.out.println("Введите вес в формате целого числа.");
            }
        }
    }

    private String readValidDeliveryAddress() {
        while (true) {
            System.out.print("Укажите адрес доставки: ");
            String deliveryAddress = scanner.nextLine().trim();

            if (deliveryAddress.isEmpty()) {
                printEmptyFieldError();
                continue;
            }

            return deliveryAddress;
        }
    }

    private int readValidSendDay() {
        while (true) {
            System.out.print("Укажите день отправления: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                printEmptyFieldError();
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
                System.out.println("Пожалуйста, введите целое число от 1 до 30.");
            }
        }
    }

    private int readValidTimeToLive() {
        while (true) {
            System.out.print("Укажите, сколько дней посылка может находиться в дороге прежде, чем испортиться: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                printEmptyFieldError();
                continue;
            }

            try {
                int timeToLive = Integer.parseInt(input);

                if (timeToLive < 0) {
                    System.out.println("Введите положительное число.");
                    continue;
                }

                if (timeToLive == 0) {
                    System.out.println("Доставка занимает минимум 1 день, мы не можем принять такую посылку.");
                    continue;
                }
                return timeToLive;
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число от 1 до 30.");
            }
        }
    }

    private void printSuccess() {
        System.out.println("Операция выполнена.");
    }

    private void printEmptyFieldError() {
        System.out.println("Поле не может быть пустым. Пожалуйста, повторите ввод.");
    }
}
