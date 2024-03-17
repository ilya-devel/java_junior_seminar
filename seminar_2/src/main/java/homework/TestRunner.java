package homework;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    public static void run(Class<?> testClass) {
        final Object testObj = initTestObj(testClass);

        Method beforeEach = getBeforeEach(testClass);
        Method beforeAll = getBeforeAll(testClass);
        Method afterEach = getAfterEach(testClass);
        Method afterAll = getAfterAll(testClass);


        if (beforeAll != null) {
            try {
                beforeAll.invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        for (Method testMethod : getTestMethods(testClass, Test.class)) {
            try {
                if (beforeEach != null) beforeEach.invoke(testObj);
                testMethod.invoke(testObj);
                if (afterEach != null) afterEach.invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        if (afterAll != null) {
            try {
                afterAll.invoke(testObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static List<Method> getTestMethods(Class<?> testClass, Class<Test> testClass1) {
        Map<Method, Integer> mapLst = new HashMap<>();
        for (Method testMethod : testClass.getDeclaredMethods()) {
            if (testMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                continue;
            }

            if (testMethod.getAnnotation(testClass1) != null) {
                mapLst.put(testMethod, testMethod.getAnnotation(testClass1).order());
            }
        }

        return mapLst.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();
    }


    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }

    private static Method getBeforeEach(Class<?> testClass) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getAnnotation(BeforeEach.class) != null) {
                return method;
            }
        }
        return null;
    }

    private static Method getAfterEach(Class<?> testClass) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getAnnotation(AfterEach.class) != null) {
                return method;
            }
        }
        return null;
    }

    private static Method getBeforeAll(Class<?> testClass) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getAnnotation(BeforeAll.class) != null) {
                return method;
            }
        }
        return null;
    }

    private static Method getAfterAll(Class<?> testClass) {
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.getAnnotation(AfterAll.class) != null) {
                return method;
            }
        }
        return null;
    }


}
