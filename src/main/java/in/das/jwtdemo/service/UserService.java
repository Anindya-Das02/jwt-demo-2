package in.das.jwtdemo.service;

import in.das.jwtdemo.models.Users;
import in.das.jwtdemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users saveUser(Users users){
        log.info("saving users {} in db",users);
        users.setId(UUID.randomUUID().toString());
        users.setPassword(new BCryptPasswordEncoder().encode(users.getPassword()));
        users.setEmailId(users.getEmailId());
        Users users1 = userRepository.save(users);
        log.info("user saved {}",users1);
        return users1;
    }
}
