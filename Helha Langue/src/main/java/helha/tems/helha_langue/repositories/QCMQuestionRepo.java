package helha.tems.helha_langue.repositories;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QCMQuestionRepo extends JpaRepository<QCMQuestion,Integer> {

    @Query("SELECT res FROM Response res WHERE res.qcmQuestion.qcmQuestionId = :qcmQuestionId")
    List<Response> findAllResponsesByQCMQuestionId(@Param("qcmQuestionId") int qcmQuestionId);

}
