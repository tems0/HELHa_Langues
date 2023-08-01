package helha.tems.helha_langue.repositories;

import helha.tems.helha_langue.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {


    @Query("SELECT u FROM User u WHERE u.IsTeacher= 'STUDENT'")
    public List<User> findAllStudents();

    @Query("SELECT u FROM User u WHERE u.IsTeacher= 'TEACHER'")
    public List<User> findAllTeachers();

    Optional<User> findByEmail(String email);

}