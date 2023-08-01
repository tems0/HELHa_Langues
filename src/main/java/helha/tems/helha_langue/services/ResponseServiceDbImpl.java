package helha.tems.helha_langue.services;

import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.repositories.ResponseRepo;
import helha.tems.helha_langue.repositories.SequenceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ResponseServiceDbImpl implements IResponseService{


    @Autowired
    ResponseRepo responseRepo;


    @Override
    public List<Response> findAll() {
        return responseRepo.findAll();
    }

    @Override
    public Response findById(int id) {
        try{
            return responseRepo.findById(id).orElseThrow();
        }
        catch (NoSuchElementException ex) {
            return null;
        }
    }

    @Override
    public Response add(Response newRep) {
        return null;
    }

    @Override
    public Response update(int id, Response response) {
        Optional<Response> responseUpdate = Optional.ofNullable(response);
        return responseRepo.save(responseUpdate.get());
    }

    @Override
    public void delete(Response response) {
        responseRepo.delete(response);
    }

    @Override
    public boolean isCorrect(Response response) {
        return response.getResponseCorrect();
    }
}
