package helha.tems.helha_langue;

import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.repositories.UserRepo;
import helha.tems.helha_langue.services.UserServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceDbImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceDbImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepo.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllTeachers() {
        List<User> teachers = new ArrayList<>();
        teachers.add(new User());

        when(userRepo.findAllTeachers()).thenReturn(teachers);

        List<User> result = userService.findAllTeachers();

        assertEquals(1, result.size());
    }

    @Test
    public void testFindAllStudents() {
        List<User> students = new ArrayList<>();
        students.add(new User());

        when(userRepo.findAllStudents()).thenReturn(students);

        List<User> result = userService.findAllStudents();

        assertEquals(1, result.size());
    }

    @Test
    public void testSaveUser() {
        User newUser = new User();

        when(userRepo.save(newUser)).thenReturn(newUser);

        User result = userService.saveUser(newUser);

        assertEquals(newUser, result);
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        Optional<User> user = Optional.of(new User());

        when(userRepo.findByEmail(email)).thenReturn(user);

        Optional<User> result = userService.findByEmail(email);

        assertEquals(user, result);
    }
}
