package ru.yandex.practicum.delivery;

import java.util.*;
import java.util.stream.Collectors;

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

    private final List<ParcelBox<StandardParcel>> standardBoxes = new ArrayList<>();
    private final List<ParcelBox<PerishableParcel>> perishableBoxes = new ArrayList<>();
    private final List<ParcelBox<FragileParcel>> fragileBoxes = new ArrayList<>();

    private final List<ParcelBox<? extends Parcel>> allBoxes = new ArrayList<>();

    public ParcelController() {
        createNewBoxForType(ParcelCategory.STANDARD);
        createNewBoxForType(ParcelCategory.PERISHABLE);
        createNewBoxForType(ParcelCategory.FRAGILE);
    }

    public void createNewBoxForType(ParcelCategory type) {
        switch (type) {
            case STANDARD -> {
                ParcelBox<StandardParcel> box = new ParcelBox<>(STANDARD_BOX_CAPACITY, type, false);
                currentStandardBox = box;
                standardBoxes.add(box);
                allBoxes.add(box); // ← обязательно добавляем в общий список
            }
            case PERISHABLE -> {
                ParcelBox<PerishableParcel> box = new ParcelBox<>(PERISHABLE_BOX_CAPACITY, type, false);
                currentPerishableBox = box;
                perishableBoxes.add(box);
                allBoxes.add(box); // ← обязательно
            }
            case FRAGILE -> {
                ParcelBox<FragileParcel> box = new ParcelBox<>(FRAGILE_BOX_CAPACITY, type, true);
                currentFragileBox = box;
                fragileBoxes.add(box);
                allBoxes.add(box); // ← обязательно
            }
        }
    }

    private List<Parcel> findParcelsByName(String name) {
        return allParcels.stream()
                .filter(parcel -> parcel.getDescription().equals(name))
                .collect(Collectors.toList());
    }

    private Parcel chooseParcelFromList(List<Parcel> parcels, String name) {
        if (parcels.isEmpty()) {
            System.out.println("Посылка с названием <<" + name + ">> не найдена.");
            return null;
        }

        if (parcels.size() == 1) {
            return parcels.get(0);
        }

        // Показываем список
        System.out.println("\nНайдено несколько посылок с названием <<" + name + ">>:");
        for (int i = 0; i < parcels.size(); i++) {
            Parcel p = parcels.get(i);
            System.out.println((i + 1) + ". " + p.getType() + ", вес: " + p.getWeight() + "кг, адрес: <<"
                    + p.getDeliveryAddress() + ">>, статус: " + p.getStatus());
        }

        System.out.print("Выберите номер посылки: ");
        int choice = readValidChoice(parcels.size());
        return parcels.get(choice - 1);
    }

    private int readValidChoice(int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("Пожалуйста, введите номер: ");
                continue;
            }
            try {
                int num = Integer.parseInt(input);
                if (num >= 1 && num <= max) {
                    return num;
                } else {
                    System.out.print("Неверный номер. Введите число от 1 до " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный формат. Введите число: ");
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

        // Проверка: вес посылки не должен превышать ёмкость коробки
        if (weight > getMaxBoxCapacity(parcelType)) {
            System.out.println("Ошибка: вес посылки (" + weight + " кг) превышает максимальную ёмкость коробки для этого типа ("
                    + getMaxBoxCapacity(parcelType) + " кг).");
            return;
        }

        switch (parcelType) {
            case STANDARD -> {
                StandardParcel parcel = createStandardParcel(description, weight, deliveryAddress, sendDay);

                // Если текущая коробка отправлена — создаём новую
                if (currentStandardBox.isSent()) {
                    createNewBoxForType(ParcelCategory.STANDARD);
                }

                boolean added = currentStandardBox.addParcel(parcel);
                if (!added) {
                    // Текущая коробка заполнена — отправляем и создаём новую
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentStandardBox);
                    createNewBoxForType(ParcelCategory.STANDARD);
                    added = currentStandardBox.addParcel(parcel);
                }

                if (!added) {
                    System.out.println("Не удалось добавить посылку в коробку. Обратитесь к администратору.");
                    return;
                }

                addParcelToSystem(parcel, parcel instanceof Trackable);
            }

            case PERISHABLE -> {
                PerishableParcel parcel = createPerishableParcel(description, weight, deliveryAddress, sendDay);

                if (currentPerishableBox.isSent()) {
                    createNewBoxForType(ParcelCategory.PERISHABLE);
                }

                boolean added = currentPerishableBox.addParcel(parcel);
                if (!added) {
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentPerishableBox);
                    createNewBoxForType(ParcelCategory.PERISHABLE);
                    added = currentPerishableBox.addParcel(parcel);
                }

                if (!added) {
                    System.out.println("Не удалось добавить посылку в коробку. Обратитесь к администратору.");
                    return;
                }

                addParcelToSystem(parcel, parcel instanceof Trackable);
            }

            case FRAGILE -> {
                FragileParcel parcel = createFragileParcel(description, weight, deliveryAddress, sendDay);

                if (currentFragileBox.isSent()) {
                    createNewBoxForType(ParcelCategory.FRAGILE);
                }

                boolean added = currentFragileBox.addParcel(parcel);
                if (!added) {
                    System.out.println("Текущая коробка заполнена. Отдаем в доставку и создаем новую.");
                    sendIfNotSent(currentFragileBox);
                    createNewBoxForType(ParcelCategory.FRAGILE);
                    added = currentFragileBox.addParcel(parcel);
                }

                if (!added) {
                    System.out.println("Не удалось добавить посылку в коробку. Обратитесь к администратору.");
                    return;
                }

                addParcelToSystem(parcel, parcel instanceof Trackable);
            }
        }
    }

    private int getMaxBoxCapacity(ParcelCategory type) {
        return switch (type) {
            case STANDARD -> STANDARD_BOX_CAPACITY;
            case PERISHABLE -> PERISHABLE_BOX_CAPACITY;
            case FRAGILE -> FRAGILE_BOX_CAPACITY;
        };
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

    private ParcelBox<? extends Parcel> findBoxForParcel(Parcel target) {
        for (ParcelBox<? extends Parcel> box : allBoxes) {
            for (Parcel p : box.getAllParcels()) {
                // Сравниваем по всем ключевым полям, как в equals(), но напрямую
                if (Objects.equals(p.getDescription(), target.getDescription()) &&
                        p.getWeight() == target.getWeight() &&
                        Objects.equals(p.getDeliveryAddress(), target.getDeliveryAddress()) &&
                        p.getSendDay() == target.getSendDay() &&
                        p.getType() == target.getType()) {
                    return box;
                }
            }
        }
        throw new IllegalArgumentException("Посылка не найдена ни в одной коробке: " + target.getDescription());
    }

    public void changeCurrentDeliveryLocation() {
        if (trackableParcels.isEmpty()) {
            System.out.println("У вас нет отслеживаемых посылок.");
            return;
        }

        System.out.println("Введите название отслеживаемой посылки:");
        String parcelName = scanner.nextLine().trim();

        if (parcelName.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }

        // Ищем ТОЛЬКО отслеживаемые посылки с таким названием
        List<Parcel> candidates = trackableParcels.stream()
                .map(t -> (Parcel) t)
                .filter(p -> p.getDescription().equals(parcelName))
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            System.out.println("Отслеживаемая посылка с названием <<" + parcelName + ">> не найдена.");
            return;
        }

        Parcel parcel = chooseParcelFromList(candidates, parcelName);
        if (parcel == null) return;

        ParcelBox<? extends Parcel> box = findBoxForParcel(parcel);
        if (!box.isSent() && box.isTrackable()) {
            System.out.println("Коробка еще не отправлена, вы не можете поменять её местоположение.");
            return;
        }

        System.out.print("Введите местоположение посылки <<" + parcel.getDescription() + ">>: ");
        String newLocation = scanner.nextLine().trim();
        ((Trackable) parcel).reportStatus(newLocation);
        parcel.setCurrentDeliveryLocation(newLocation);
        parcel.changeStatus(Status.IN_TRANSIT);

        if (newLocation.equals(parcel.getDeliveryAddress())) {
            makeDeliveryCompletedForParcel(parcel); // ← новый метод!
        }
    }

    public void reportDeliveryCompleted() {
        if (allParcels.isEmpty()) {
            System.out.println("У вас нет ни одной посылки.");
            return;
        }
        System.out.println("Введите название посылки, которая была доставлена:");
        String parcelName = scanner.nextLine().trim();

        if (parcelName.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }

        List<Parcel> candidates = findParcelsByName(parcelName);
        Parcel parcel = chooseParcelFromList(candidates, parcelName);
        if (parcel != null) {
            makeDeliveryCompletedForParcel(parcel);
        }
    }

    private void makeDeliveryCompletedForParcel(Parcel parcel) {
        System.out.println("Посылка прибыла в пункт назначения.");
        System.out.println("Вынимаем из коробки.");

        ParcelBox<? extends Parcel> box = findBoxForParcel(parcel);

        @SuppressWarnings("unchecked")
        ParcelBox<Parcel> rawBox = (ParcelBox<Parcel>) box;
        rawBox.removeParcel(parcel);

        // Удаляем из списков
        allParcels.remove(parcel);
        if (parcel instanceof Trackable) {
            trackableParcels.remove(parcel);
        }

        parcel.changeStatus(Status.DELIVERED);
        System.out.println("Уведомление о поступлении посылки <<" + parcel.getDescription() + ">> отправлено получателю.");

        if (allParcels.isEmpty()) {
            System.out.println("Все посылки доставлены, можно отдыхать!");
        }
    }

    public void showCurrentDeliveryLocation() {
        if (trackableParcels.isEmpty()) {
            System.out.println("У вас нет отслеживаемых посылок.");
            return;
        }

        System.out.println("Введите название посылки: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }

        List<Parcel> candidates = trackableParcels.stream()
                .map(t -> (Parcel) t)
                .filter(p -> p.getDescription().equals(description))
                .collect(Collectors.toList());

        if (candidates.isEmpty()) {
            // Проверим, существует ли вообще такая посылка
            boolean exists = allParcels.stream()
                    .anyMatch(p -> p.getDescription().equals(description));
            if (!exists) {
                System.out.println("Посылка с таким названием не найдена.");
            } else {
                System.out.println("Посылка <<" + description + ">> не является отслеживаемой.");
            }
            return;
        }

        Parcel parcel = chooseParcelFromList(candidates, description);
        if (parcel != null) {
            System.out.println(parcel.getCurrentDeliveryLocation());
        }
    }

    public void showBoxContents() {
        if (allBoxes.isEmpty()) {
            System.out.println("У вас пока нет посылок, все коробки пусты.");
            return;
        }

        ParcelCategory parcelType = chooseParcelType();
        List<? extends ParcelBox<? extends Parcel>> boxes = switch (parcelType) {
            case STANDARD -> standardBoxes;
            case PERISHABLE -> perishableBoxes;
            case FRAGILE -> fragileBoxes;
        };

        if (boxes.isEmpty()) {
            System.out.println("Нет коробок для выбранного типа.");
            return;
        }

        boolean hasNonEmptyBox = boxes.stream()
                .anyMatch(box -> !box.getAllParcels().isEmpty());

        if (!hasNonEmptyBox) {
            System.out.println("Все коробки для " + parcelType + " пусты.");
            return;
        }

        if (boxes.size() == 1) {
            ParcelBox<? extends Parcel> onlyBox = boxes.getFirst();
            System.out.println("\nСодержимое коробки (" + parcelType + "):");
            printParcels(onlyBox.getAllParcels());
            return;
        }

        System.out.println("\nДоступные коробки для " + parcelType + ":");
        for (int i = 0; i < boxes.size(); i++) {
            ParcelBox<? extends Parcel> box = boxes.get(i);
            String status = box.isSent() ? "отправлена" : "не отправлена";
            System.out.println((i + 1) + ". Коробка №" + (i + 1) + " (" + status + "), посылок: " + box.getAllParcels().size());
        }

        System.out.print("Выберите номер коробки для просмотра содержимого: ");
        int choice = readValidBoxChoice(boxes.size());

        ParcelBox<? extends Parcel> selectedBox = boxes.get(choice - 1);
        System.out.println("\nСодержимое коробки №" + choice + " (" + parcelType + "):");
        printParcels(selectedBox.getAllParcels());
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

    public String printNumbersToChooseType() {
        return "Введите число, соответствующее типу посылки:\n"
                 + "1. Обычная посылка\n"
                 + "2. Скоропортящаяся посылка\n"
                 + "3. Хрупкая посылка\n";
    }

    public ParcelCategory chooseParcelType() {
        while (true) {
            System.out.println(printNumbersToChooseType());
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

    private int readValidBoxChoice(int max) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.print("Пожалуйста, введите номер коробки: ");
                continue;
            }
            try {
                int num = Integer.parseInt(input);
                if (num >= 1 && num <= max) {
                    return num;
                } else {
                    System.out.print("Неверный номер. Введите число от 1 до " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неверный формат. Введите число: ");
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