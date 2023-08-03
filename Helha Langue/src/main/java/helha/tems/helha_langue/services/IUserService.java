package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.Student;
import helha.tems.helha_langue.models.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    public List<User> findAll();
    public List<User> findAllTeachers();
    public List<User> findAllStudents();
    public User save(User newUser);
    User saveUser(User newUser);
    public Optional<User> findByEmail(String email);
}
