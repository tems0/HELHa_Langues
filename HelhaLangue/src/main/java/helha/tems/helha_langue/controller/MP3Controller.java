package helha.tems.helha_langue.controller;

import helha.tems.helha_langue.services.MP3ServiceDbImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mp3")
public class MP3Controller {

    @Value("${mp3.upload.directory}")
    private String uploadDirectory; // Chemin du répertoire où les fichiers MP3 seront stockés

    // Limite de la taille du fichier en bytes (10 Mo)
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;
    private static Map<String, ClassPathResource> resourceCache = new HashMap<>();
    @Autowired
    private MP3ServiceDbImpl mp3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMP3(@RequestParam("file") MultipartFile file) {
        String response = mp3Service.uploadMP3(file);

        return ResponseEntity.ok("Le fichier MP4 a été téléchargé avec succès : " + response);
    }
    public static ClassPathResource getReloadableResource(String path) {
        if (!resourceCache.containsKey(path)) {
            ClassPathResource resource = new ClassPathResource(path);
            resourceCache.put(path, resource);
        }
        return resourceCache.get(path);
    }

    public static void reloadResource(String path) throws IOException {
        if (resourceCache.containsKey(path)) {
            ClassPathResource oldResource = resourceCache.get(path);
            ClassPathResource newResource = new ClassPathResource(path);
            resourceCache.put(path, newResource);
            oldResource.getInputStream().close(); // Close the old resource's stream
        }
    }
    @GetMapping("/getMP3/{fileName}")
    public ResponseEntity<Resource> getAudioFile(@PathVariable String fileName) throws IOException {
        String filePath = "src/main/resources/MP3/" + fileName;

        Resource resource = new FileSystemResource(filePath);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "audio/mpeg")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadMP3(@PathVariable String fileName) {
        try {
            // Charger le fichier MP3 depuis le système de fichiers
            Path path = Paths.get(uploadDirectory + File.separator + fileName);
            Resource resource = new UrlResource(path.toUri());

            // Vérifier si le fichier existe
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Construire la réponse avec le contenu du fichier MP3
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteMP3(@PathVariable String fileName) {
        try {
            // Vérifier si le fichier existe dans le répertoire de stockage
            Path filePath = Paths.get(uploadDirectory, fileName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Supprimer le fichier du système de fichiers
            Files.delete(filePath);

            return ResponseEntity.ok("Le fichier MP3 a été supprimé avec succès : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur s'est produite lors de la suppression du fichier MP3.");
        }
    }

    @PutMapping("/upload/{fileName:.+}")
    public ResponseEntity<String> updateMP3(@PathVariable String fileName, @RequestParam("file") MultipartFile file) {
        try {
            // Vérifier si le fichier est vide ou nul
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner un fichier MP3 à télécharger.");
            }

            // Vérifier la taille du fichier
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("La taille du fichier dépasse la limite autorisée (50 Mo).");
            }
            //Supprimer l'ancien MP3
            Path filePathOld = Paths.get(uploadDirectory, fileName);
            Files.delete(filePathOld);

            // Extraire le nom d'origine du fichier sans l'extension .mp3
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

            // Générer un nom de fichier logique basé sur le nom d'origine du fichier sans l'extension et l'horodatage actuel
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String newFileName = fileNameWithoutExtension + "_" + timestamp + fileExtension;

            // Stocker le fichier dans le système de fichiers en remplaçant le fichier existant (s'il existe)
            byte[] bytes = file.getBytes();
            Path filePath = Paths.get(uploadDirectory, newFileName);
            Files.write(filePath, bytes);


            return ResponseEntity.ok("Le fichier MP3 a été téléchargé avec succès : " + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement du fichier MP3.");
        }

    }
}

