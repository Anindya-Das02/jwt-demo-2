package in.das.jwtdemo.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private String id;
    private String username;
    private String password;
}
