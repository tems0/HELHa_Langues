package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.repositories.QCMQuestionRepo;
import helha.tems.helha_langue.repositories.ResponseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class QCMQuestionServiceDbImpl implements IQCMQuestionService {

    @Autowired
    QCMQuestionRepo qcmRepo;

    @Override
    public List<QCMQuestion> findAll() {
        return qcmRepo.findAll();
    }

    @Override
    public List<Response> findAllResponse(int id) {
        return qcmRepo.findAllResponsesByQCMQuestionId(id);
    }

    @Override
    public QCMQuestion findById(int id) {
        try{
            return qcmRepo.findById(id).orElseThrow();
        }
        catch (NoSuchElementException ex) {
            return null;
        }
    }

    @Override
    public QCMQuestion add(QCMQuestion newRep) {
        return null;
    }

    @Override
    public QCMQuestion update(int id, QCMQuestion qcmQuestion) {
        Optional<QCMQuestion> responseUpdate = Optional.ofNullable(qcmQuestion);
        return qcmRepo.save(responseUpdate.get());
    }

    @Override
    public void delete(QCMQuestion qcm) {
        qcmRepo.delete(qcm);
    }
}
