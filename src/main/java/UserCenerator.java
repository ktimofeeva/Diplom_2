import org.apache.commons.lang3.RandomStringUtils;

public class UserCenerator {

    public static User getDefault() {
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String name = RandomStringUtils.randomAlphabetic(5);

        return new User(email, password, name);
    }

    public static User getWithoutEmail() {
        final String password = RandomStringUtils.randomAlphabetic(5);
        final String name = RandomStringUtils.randomAlphabetic(5);

        return new User(null,password, name);
    }

    public static User getWithoutPassword() {
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String name = RandomStringUtils.randomAlphabetic(5);

        return new User(email, null, name);
    }

    public static User getWithoutName() {
        final String email = RandomStringUtils.randomAlphabetic(10)+"@yandex.ru";
        final String password = RandomStringUtils.randomAlphabetic(5);

        return new User(email,password, null);
    }
}


