package model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}