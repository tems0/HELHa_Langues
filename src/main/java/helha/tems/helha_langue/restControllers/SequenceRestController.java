package helha.tems.helha_langue.restControllers;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.services.IQCMQuestionService;
import helha.tems.helha_langue.services.ISequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Sequence>> updateSequence(@PathVariable int id, @RequestBody Sequence sequence)
    {
        try {
            final Sequence seq = sequenceService.findById(id);
            if (seq != null) {
                return ResponseEntity.status(204).body(Optional.ofNullable(sequenceService.update(id, sequence)));
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
        sequenceService.delete(sequence);
        return ResponseEntity.ok("Sequence and references deleted successfully.");
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


