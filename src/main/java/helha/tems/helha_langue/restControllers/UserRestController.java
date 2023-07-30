package helha.tems.helha_langue.restControllers;

import helha.tems.helha_langue.config.JwUtils;
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

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    @Autowired
    IUserService userService;
    @Autowired
    EmailService mailService;
    private final AuthenticationManager authenticationManager;
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

        try {
            final UserDetails user = userDetailsService.loadUserByUsername(email);
            String token =jwtUtils.generateToken(user);
            if (user != null) {
                mailService.sendEmail(email,
                        "Connexion via Email", "votre token  -> "+token);
                boolean n= jwtUtils.isTokenValid(token,user);
                return ResponseEntity.status(200).body(token + "TON TOKEN BG");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("echec de connexion -> Email non existant");
        }
        return null;
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> post(@RequestBody User newUser){
        newUser.setEmail(newUser.getEmail().toLowerCase());
        userService.saveUser(newUser);
        return ResponseEntity.status(201).body(newUser);

    }
}
