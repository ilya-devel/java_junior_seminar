package homework;

public class Asserts {
    public static void assertEquals (int expected, int actual) {
        try {
            if (actual != expected)
                throw new RuntimeException("Полученное " + actual + " не соответствует ожидаемому " + expected);
            System.out.println("Результат соответствует ожидаемому");
        } catch (RuntimeException e) {
            System.err.println(e);
        }

    }
}
