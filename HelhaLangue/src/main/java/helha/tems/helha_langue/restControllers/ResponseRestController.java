package helha.tems.helha_langue.restControllers;

import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.services.IQCMQuestionService;
import helha.tems.helha_langue.services.IResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ResponseRestController {


    @Autowired
    IResponseService responseService;

    @GetMapping("/AllRepsonses")
    public  ResponseEntity<List<Response>> getAll(){
        try {
            final List<Response> seqs = responseService.findAll();
            if (seqs != null || !seqs.isEmpty()) {
                return ResponseEntity.status(200).body(seqs);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @GetMapping("/Response")
    public ResponseEntity<Response> get(int id){

        try {
            final Response seq = responseService.findById(id);
            if (seq != null) {
                return ResponseEntity.status(200).body(seq);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Response>> updateResponse(@PathVariable int id, @RequestBody Response response)
    {
        try {
            final Response rep = responseService.findById(id);
            if (rep != null) {
                rep.setResponse(response.getResponse());
                rep.setResponseCorrect(response.getResponseCorrect());
                return ResponseEntity.status(204).body(Optional.ofNullable(responseService.update(id, rep)));
            }
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(null);
        }
        return  ResponseEntity.status(404).body(null);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Map<String, Object>> deleteResponse(@PathVariable int id)
    {
        Response response = responseService.findById(id);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> res = new HashMap<>();
        res.put("message", "response deleted successfully.");
        responseService.delete(response);
        return ResponseEntity.ok(res);
    }
}
