package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.Response;


import java.util.List;

public interface IResponseService {
    List<Response> findAll();
    Response findById(int id);
    Response add(Response newRep);
    Response update(int id, Response response);
    void delete(Response response);
    boolean isCorrect(Response response);
}
