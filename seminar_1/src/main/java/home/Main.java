package home;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        ISomeAction tmp = () -> System.out.println("Hello world!");
        tmp.action();
        ISomeAction2 tmp2 = x -> x * x;
        System.out.println(tmp2.action(3));

        Function<Integer, Integer> pow2 = x -> x * x;
        System.out.println(pow2.apply(5));

        UnaryOperator<Integer> cube = x -> x * x * x;
        System.out.println(cube.apply(3));

        Consumer<String> consumer = str -> System.out.println(str);
        consumer.accept("Hello world");

        Supplier<Integer> supplier = () -> new Random().nextInt(101);
        System.out.println(supplier.get());

        Runnable runnable = () -> System.out.println("It's running");
        runnable.run();

        Predicate<Integer> evenTester = n -> n % 2 == 0;
        System.out.println(evenTester.test(3));
        System.out.println(evenTester.test(6));
        System.out.println(evenTester.test(9));

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integers.add(supplier.get());
        }

        Comparator<Integer> customComporator = (x, y) -> {
            if (x % 2 == 0 && y % 2 != 0) {
                return -1;
            } else if (x % 2 != 0 && y % 2 == 0) {
                return 1;
            }
            return Integer.compare(x, y);
        };
        System.out.println(integers);
        integers.sort(customComporator);
        System.out.println(integers);
//        System.out.println(integers.stream().filter(s -> s % 2 == 0).toList().stream().reduce((x, y) -> x + y, ));

        integers.stream().filter(num -> num < 50).map(n -> n*2).forEach(System.out::println);

        Stream.generate(supplier).limit(5).toList();
    }

    public interface ISomeAction {
        void action();
    }

    public interface ISomeAction2 {
        int action(int x);
    }
}