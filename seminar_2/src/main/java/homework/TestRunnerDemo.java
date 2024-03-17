package homework;

public class TestRunnerDemo {

    // private никому не видно
    // default (package-private) внутри пакета
    // protected внутри пакета + наследники
    // public всем

    public static void main(String[] args) {
        TestRunner.run(TestRunnerDemo.class);
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("\nStart test");
    }

    @BeforeAll
    void beforeAll() {
        System.out.println("Start testing");
    }

    @AfterEach
    void afterEach() {
        System.out.println("Finish test");
    }

    @AfterAll
    void afterAll() {
        System.out.println("\nFinish all tests");
    }

    @Test(order = 3)
    private void test1() {
        System.out.println("test1");
    }

    @Test(order = 1)
    void test2() {
        System.out.println("test2");
    }

    @Test
    void test3() {
        System.out.println("test3");
    }

    @Test
    void test4() {
        System.out.println("test4");
    }

    @Test(order = 2)
    void testAssertEquals() {
        Asserts.assertEquals(1, 3);
    }
    @Test(order = 2)
    void testAssertEquals2() {
        Asserts.assertEquals(1, 1);
    }
}
