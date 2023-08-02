package helha.tems.helha_langue.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface IMP3Service {

    public String uploadMP3(@RequestParam("file") MultipartFile file) ;
    public Resource downloadMP3(@PathVariable String fileName) ;
    public String deleteMP3(@PathVariable String fileName) ;
    public String updateMP3(@PathVariable String fileName, @RequestParam("file") MultipartFile file);
}
