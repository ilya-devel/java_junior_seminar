package homeworks;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Homework {

    /**
     * Реалзиовать методы, описанные ниже:
     */

    /**
     * Вывести на консоль отсортированные (по алфавиту) имена персонов
     */
    public void printNamesOrdered(List<Person> persons) {
        persons.stream().sorted((x, y) -> x.getName().compareTo(y.getName())).forEach(System.out::println);
    }

    /**
     * В каждом департаменте найти самого взрослого сотрудника.
     * Вывести на консоль мапипнг department -> personName
     * Map<Department, Person>
     */
    public Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        return persons.stream().collect(Collectors.toMap(Person::getDepartment, Function.identity(), (first, second) -> {
            if (first.getAge() > second.getAge()) return first;
            return second;
        }));
    }

    /**
     * Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
     */
    public List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(person -> person.getAge() < 30)
                .filter(person -> person.getSalary() > 50_000)
                .limit(10).toList();
    }

    /**
     * Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
     */
    public Department findTopDepartment(List<Person> persons) {
        System.out.println("\nОбъём общего заработка по департаментам:");
        persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment, Collectors.summingDouble(Person::getSalary)))
                .entrySet().forEach(System.out::println);
        System.out.print("\nЛидер: ");
        return persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment, Collectors.summingDouble(Person::getSalary)))
                .entrySet().stream().max(Map.Entry.comparingByValue(Comparator.comparingDouble(value -> value)))
                .get().getKey();
    }

}