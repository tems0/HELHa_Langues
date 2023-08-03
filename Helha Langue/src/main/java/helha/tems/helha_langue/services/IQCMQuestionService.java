package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;


import java.util.List;

public interface IQCMQuestionService {
    List<QCMQuestion> findAll();
    List<Response> findAllResponse(int id);
    QCMQuestion findById(int id);
    QCMQuestion add(QCMQuestion newRep);
    QCMQuestion update(int id, QCMQuestion QCMQuestion);
    void delete(QCMQuestion qcm);
}
