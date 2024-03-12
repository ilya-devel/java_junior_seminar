package homeworks;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            departments.add(new Department("Department #" + i));
        }

        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            persons.add(new Person(
                    "Person #" + new Random().nextInt(1000),
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }
        System.out.println("\nTask1");
        new Homework().printNamesOrdered(persons);

        System.out.println("\nTask2");
        new Homework().printDepartmentOldestPerson(persons).values()
                .forEach(x -> System.out.println(x.getDepartment() + ", salary: " + x));

        System.out.println("\nTask3");
        new Homework().findFirstPersons(persons).forEach(System.out::println);

        System.out.println("\nTask4");
        System.out.println(new Homework().findTopDepartment(persons));
    }
}
