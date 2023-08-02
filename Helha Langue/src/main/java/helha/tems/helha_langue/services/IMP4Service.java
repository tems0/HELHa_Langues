package helha.tems.helha_langue.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface IMP4Service {
    public String uploadMP4(@RequestParam("file") MultipartFile file) ;
    public Resource downloadMP4(@PathVariable String fileName) ;
    public String deleteMP4(@PathVariable String fileName) ;
    public String updateMP4(@PathVariable String fileName, @RequestParam("file") MultipartFile file);
}
