package helha.tems.helha_langue;

import helha.tems.helha_langue.services.MP3ServiceDbImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
@TestPropertySource(properties = {"mp3.upload.directory=src/test/resources/MP3"})
public class MP3ServiceDbImplTest {

    @Value("${mp3.upload.directory}")
    private String uploadDirectory;
    @InjectMocks
    private MP3ServiceDbImpl mp3Service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mp3Service.setUploadDirectory(uploadDirectory);
    }

    @Test
    void uploadMP4_Success() throws IOException {

        String fileName = "test.mp4";
        String content = "Mock MP4 content";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "video/mp4", content.getBytes());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        String timestamp = dateFormat.format(new Date());
        String newFileName = "test_" + timestamp + ".mp4";

        String result = mp3Service.uploadMP3(file);

        String extractedFileName = result.substring(result.lastIndexOf("\\") + 1);


        String timestamp2 = extractedFileName.substring(extractedFileName.lastIndexOf('_')-8 , extractedFileName.lastIndexOf('.') );

        String shortenedTimestamp = timestamp2.substring(0, timestamp2.length() - 2);

        String newExtractedFileName = extractedFileName.substring(0, timestamp2.length() - 11);//pour enlever les deux dernier chiffres car cest des secondes
        newExtractedFileName=newExtractedFileName+'_'+ shortenedTimestamp + ".mp4";



        assertEquals(uploadDirectory + "/" + newFileName, uploadDirectory + "/" + newExtractedFileName);
        assertTrue(Files.exists(Paths.get(uploadDirectory, extractedFileName)));
        assertEquals(content, Files.readString(Paths.get(uploadDirectory, extractedFileName)));



    }

    @Test
    void deleteMP4_Success() throws IOException {
        String fileName = "testDelete.mp4";
        String content = "Mock MP4 content";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "video/mp4", content.getBytes());

        String result = mp3Service.uploadMP3(file);

        String extractedFileName = result.substring(result.lastIndexOf("\\") + 1);
        String result2 = mp3Service.deleteMP3(extractedFileName);
        String extractedFileNameDeleted = result2.substring(result2.lastIndexOf("\\") + 1);

        assertEquals(uploadDirectory + "/" + extractedFileName, uploadDirectory + "/" +extractedFileNameDeleted);
    }

    @Test
    void updateMP4_Success() throws IOException {
        String originalFileName = "testUpdated.mp4";
        MultipartFile mockFile = new MockMultipartFile(
                originalFileName,
                originalFileName,
                "video/mp4",
                new byte[1024]
        );


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
        String timestamp = dateFormat.format(new Date());
        String newFileName = "testUpdated_" + timestamp + ".mp4";


        mp3Service.setUploadDirectory(uploadDirectory);

        String result = mp3Service.updateMP3(originalFileName, mockFile);
        String extractedFileNameUpdated = result.substring(result.lastIndexOf("\\") + 1);


        String timestamp2 = extractedFileNameUpdated.substring(extractedFileNameUpdated.lastIndexOf('_')-8 , extractedFileNameUpdated.lastIndexOf('.') );

        String shortenedTimestamp = timestamp2.substring(0, timestamp2.length() - 2);//pour enlever les deux dernier chiffres car cest des secondes

        extractedFileNameUpdated = extractedFileNameUpdated.substring(0, timestamp.length() - 2);
        extractedFileNameUpdated=extractedFileNameUpdated+'_'+ shortenedTimestamp + ".mp4";

        assertEquals(uploadDirectory + "/" + newFileName,uploadDirectory + "/"+ extractedFileNameUpdated);
    }
}

