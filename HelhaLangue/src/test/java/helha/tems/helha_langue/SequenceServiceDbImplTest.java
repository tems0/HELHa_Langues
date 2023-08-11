package helha.tems.helha_langue;

import helha.tems.helha_langue.models.QCMQuestion;
import helha.tems.helha_langue.models.Sequence;
import helha.tems.helha_langue.models.User;
import helha.tems.helha_langue.repositories.QCMQuestionRepo;
import helha.tems.helha_langue.repositories.SequenceRepo;
import helha.tems.helha_langue.services.SequenceServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SequenceServiceDbImplTest {

    @Mock
    private SequenceRepo sequenceRepo;

    @Mock
    private QCMQuestionRepo qcmQuestionRepo;

    @InjectMocks
    private SequenceServiceDbImpl sequenceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<Sequence> sequences = new ArrayList<>();
        sequences.add(new Sequence());
        sequences.add(new Sequence());

        when(sequenceRepo.findAll()).thenReturn(sequences);

        List<Sequence> result = sequenceService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testUpdate() {
        Sequence sequence = new Sequence();
        sequence.setSequenceId(1);

        when(sequenceRepo.save(sequence)).thenReturn(sequence);

        Sequence result = sequenceService.update(1, sequence);

        assertEquals(sequence, result);
    }

    @Test
    public void testDelete() {
        Sequence sequence = new Sequence();
        User user = new User();
        user.getSequences().add(sequence);
        sequence.getUsers().add(user);

        sequenceService.delete(sequence);

        verify(sequenceRepo).delete(sequence);
        assertEquals(0, user.getSequences().size());
    }

    @Test
    public void testFindById() {
        Sequence sequence = new Sequence();
        when(sequenceRepo.findById(1)).thenReturn(Optional.of(sequence));

        Sequence result = sequenceService.findById(1);

        assertEquals(sequence, result);
    }

    @Test
    public void testAdd() {
        Sequence sequence = new Sequence();
        when(sequenceRepo.save(sequence)).thenReturn(sequence);

        Sequence result = sequenceService.add(sequence);

        assertEquals(sequence, result);
    }

    @Test
    public void testAddQuestion() {
        Sequence sequence = new Sequence();
        QCMQuestion qcmQuestion = new QCMQuestion();
        qcmQuestion.setQcmQuestionId(1);
        Set<QCMQuestion> qcmQuestions = new HashSet<>();
        qcmQuestions.add(qcmQuestion);
        sequence.setQcmQuestions(qcmQuestions);

        when(sequenceRepo.save(sequence)).thenReturn(sequence);
        when(qcmQuestionRepo.save(qcmQuestion)).thenReturn(qcmQuestion);

        Sequence result = sequenceService.addQuestion(sequence);

        verify(qcmQuestionRepo).save(qcmQuestion);
        assertEquals(sequence, result);
    }
}

