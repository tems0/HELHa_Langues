package helha.tems.helha_langue;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.repositories.QCMQuestionRepo;
import helha.tems.helha_langue.services.QCMQuestionServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QCMQuestionServiceDbImplTest {

    @Mock
    private QCMQuestionRepo qcmRepo;

    @InjectMocks
    private QCMQuestionServiceDbImpl qcmQuestionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        when(qcmRepo.findAll()).thenReturn(List.of(new QCMQuestion(), new QCMQuestion()));

        List<QCMQuestion> result = qcmQuestionService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindAllResponse() {
        int qcmQuestionId = 1;
        when(qcmRepo.findAllResponsesByQCMQuestionId(qcmQuestionId)).thenReturn(List.of(new Response(), new Response()));

        List<Response> result = qcmQuestionService.findAllResponse(qcmQuestionId);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindById_ExistingId() {
        QCMQuestion qcmQuestion = new QCMQuestion();
        when(qcmRepo.findById(1)).thenReturn(Optional.of(qcmQuestion));

        QCMQuestion result = qcmQuestionService.findById(1);

        assertEquals(qcmQuestion, result);
    }

    @Test
    public void testFindById_NonExistingId() {
        when(qcmRepo.findById(1)).thenReturn(Optional.empty());

        QCMQuestion result = qcmQuestionService.findById(1);

        assertNull(result);
    }

    @Test
    public void testUpdate() {
        QCMQuestion qcmQuestion = new QCMQuestion();
        qcmQuestion.setQcmQuestionId(1);

        when(qcmRepo.save(qcmQuestion)).thenReturn(qcmQuestion);

        QCMQuestion result = qcmQuestionService.update(1, qcmQuestion);

        assertEquals(qcmQuestion, result);
    }

    @Test
    public void testDelete() {
        QCMQuestion qcmQuestion = new QCMQuestion();

        qcmQuestionService.delete(qcmQuestion);

        verify(qcmRepo).delete(qcmQuestion);
    }
}

