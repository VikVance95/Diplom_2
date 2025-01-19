package constants;

import org.apache.commons.lang3.RandomStringUtils;

public class Data {
    public static String RANDOM_EMAIL = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
    public static String RANDOM_PASS = RandomStringUtils.randomNumeric(5);
    public static String RANDOM_NAME = RandomStringUtils.randomAlphabetic(10);
}
