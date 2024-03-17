package seminar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        User someUser = new User("SomeUser", "pa$$");
        Class<User> userClass = User.class;
        Constructor<?>[] constructors = userClass.getConstructors();
        Constructor<User> constructor = userClass.getConstructor(String.class, String.class);
        User someUser2 = constructor.newInstance("SomeUser2", "pa$$");

        System.out.println(User.getCounter());
        System.out.println(Arrays.stream(userClass.getDeclaredMethods()).toList());

        Method methodGetLogin = userClass.getMethod("getLogin");
        Method methodSetPassword = userClass.getMethod("setPassword", String.class);
        methodSetPassword.invoke(someUser, "newPass");

        System.out.println(methodGetLogin.invoke(someUser2));
        System.out.println(someUser.getPassword());

        Field password = userClass.getDeclaredField("password");
        System.out.println(password.get(someUser2));
        Field login = userClass.getDeclaredField("login");
        login.setAccessible(true);
        login.set(someUser, "newLogin");
        System.out.println(someUser.getLogin());
    }

    static class User {
        private static long counter = 0L;
        private final String login;
        private String password;

        public User(String login, String password) {
            this.login = login;
            this.password = password;
            counter++;
        }

        public User(String login) {
            this(login, "defaultPassword");
        }

        public static long getCounter() {
            return counter;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class SuperUser extends User {

        public SuperUser(String login) {
            super(login);
        }

        @Override
        public void setPassword(String password) {
            throw new UnsupportedOperationException();
        }
    }
}