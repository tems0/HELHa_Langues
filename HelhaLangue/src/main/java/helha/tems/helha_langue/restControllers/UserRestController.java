package helha.tems.helha_langue.restControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helha.tems.helha_langue.config.JwUtils;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.Student;
import helha.tems.helha_langue.models.Teacher;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    MP3ServiceDbImpl mp3Service;
    @Autowired
    MP4ServiceDbImpl mp4Service;
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
    public ResponseEntity<Map<String, Object>> authenticateEmail(@RequestParam String token ,String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            final UserDetails user = userDetailsService.loadUserByUsername(email);
            if (user != null) {
                boolean n= jwtUtils.isTokenValid(token,user);
                if (n)
                {
                    Optional<User> optionalUser  = userService.findByEmail(email);
                    User userLoged = optionalUser.get();
                    String u =userLoged.getEst_professeur();
                    if(u.equals("STUDENT"))
                        response.put("message", "STUDENT");
                    else
                        response.put("message", "TEACHER");
                    return ResponseEntity.status(200).body(response);
                }
                else
                {
                    response.put("message", "echec de connexion -> token erroné");
                    return ResponseEntity.status(400).body(response);
                }
            }
        } catch (Exception ex) {
            response.put("message", "echec de connexion -> Email non existant");
            return ResponseEntity.status(400).body(response);
        }


        return null;
    }
    @GetMapping("login")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestParam String email) {
        UserDetails user = null;
        try {
            user = userDetailsService.loadUserByUsername(email);
            String token =jwtUtils.generateToken(user);
            if (user != null) {
                mailService.sendEmail(email,
                        "Connexion via Email", "votre token  -> "+token);
                boolean n= jwtUtils.isTokenValid(token,user);
                // Créez un objet Map pour représenter les données à renvoyer
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("message", "Connexion réussie. Voici votre token.");
                return ResponseEntity.status(200).body(response);
            }
        } catch (Exception ex) {
            if(user == null)
            {
                Map<String, Object> response = new HashMap<>();
                response.put("token", null);
                response.put("message", "Échec de connexion. Email non existant.");

                // Renvoyez la réponse JSON avec le statut 400 Bad Request
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        return null;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Map<String, Object>> post(@RequestBody User newUser){
        // Convertir l'email en minuscules (au cas où l'utilisateur aurait entré l'email en majuscules)
        newUser.setEmail(newUser.getEmail().toLowerCase());
        Optional<User> optionalUser = userService.findByEmail(newUser.getEmail().toLowerCase());
        Map<String, Object> response = new HashMap<>();

        // Vérifie si l'utilisateur existe
        if (!optionalUser.isEmpty()) {
            response.put("message", "Adresse email deja existante.");
            return ResponseEntity.status(400).body(response);
        }
        // Vérifier si l'email se termine par "student.helha.be"
        if (newUser.getEmail().endsWith("student.helha.be")) {
            newUser.setEst_professeur("STUDENT");
            Student student = new Student();
            student.setEmail(newUser.getEmail());
            student.setLastName(newUser.getLastName());
            student.setFirstName(newUser.getFirstName());
            userService.saveUser(student); // Enregistrez l'étudiant dans la base de données
            response.put("message", "Compte créé.");
            return ResponseEntity.ok(response);
        } else if(newUser.getEmail().endsWith("helha.be")){
            newUser.setEst_professeur("TEACHER");
            newUser.setEst_professeur("TEACHER");
            Teacher teacher = new Teacher();
            teacher.setEmail(newUser.getEmail());
            teacher.setLastName(newUser.getLastName());
            teacher.setFirstName(newUser.getFirstName());
            userService.saveUser(teacher);
            response.put("message", "Compte créé.");
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/{email}/sequences")
    public ResponseEntity<Map<String, Object>> addSequenceToUser(@PathVariable String email,@RequestParam("file") MultipartFile file ,
                                                   @RequestParam("seq") String sequenceJson) throws JsonProcessingException {
        // Recherche l'utilisateur existant par son email
        Optional<User> optionalUser = userService.findByEmail(email);
        ObjectMapper objectMapper = new ObjectMapper();
        Sequence sequence = objectMapper.readValue(sequenceJson, Sequence.class);

        // Vérifie si l'utilisateur existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        // Vérifier si l'email se termine par "student.helha.be"
        if (user.getEmail().endsWith("student.helha.be")) {
            sequence.setCompleted(true);
        } else if(user.getEmail().endsWith("helha.be")){
            sequence.setCompleted(false);
        }
        else{
            return ResponseEntity.notFound().build();
        }
        // Verification du import
        Map<String, Object> res = new HashMap<>();
        if (file.getOriginalFilename().endsWith(".mp3")) {
            String response = mp3Service.uploadMP3(file);
            if(!response.startsWith("src")) {
                res.put("token", null);
                res.put("message", response);
                return ResponseEntity.status(400).body(res);
            }
            sequence.setAudioMP3(response);
        } else if(file.getOriginalFilename().endsWith(".mp4")){
            String response = mp4Service.uploadMP4(file);
            if(!response.startsWith("src")){
                res.put("token", null);
                res.put("message", response);
                return ResponseEntity.status(400).body(res);
            }
            sequence.setVideoMP4(response);
        }
        else{
            return ResponseEntity.notFound().build();
        }
        // Ajoutez la séquence à l'utilisateur
        user.getSequences().add(sequence);

        // Enregistrez les modifications dans la base de données
        userService.saveUser(user);
        res.put("token", null);
        res.put("message", "sequence creer");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/{email}/sequencesWithoutFile")
    public ResponseEntity<Map<String, Object>> addSequenceToUserWithoutFile(@PathVariable String email,
                                                                 @RequestParam("seq") String sequenceJson) throws JsonProcessingException {
        // Recherche l'utilisateur existant par son email
        Optional<User> optionalUser = userService.findByEmail(email);
        ObjectMapper objectMapper = new ObjectMapper();
        Sequence sequence = objectMapper.readValue(sequenceJson, Sequence.class);

        // Vérifie si l'utilisateur existe
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = optionalUser.get();
        // Vérifier si l'email se termine par "student.helha.be"
        if (user.getEmail().endsWith("student.helha.be")) {
            sequence.setCompleted(true);
        } else if(user.getEmail().endsWith("helha.be")){
            sequence.setCompleted(false);
        }
        else{
            return ResponseEntity.notFound().build();
        }
        boolean sequenceExists = false;
        for (Sequence existingSequence : user.getSequences()) {
            if (existingSequence.getSequenceId() == sequence.getSequenceId()) {
                // Mise à jour de la séquence existante
                existingSequence.setCompleted(sequence.getCompleted());
                existingSequence.setScoreGot(sequence.getScoreGot());
                sequenceExists = true;
                break;
            }
        }
        // Si la séquence n'existe pas, l'ajouter à la liste
        if (!sequenceExists) {
            user.getSequences().add(sequence);
        }
        // Verification du import
        Map<String, Object> res = new HashMap<>();

        // Enregistrez les modifications dans la base de données
        userService.saveUser(user);
        res.put("message", "sequence terminé pour l'etudiant");
        return ResponseEntity.ok(res);
    }
}
