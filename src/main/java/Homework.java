//Java Junior Home Work #1, by Istomin Sergey, group 6413
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Homework {

        public static void main(String[] args) {
            //region Лист Депортаментов
            List<Department> departments = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                departments.add(new Department("Department #" + i));
            }
            //endregion

            //region Лист Cотрудников
            List<Person> persons = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                int randomDepartmentIndex = ThreadLocalRandom.current().nextInt(departments.size());
                Department department = departments.get(randomDepartmentIndex);
                Person person = new Person();
                person.setName("Person #" + i);
                person.setAge(ThreadLocalRandom.current().nextInt(20, 65));
                person.setSalary(ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0);
                person.setDepartment(department);
                persons.add(person);
            }
            //endregion

            //region Блок проверки работы методов

            System.out.println("Работники старше 35 лет, с зарплатой больше 50 т.р. :");
            System.out.println(countPersons(persons, 35, 50000));
            System.out.println("Next chek!!!");
            System.out.println();


            System.out.println("Средняя зарплата работников, которые работают в департаменте № 2");
            System.out.println(averageSalary(persons, 2).orElse(0));
            System.out.println("Next chek!!!");
            System.out.println();


            System.out.println("Группировка работников по департаментам");
            for (var entry : groupByDepartment(persons).entrySet()) {
                System.out.print(entry.getKey().getName() + ": ");
                System.out.println(entry.getValue().size());
            }
            System.out.println("Next chek!!!");
            System.out.println();


            System.out.println("Максимальные зарплаты работников по отделам");
            for (var entry : maxSalaryByDepartment(persons).entrySet()) {
                System.out.print(entry.getKey().getName() + ": ");
                System.out.println(entry.getValue());
            }
            System.out.println("Next chek!!!");
            System.out.println();


            System.out.println("Сгруперованные имена работников по департаментам");
            for (var entry : groupPersonNamesByDepartment(persons).entrySet()) {
                System.out.print(entry.getKey().getName() + ": ");
                System.out.println(entry.getValue());
            }
            System.out.println("Next chek!!!");
            System.out.println();

            System.out.println("Сотрудники с минимальной зарплатой в своем отделе");
            minSalaryPersons(persons).forEach(System.out::println);


            //endregion
        }
        /**
        * Используя классы Person и Department, реализовать методы ниже:
        */
        //region Методы для реализации
        /**
        * Найти количество сотрудников, старше x лет с зарплатой больше, чем d
        */
        static int countPersons(List<Person> persons, int x, double d) {
            return (int) persons.stream()
                    .filter(it -> it.getAge() > x) //Фильтр по возросту
                    .filter(it -> it.getSalary() > d) //Фильтр по зарплате
                    .peek(System.out::println) //Печать отфильтрованных сотрудников
                    .count(); //Колличество сотрудников старше x лет с зарплатой больше, чем d
        }

        /**
         * Найти среднюю зарплату сотрудников, которые работают в департаменте X
         */
        static OptionalDouble averageSalary(List<Person> persons, int x) {
            return persons.stream()
                    .filter(it -> it.getDepartment().getName().contains("#" + x)) //фильтр по депортаменту
                    .mapToDouble(Person::getSalary) //получаем зарплату работника
                    .average(); //получаем среднее значение
        }

        /**
         * Сгруппировать сотрудников по департаментам
         */
        static Map<Department, List<Person>> groupByDepartment(List<Person> persons) {
            return persons.stream()
                    .collect(Collectors.groupingBy(Person::getDepartment)); //групперовка сотрудников по депортаментам
        }

        /**
         * Найти максимальные зарплаты по отделам
         */
        static Map<Department, Double> maxSalaryByDepartment(List<Person> persons) {
            return persons.stream()
                    .collect(Collectors.toMap(Person::getDepartment, //групперуем сотрудников по отделам
                            Person::getSalary, //получаем зарплату работника
                            Double::max)); //получаем максимальное значение
        }

        /**
         * ** Сгруппировать имена сотрудников по департаментам
         */
        static Map<Department, List<String>> groupPersonNamesByDepartment(List<Person> persons) {
            return persons.stream()
                    .collect(Collectors.groupingBy(Person::getDepartment, //групперовка сотрудников по депортаментам
                            Collectors.mapping(Person::getName, //получаем имена сотрудников
                                    Collectors.toList()))); //добавляем в лист
        }

        /**
         * ** Найти сотрудников с минимальными зарплатами в своем отделе
         */
        static List<Person> minSalaryPersons(List<Person> persons) {
            // В каждом департаменте ищем сотрудника с минимальной зарплатой.
            // Всех таких сотрудников собираем в список и возвращаем из метода.
            return persons.stream()
                    .collect(Collectors.toMap(Person::getDepartment, p -> p, //в каждом депортаменте ищим мин зарплату
                            (p1, p2) -> p1.getSalary() <= p2.getSalary() ? p1 : p2))
                    .values()
                    .stream()
                    .toList(); //собираем лист
        }
        //endregion

    }

