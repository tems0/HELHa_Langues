package helha.tems.helha_langue.restControllers;

import helha.tems.helha_langue.config.JwUtils;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.services.EmailService;
import helha.tems.helha_langue.services.IUserService;
import helha.tems.helha_langue.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    @Autowired
    IUserService userService;
    @Autowired
    EmailService mailService;
    private final JwUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/AllUsers")
    public List<User> get(){
        return  userService.findAll();
    }

    @GetMapping("/AllTeachers")
    public List<User> getAllTeachers(){
        return  userService.findAllTeachers();
    }

    @GetMapping("/AllStudents")
    public List<User> getAllStudents(){
        List<User> u= userService.findAllStudents();
        return  userService.findAllStudents();
    }
    @GetMapping("login/User")
    public ResponseEntity<String> authenticateEmail(@RequestParam String token ,String email) {

        try {
            final UserDetails user = userDetailsService.loadUserByUsername(email);
            if (user != null) {
                boolean n= jwtUtils.isTokenValid(token,user);
                if (n)
                {
                    return ResponseEntity.status(200).body("connecté");
                }
                else
                {
                    return ResponseEntity.status(400).body("echec de connexion -> token erroné");
                }
            }
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("echec de connexion -> Email non existant");
        }


        return null;
    }
    @GetMapping("login")
    public ResponseEntity<String> authenticate(@RequestParam String email) {
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(email);
            String token =jwtUtils.generateToken(user);
            if (user != null) {
                mailService.sendEmail(email,
                        "Connexion via Email", "votre token  -> "+token);
                boolean n= jwtUtils.isTokenValid(token,user);
                return ResponseEntity.status(200).body(token + "TON TOKEN BG");
            }
        } catch (Exception ex) {
            if(user == null)
            return ResponseEntity.status(400).body("echec de connexion -> Email non existant");
        }
        return null;
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> post(@RequestBody User newUser){
        // Convertir l'email en minuscules (au cas où l'utilisateur aurait entré l'email en majuscules)
        newUser.setEmail(newUser.getEmail().toLowerCase());

        // Vérifier si l'email se termine par "student.helha.be"
        if (newUser.getEmail().endsWith("student.helha.be")) {
            newUser.setIsTeacher("STUDENT");
        } else if(newUser.getEmail().endsWith("helha.be")){
            newUser.setIsTeacher("TEACHER");
        }
        else{
            return ResponseEntity.notFound().build();
        }

        userService.saveUser(newUser);
        return ResponseEntity.status(201).body(newUser);

    }

    @PostMapping("/{email}/sequences")
    public ResponseEntity<User> addSequenceToUser(@PathVariable String email, @RequestBody Sequence sequence) {
        // Recherche l'utilisateur existant par son email
        Optional<User> optionalUser = userService.findByEmail(email);

        // Vérifie si l'utilisateur existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        // Ajoutez la séquence à l'utilisateur
        user.getSequences().add(sequence);

        // Enregistrez les modifications dans la base de données
        userService.saveUser(user);

        return ResponseEntity.ok(user);
    }
}
