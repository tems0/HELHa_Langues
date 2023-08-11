package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.Student;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceDbImpl implements IUserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public List<User> findAll() {
        return  userRepo.findAll();
    }

    @Override
    public List<User> findAllTeachers() {
        return  userRepo.findAllTeachers();
    }

    @Override
    public List<User> findAllStudents() {
        return  userRepo.findAllStudents();
    }


    @Override
    public User saveUser(User newUser) {
        return  userRepo.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
