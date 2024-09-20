package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import java.util.List;

public class RandomDataUtils {

    private static final Faker faker = new Faker();
    private static final List<String> categories = List.of(
            "Обучение", "Рестораны", "Продукты", "Игры", "Здоровье", "Путешествие", "Увлечения", "Спорт",
            "Ремонт", "Автомобиль", "Штрафы", "Коммуналка", "Подарки", "Техника"
    );


    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }


    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        return categories.get(faker.random().nextInt(0 ,categories.size()));
    }


    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
