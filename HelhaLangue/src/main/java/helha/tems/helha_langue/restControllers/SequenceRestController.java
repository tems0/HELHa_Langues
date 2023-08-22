package helha.tems.helha_langue.restControllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.services.IQCMQuestionService;
import helha.tems.helha_langue.services.ISequenceService;
import helha.tems.helha_langue.services.MP3ServiceDbImpl;
import helha.tems.helha_langue.services.MP4ServiceDbImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/sequences")
@RequiredArgsConstructor
public class SequenceRestController {

    @Autowired
    ISequenceService sequenceService;

    @Autowired
    IQCMQuestionService qcmService;
    @Autowired
    MP3ServiceDbImpl mp3Service;
    @Autowired
    MP4ServiceDbImpl mp4Service;

    @GetMapping("/AllSequences")
    public  ResponseEntity<List<Sequence>> getAll(){
        try {
            final List<Sequence> seqs = sequenceService.findAll();
            if (seqs != null || !seqs.isEmpty()) {
                return ResponseEntity.status(200).body(seqs);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @GetMapping("/Sequence")
    public ResponseEntity<Sequence> get(int id){

        try {
            final Sequence seq = sequenceService.findById(id);
            if (seq != null) {
                return ResponseEntity.status(200).body(seq);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @PutMapping("/WithFile/{id}")
    public ResponseEntity<Optional<Sequence>> updateSequenceWithFile(@PathVariable int id,
                                                             @RequestParam("file") MultipartFile file,
                                                             @RequestParam("seq") String sequenceJson)
    {
        try {
            //pour creer un objet sequence
            ObjectMapper objectMapper = new ObjectMapper();
            Sequence sequence = objectMapper.readValue(sequenceJson, Sequence.class);
            final Sequence seq = sequenceService.findById(id);
            seq.setLanguages(sequence.getLanguages());//pour faire un update au lieu d'un create
            seq.setTimer(sequence.getTimer());
            if (seq != null) {
                // Verification du import

                if (file.getOriginalFilename().endsWith(".mp3")) {
                    // Créer un objet File avec le chemin du fichier
                    File fileseq = new File(seq.getAudioMP3());
                    // Obtenir le nom du fichier en utilisant la méthode getName()
                    String fileName = fileseq.getName();

                    // Créer un objet File avec le chemin du fichier
                    File fileseqVid = new File(seq.getVideoMP4());
                    // Obtenir le nom du fichier en utilisant la méthode getName()
                    String fileNameVid = fileseqVid.getName();


                    mp4Service.deleteMP4(fileNameVid);

                    String response = mp3Service.updateMP3(fileName,file);
                    seq.setAudioMP3(response);
                } else if(file.getOriginalFilename().endsWith(".mp4")){
                    // Créer un objet File avec le chemin du fichier
                    File fileseq = new File(seq.getVideoMP4());
                    // Obtenir le nom du fichier en utilisant la méthode getName()
                    String fileName = fileseq.getName();

                    // Créer un objet File avec le chemin du fichier
                    File fileseqAud = new File(seq.getAudioMP3());
                    // Obtenir le nom du fichier en utilisant la méthode getName()
                    String fileNameAud = fileseqAud.getName();


                    mp3Service.deleteMP3(fileNameAud);
                    String response = mp4Service.updateMP4(fileName,file);
                    seq.setVideoMP4(response);
                }
                else{
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.status(204).body(Optional.ofNullable(sequenceService.update(id, seq)));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Sequence>> updateSequence(@PathVariable int id,
                                                             @RequestParam("seq") String sequenceJson)
    {
        try {
            //pour creer un objet sequence
            ObjectMapper objectMapper = new ObjectMapper();
            Sequence sequence = objectMapper.readValue(sequenceJson, Sequence.class);
            final Sequence seq = sequenceService.findById(id);
            seq.setLanguages(sequence.getLanguages());//pour faire un update au lieu d'un create
            seq.setTimer(sequence.getTimer());
            sequence.setSequenceId(seq.getSequenceId());//pour faire un update au lieu d'un create
            if (seq != null) {
                return ResponseEntity.status(204).body(Optional.ofNullable(sequenceService.update(id, seq)));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteSequence(@PathVariable int id)
    {
        Sequence sequence = sequenceService.findById(id);
        if (sequence == null) {
            return ResponseEntity.notFound().build();
        }
        // Verification du import
        if (sequence.getAudioMP3().endsWith(".mp3")) {
            // Créer un objet File avec le chemin du fichier
            File file = new File(sequence.getAudioMP3());
            // Obtenir le nom du fichier en utilisant la méthode getName()
            String fileName = file.getName();
            String response = mp3Service.deleteMP3(fileName);
            if(!response.startsWith("src"))
            {
                sequenceService.delete(sequence);
                return ResponseEntity.status(400).body(response);
            }
            sequenceService.delete(sequence);
            return ResponseEntity.ok("Sequence and references deleted successfully.");
        } else if(sequence.getVideoMP4().endsWith(".mp4")){
            // Créer un objet File avec le chemin du fichier
            File file = new File(sequence.getVideoMP4());
            // Obtenir le nom du fichier en utilisant la méthode getName()
            String fileName = file.getName();
            String response = mp4Service.deleteMP4(fileName);
            if(!response.startsWith("src"))
            {
                sequenceService.delete(sequence);
                return ResponseEntity.status(400).body(response);
            }
            sequenceService.delete(sequence);
            return ResponseEntity.ok("Sequence and references deleted successfully.");
        }
        else{
            sequenceService.delete(sequence);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{sequenceId}/addQuestionWithResponses")
    public ResponseEntity<Sequence> addSequenceToUser(@PathVariable int sequenceId,
                                                      @RequestBody QCMQuestion qcmQuestion) {
        try {
            // Recherchez la sequence existante par son id
            Sequence sequence = sequenceService.findById(sequenceId);
            // Ajoutez la question à la séquence
            qcmQuestion.setSequence(sequence);
            sequence.getQcmQuestions().add(qcmQuestion);
            sequence.setScore(sequence.getQcmQuestions().size());//une question un point
            // Enregistrez les modifications dans la base de données
            sequenceService.addQuestion(sequence);

            return ResponseEntity.ok(sequence);
            // Use the 'sequence' object as needed
        } catch (Exception ex) {
            // Log the exception or handle it in an appropriate way
            ex.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}


