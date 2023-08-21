package helha.tems.helha_langue.restControllers;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.services.IQCMQuestionService;
import helha.tems.helha_langue.services.IResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/qcmQuestions")
@RequiredArgsConstructor
public class QCMQuestionRestController {

    @Autowired
    IQCMQuestionService qcmService;

    @GetMapping("/AllQcms")
    public  ResponseEntity<List<QCMQuestion>> getAll(){
        try {
            final List<QCMQuestion> seqs = qcmService.findAll();
            if (seqs != null || !seqs.isEmpty()) {
                return ResponseEntity.status(200).body(seqs);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @GetMapping("/Qcm")
    public ResponseEntity<QCMQuestion> get(int id){

        try {
            final QCMQuestion seq = qcmService.findById(id);
            if (seq != null) {
                return ResponseEntity.status(200).body(seq);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }
    @GetMapping("/Qcm/Response")
    public ResponseEntity<List<Response>> getAllResponse(int id){

        try {
            final List<Response> reps = qcmService.findAllResponse(id);
            if (!reps.isEmpty()) {
                return ResponseEntity.status(200).body(reps);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<QCMQuestion>> updateQcm(@PathVariable int id, @RequestBody QCMQuestion qcmQuestion)
    {
        try {
            final QCMQuestion qcm = qcmService.findById(id);
            if (qcm != null) {
                qcm.setQuestionNom(qcmQuestion.getQuestionNom());
                return ResponseEntity.status(204).body(Optional.ofNullable(qcmService.update(id, qcm)));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @PutMapping("/addResponse/{id}")
    public ResponseEntity<?> addResponseToQcm(@PathVariable int id, @RequestBody Response response) {
        try {
            final QCMQuestion qcm = qcmService.findById(id);
            if (qcm != null) {
                response.setQcmQuestion(qcm);
                qcm.getResponses().add(response);
                qcmService.update(id, qcm);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteQcm(@PathVariable int id)
    {
        QCMQuestion qcm = qcmService.findById(id);

        if (qcm == null) {
            return ResponseEntity.notFound().build();
        }
        qcmService.delete(qcm);
        Map<String, Object> res = new HashMap<>();
        res.put("message", "qcm deleted successfully.");
        return ResponseEntity.ok(res);
    }
}
