package model;

import net.datafaker.Faker;
import lombok.Getter;

@Getter
public class User {

    static Faker faker = new Faker();

    private String name;
    private String email;
    private String password;

    public User getData() {
        email = faker.internet().emailAddress().toLowerCase();
        password = faker.internet().password();
        name = faker.name().firstName();

        return this;
    }

}
