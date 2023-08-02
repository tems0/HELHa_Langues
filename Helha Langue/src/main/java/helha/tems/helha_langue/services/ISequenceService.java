package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;

import java.util.List;
import java.util.Optional;

public interface ISequenceService {
    List<Sequence> findAll();
    Sequence findById(int id);
    Sequence add(Sequence newSeq);
    Sequence addQuestion(Sequence newSeq);

    Sequence update(int id, Sequence sequence);
    void delete(Sequence sequence);
}
