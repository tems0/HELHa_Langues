package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.repositories.QCMQuestionRepo;
import helha.tems.helha_langue.repositories.ResponseRepo;
import helha.tems.helha_langue.repositories.SequenceRepo;
import helha.tems.helha_langue.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class SequenceServiceDbImpl implements ISequenceService {

    @Autowired
    SequenceRepo sequenceRepo;
    @Autowired
    QCMQuestionRepo qcmRepo;

    @Override
    public List<Sequence> findAll() {
        return  sequenceRepo.findAll();
    }

    @Override
    public Sequence update(int id, Sequence sequence) {
        Optional<Sequence> sequenceUpdate = Optional.ofNullable(sequence);
        return sequenceRepo.save(sequenceUpdate.get());
    }



    @Transactional
    public void delete(Sequence sequence) {
        for (User user : sequence.getUsers()) {
            user.getSequences().remove(sequence);
        }
        sequenceRepo.delete(sequence);
    }
    @Override
    public Sequence findById(int id) {
        try{
            return sequenceRepo.findById(id).orElseThrow();
        }
        catch (NoSuchElementException ex) {
            return null;
        }

    }

    @Override
    public Sequence add(Sequence newSeq) {
        return sequenceRepo.save(newSeq);
    }

    @Override
    public Sequence addQuestion(Sequence newSeq) {
        sequenceRepo.save(newSeq);
        Set<QCMQuestion> qcms =newSeq.getQcmQuestions();
        QCMQuestion lastQCM = qcms.stream()
                .max(Comparator.comparingInt(QCMQuestion::getQcmQuestionId))
                .orElse(null);
        Set<Response> responses = lastQCM.getResponses();

        responses.forEach(response -> response.setQcmQuestion(lastQCM));

        qcmRepo.save(lastQCM);
        return newSeq;
    }
}
