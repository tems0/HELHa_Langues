package helha.tems.helha_langue;

import helha.tems.helha_langue.models.Response;
import helha.tems.helha_langue.repositories.ResponseRepo;
import helha.tems.helha_langue.services.ResponseServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResponseServiceDbImplTest {

    @Mock
    private ResponseRepo responseRepo;

    @InjectMocks
    private ResponseServiceDbImpl responseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        when(responseRepo.findAll()).thenReturn(List.of(new Response(), new Response()));

        List<Response> result = responseService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindById_ExistingId() {
        Response response = new Response();
        when(responseRepo.findById(1)).thenReturn(Optional.of(response));

        Response result = responseService.findById(1);

        assertEquals(response, result);
    }

    @Test
    public void testFindById_NonExistingId() {
        when(responseRepo.findById(1)).thenReturn(Optional.empty());

        Response result = responseService.findById(1);

        assertNull(result);
    }

    @Test
    public void testUpdate() {
        Response response = new Response();
        response.setResponseId(1);

        when(responseRepo.save(response)).thenReturn(response);

        Response result = responseService.update(1, response);

        assertEquals(response, result);
    }

    @Test
    public void testDelete() {
        Response response = new Response();

        responseService.delete(response);

        verify(responseRepo).delete(response);
    }

    @Test
    public void testIsCorrect_True() {
        Response response = new Response();
        response.setResponseCorrect(true);

        boolean result = responseService.isCorrect(response);

        assertTrue(result);
    }

    @Test
    public void testIsCorrect_False() {
        Response response = new Response();
        response.setResponseCorrect(false);

        boolean result = responseService.isCorrect(response);

        assertFalse(result);
    }
}

