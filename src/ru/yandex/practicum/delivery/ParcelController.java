package ru.yandex.practicum.delivery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ParcelController {

    private static final int STANDARD_BOX_CAPACITY = 50;
    private static final int PERISHABLE_BOX_CAPACITY = 10;
    private static final int FRAGILE_BOX_CAPACITY = 5;

    private final Scanner scanner = new Scanner(System.in);
    private final List<Parcel> allParcels = new ArrayList<>();
    private final List<Trackable> trackableParcels = new ArrayList<>();
    private final List<Parcel> archive = new ArrayList<>();

    private ParcelBox<StandardParcel> currentStandardBox;
    private ParcelBox<PerishableParcel> currentPerishableBox;
    private ParcelBox<FragileParcel> currentFragileBox;

    private final List<ParcelBox<? extends Parcel>> allBoxes = new ArrayList<>();

    public ParcelController() {
        createNewBoxForType(ParcelCategory.STANDARD);
        createNewBoxForType(ParcelCategory.PERISHABLE);
        createNewBoxForType(ParcelCategory.FRAGILE);
    }

    public void createNewBoxForType(ParcelCategory type) {
        switch (type) {
            case STANDARD -> {
                currentStandardBox = new ParcelBox<>(STANDARD_BOX_CAPACITY, ParcelCategory.STANDARD, false);
                allBoxes.add(currentStandardBox);
            }
            case PERISHABLE -> {
                currentPerishableBox = new ParcelBox<>(PERISHABLE_BOX_CAPACITY, ParcelCategory.PERISHABLE, false);
                allBoxes.add(currentPerishableBox);
            }
            case FRAGILE -> {
                currentFragileBox = new ParcelBox<>(FRAGILE_BOX_CAPACITY, ParcelCategory.FRAGILE, true);
                allBoxes.add(currentFragileBox);
            }
        }
    }


    public void sendParcels() {
        if (allParcels.isEmpty()) {
            System.out.println("Вы пока не добавили ни одной посылки");
            return;
        }
        sendIfNotSent(currentStandardBox);
        sendIfNotSent(currentPerishableBox);
        sendIfNotSent(currentFragileBox);
    }

    private void sendIfNotSent(ParcelBox<? extends Parcel> box) {
        if (box.isSent()) {
            System.out.println("Коробка " + box.getBoxType() + " уже отправлена!");
            return;
        }

        if (box.getAllParcels().isEmpty()) {
            System.out.println("В коробке " + box.getBoxType() + " еще нет посылок.");
            return;
        }

        for (Parcel parcel : box.getAllParcels()) {
            parcel.packageItem();
            parcel.deliver();
            parcel.changeStatus(Status.SENT);
            parcel.setCurrentDeliveryLocation("Посылка отправлена из почтового отделения");
        }

        box.setSent(true);
    }

    private StandardParcel createStandardParcel(String description, int weight, String address, int sendDay) {
        return new StandardParcel(description, weight, address, sendDay);
    }

    private PerishableParcel createPerishableParcel(String description, int weight, String address, int sendDay) {
        int timeToLive = readValidTimeToLive();
        return new PerishableParcel(description, weight, address, sendDay, timeToLive);
    }

    private FragileParcel createFragileParcel(String description, int weight, String address, int sendDay) {
        return new FragileParcel(description, weight, address, sendDay);
    }

    private ParcelBox<? super Parcel> createParcelBox(int maxWeight, ParcelCategory boxType, boolean isTrackable) {
        return new ParcelBox<>(maxWeight, boxType, isTrackable);
    }

    private boolean addParcelToSystem(Parcel parcel, boolean isTrackable) {
        parcel.changeStatus(Status.CREATED);
        allParcels.add(parcel);
        archive.add(parcel);
        if (isTrackable) {
            trackableParcels.add((Trackable) parcel);
        }
        System.out.print(parcel);
        printSuccess();
        return true;
    }

    public void addParcel() {
        ParcelCategory parcelType = chooseParcelType();
        String description = readValidDescription();
        int weight = readValidWeight();
        String deliveryAddress = readValidDeliveryAddress();
        int sendDay = readValidSendDay();

        switch (parcelType) {
            case STANDARD -> {
                if (weight > STANDARD_BOX_CAPACITY) {
                    System.out.println("Ошибка: вес посылки (" + weight + " кг) превышает максимальную ёмкость коробки для стандартных посылок (" + STANDARD_BOX_CAPACITY + " кг).");
                    return;
                }
                StandardParcel parcel = createStandardParcel(description, weight, deliveryAddress, sendDay);
                // Если текущая коробка уже отправлена — создаём новую
                if (currentStandardBox.isSent()) {
                    createNewBoxForType(ParcelCategory.STANDARD);
                }
                if (!currentStandardBox.addParcel(parcel)) {
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentStandardBox); // или просто помечаем как отправленную
                    createNewBoxForType(ParcelCategory.STANDARD);
                    currentStandardBox.addParcel(parcel);
                }
                addParcelToSystem(parcel, parcel instanceof Trackable);
            }

            case PERISHABLE -> {
                if (weight > PERISHABLE_BOX_CAPACITY) {
                    System.out.println("Ошибка: вес посылки (" + weight + " кг) превышает максимальную ёмкость коробки для скоропортящихся посылок (" + PERISHABLE_BOX_CAPACITY + " кг).");
                    return;
                }
                PerishableParcel parcel = createPerishableParcel(description, weight, deliveryAddress, sendDay);
                if (currentPerishableBox.isSent()) {
                    createNewBoxForType(ParcelCategory.PERISHABLE);
                }
                if (!currentPerishableBox.addParcel(parcel)) {
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentPerishableBox);
                    createNewBoxForType(ParcelCategory.PERISHABLE);
                    currentPerishableBox.addParcel(parcel);
                }
                addParcelToSystem(parcel, parcel instanceof Trackable);
            }

            case FRAGILE -> {

                if (weight > FRAGILE_BOX_CAPACITY) {
                    System.out.println("Ошибка: вес посылки (" + weight + " кг) превышает максимальную ёмкость коробки для хрупких посылок (" + FRAGILE_BOX_CAPACITY + " кг).");
                    return;
                }

                FragileParcel parcel = createFragileParcel(description, weight, deliveryAddress, sendDay);
                if (currentFragileBox.isSent()) {
                    createNewBoxForType(ParcelCategory.FRAGILE);
                }
                if (!currentFragileBox.addParcel(parcel)) {
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentFragileBox);
                    createNewBoxForType(ParcelCategory.FRAGILE);
                    currentFragileBox.addParcel(parcel);
                }
                addParcelToSystem(parcel, parcel instanceof Trackable);
            }
        }
    }

    private ParcelBox<? extends Parcel> getActiveBox(ParcelCategory type) {
        return switch (type) {
            case STANDARD -> currentStandardBox;
            case PERISHABLE -> currentPerishableBox;
            case FRAGILE -> currentFragileBox;
        };
    }

    @SuppressWarnings("unchecked")
    private <T extends Parcel> ParcelBox<T> getActiveTypedBox(ParcelCategory type) {
        return (ParcelBox<T>) getActiveBox(type);
    }

    public ArrayList<Parcel> getArchive() {
        return new ArrayList<>(archive);
    }

    public void showArchive() {
        if (getArchive().isEmpty()) {
            System.out.println("Архив пока пуст.");
            return;
        }
        for (Parcel parcel : getArchive()) {
            System.out.println(parcel);
        }
    }

    public void calculateCosts() {

        int totalCost = 0;
        for (Parcel parcel : allParcels) {
            totalCost += parcel.calculateDeliveryCost();
        }
        System.out.println("Общая сумма всех посылок, переданных на доставку, составляет " + totalCost + "$");
    }

    public void reportDeliveryCompleted() {
        if (allParcels.isEmpty()) {
            System.out.println("У вас нет ни одной посылки.");
            return;
        }
        System.out.println("Введите название посылки, которая была доставлена:");
        String parcelName = scanner.nextLine().trim();
        makeDeliveryCompleted(parcelName);
    }

    public void makeDeliveryCompleted(String parcelName) {
        // Сначала проверим, есть ли такая посылка
        boolean exists = allParcels.stream()
                .anyMatch(parcel -> parcel.getDescription().equals(parcelName));

        if (!exists) {
            System.out.println("Такой посылки в списке нет.");
            return;
        }

        // Если есть дубликаты — уточняем тип
        long count = allParcels.stream()
                .filter(parcel -> parcel.getDescription().equals(parcelName))
                .count();

        ParcelCategory requiredType = null;

        if (count > 1) {
            System.out.println("Укажите тип посылки, которая была доставлена:");

            requiredType = chooseParcelType();
        }

        // Используем итератор для безопасного удаления
        Iterator<Parcel> iterator = allParcels.iterator();

        while (iterator.hasNext()) {
            Parcel parcel = iterator.next();

            boolean matchesName = parcel.getDescription().equals(parcelName);
            boolean matchesType = (requiredType == null) || parcel.getType().equals(requiredType);

            if (matchesName && matchesType) {
                System.out.println("Посылка прибыла в пункт назначения.");
                System.out.println("Вынимаем из коробки.");

                if (parcel.getType().equals(ParcelCategory.STANDARD)) {
                    currentStandardBox.removeParcel((StandardParcel) parcel);
                } else if (parcel.getType().equals(ParcelCategory.PERISHABLE)) {
                    currentPerishableBox.removeParcel((PerishableParcel) parcel);
                } else if (parcel.getType().equals(ParcelCategory.FRAGILE)) {
                    currentFragileBox.removeParcel((FragileParcel) parcel);
                    trackableParcels.remove(parcel);
                }

                // Безопасное удаление из allParcels
                iterator.remove();

                parcel.changeStatus(Status.DELIVERED);

                System.out.println("Уведомление о поступлении посылки <<" + parcel.getDescription() + ">> отправлено получателю.");

                if (allParcels.isEmpty()) {
                    System.out.println("Все посылки доставлены, можно отдыхать!");
                }

                return;
            }
        }

        // Если дошли сюда — не нашли подходящую посылку (например, неверный тип при дубликатах)
        if (count < 1) {
            System.out.println("Не найдено посылки с указанным типом.");
        }
    }

    private ParcelBox<? extends Parcel> findBoxForParcel(Parcel parcel) {
        if (currentStandardBox.getAllParcels().contains(parcel)) {
            return currentStandardBox;
        }
        if (currentPerishableBox.getAllParcels().contains(parcel)) {
            return currentPerishableBox;
        }
        if (currentFragileBox.getAllParcels().contains(parcel)) {
            return currentFragileBox;
        }
        throw new IllegalArgumentException("Посылка не найдена ни в одной коробке.");
    }

    public void changeCurrentDeliveryLocation() {
        if (trackableParcels.isEmpty()) {
            System.out.println("У вас нет отслеживаемых посылок.");
            return;
        }

        System.out.println("Введите название отслеживаемой посылки:");
        String parcelName = scanner.nextLine().trim();

        for (Trackable trackable : trackableParcels) {
            Parcel parcel = (Parcel) trackable;

            if (parcelName.isEmpty()) {
                System.out.println("Пожалуйста, введите команду. \n");
                return;
            }

            ParcelBox<? extends Parcel> box = findBoxForParcel(parcel);
            if (!box.isSent() && box.isTrackable()) {
                System.out.println("Коробка еще не отправлена, вы не можете поменять ее статус.");
                return;
            }

            if (parcelName.equals(parcel.getDescription())) {
                System.out.print("Введите местоположение посылки <<" + parcel.getDescription() + ">>: ");
                String newLocation = scanner.nextLine().trim();
                trackable.reportStatus(newLocation);
                parcel.setCurrentDeliveryLocation(newLocation);
                parcel.changeStatus(Status.IN_TRANSIT);

                if (parcel.getCurrentDeliveryLocation().equals(parcel.getDeliveryAddress())) {
                    makeDeliveryCompleted(parcelName);
                    parcel.changeStatus(Status.DELIVERED);
                }
                return;
            }
        }
        System.out.println("Такой посылки нет. Проверьте название.");
    }

    public void showCurrentDeliveryLocation() {

        if (trackableParcels.isEmpty()) {
            System.out.println("У вас нет отслеживаемых посылок.");
            return;
        }

        System.out.println("Введите название посылки: ");
        String description = scanner.nextLine().trim();

        boolean parcelExists = allParcels.stream()
                .anyMatch(p -> p.getDescription().equals(description));

        if (!parcelExists) {
            System.out.println("Посылка с таким названием не найдена.");
            return;
        }

        for (Trackable trackable : trackableParcels) {
            Parcel parcel = (Parcel) trackable;
            if (parcel.getDescription().equals(description)) {
                System.out.println(parcel.getCurrentDeliveryLocation());
            }
            return;
        }
        System.out.println("Запрашиваемая вами посылка не отслеживается.");
    }

    public void showBoxContents() {
        if (allParcels.isEmpty()) {
            System.out.println("У вас пока нет посылок, все коробки пусты.");
            return;
        }
        ParcelCategory parcelType = chooseParcelType();
        switch (parcelType) {
            case STANDARD:
                System.out.println("Содержимое коробки для обычных посылок:");
                printParcels(currentStandardBox.getAllParcels());
                break;
            case PERISHABLE:
                System.out.println("Содержимое коробки для скоропортящихся посылок:");
                printParcels(currentPerishableBox.getAllParcels());
                break;
            case FRAGILE:
                System.out.println("Содержимое коробки для хрупких посылок:");
                printParcels(currentFragileBox.getAllParcels());
                break;
        }
    }

    public void printAllParcels() {
        if (allParcels.isEmpty()) {
            System.out.println("У вас нет ни одной посылки.");
            return;
        }
        for (Parcel parcel : allParcels) {
            System.out.println(parcel.toString());
        }
    }

    private void printParcels(List<? extends Parcel> parcels) {
        if (parcels.isEmpty()) {
            System.out.println("Коробка пуста.");
        } else {
            for (Parcel parcel : parcels) {
                System.out.println("— " + parcel.getDescription() + " (" + parcel.getWeight() + "кг)");
            }
        }
    }

    public void printNumbersToChooseType() {
        System.out.println("Введите число, соответствующее типу посылки: \n"
                 + "1. Обычная посылка \n"
                 + "2. Скоропортящаяся посылка \n"
                 + "3. Хрупкая посылка \n");
    }

    public ParcelCategory chooseParcelType() {
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
                        return ParcelCategory.STANDARD;
                    case 2:
                        System.out.println("Выбран тип: скоропортящаяся посылка.");
                        return ParcelCategory.PERISHABLE;
                    case 3:
                        System.out.println("Выбран тип: хрупкая посылка.");
                        return ParcelCategory.FRAGILE;
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
        System.out.println(" успешно добавлена.");
    }

    private void printEmptyFieldError() {
        System.out.println("Поле не может быть пустым. Пожалуйста, повторите ввод.");
    }
}



//    private final ParcelBox<? super Parcel> standardBox = new ParcelBox<>(STANDARD_BOX_CAPACITY, ParcelCategory.STANDARD, false);
//    private final ParcelBox<? super Parcel> perishableBox = new ParcelBox<>(PERISHABLE_BOX_CAPACITY, ParcelCategory.PERISHABLE, false);
//    private final ParcelBox<? super Parcel> fragileBox = new ParcelBox<>(FRAGILE_BOX_CAPACITY, ParcelCategory.FRAGILE, true);

//    public void sendParcels() {
//        if (allParcels.isEmpty()) {
//            System.out.println("Вы пока не добавили ни одной посылки");
//            return;
//        }
//        if (!standardBox.isSent()) {
//            for (StandardParcel parcel : standardBox.getAllParcels()) {
//                parcel.packageItem();
//                parcel.deliver();
//                parcel.changeStatus(Status.SENT);
//                standardBox.setSent(true);
//            }
//        } else {
//            System.out.println("Посылки уже отправлены.");
//        }
//    }

//СТАРЫЙ МЕТОД ДОБАВЛЕНИЯ ПОСЫЛКИ С ДУБЛИРОВАНИЕМ КОДА
//public void addParcel() {
//
//        int parcelType = chooseParcelType();
//        String description = readValidDescription();
//        int weight = readValidWeight();
//        String deliveryAddress = readValidDeliveryAddress();
//        int sendDay = readValidSendDay();
//
//        if (parcelType == 1) {
//            //создаем StandardParcel
//            StandardParcel parcel = new StandardParcel(description, weight, deliveryAddress, sendDay);
//            if (standardBox.addParcel(parcel)) {
//                parcel.changeStatus(Status.CREATED);
//                allParcels.add(parcel);
//                archive.add(parcel);
//                System.out.print(parcel);
//                printSuccess();
//            }
//
//        } else if (parcelType == 2) {
//            //создаем PerishableParcel с доп параметром для срока годности
//            int timeToLive = readValidTimeToLive();
//            PerishableParcel parcel = new PerishableParcel(description, weight, deliveryAddress, sendDay, timeToLive);
//            if (perishableBox.addParcel(parcel)) {
//                parcel.changeStatus(Status.CREATED);
//                archive.add(parcel);
//                allParcels.add(parcel);
//                System.out.print(parcel);
//                printSuccess();
//            }
//
//        } else {
//            //создаем FragileParcel
//            FragileParcel parcel = new FragileParcel(description, weight, deliveryAddress, sendDay);
//            if (fragileBox.addParcel(parcel)) {
//                parcel.changeStatus(Status.CREATED);
//                allParcels.add(parcel);
//                trackableParcels.add(parcel);
//                archive.add(parcel);
//                System.out.print(parcel);
//                printSuccess();
//            }
//        }
//    }