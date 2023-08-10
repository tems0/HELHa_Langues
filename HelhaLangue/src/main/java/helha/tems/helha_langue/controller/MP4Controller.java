package helha.tems.helha_langue.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mp4")
public class MP4Controller {

    @Value("${mp4.upload.directory}")
    private String uploadDirectory; // Chemin du répertoire où les fichiers MP4 seront stockés

    // Limite de la taille du fichier en bytes (10 Mo)
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMP4(@RequestParam("file") MultipartFile file) {
        try {
            // Vérifier si le fichier est vide ou nul
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner un fichier MP4 à télécharger.");
            }

            // Vérifier la taille du fichier
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("La taille du fichier dépasse la limite autorisée (50 Mo).");
            }

            // Vérifier si le répertoire de stockage existe, sinon le créer
            Path directoryPath = Paths.get(uploadDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            // Extraire le nom d'origine du fichier sans l'extension .mp4
            String originalFileName = file.getOriginalFilename();
            String fileNameWithoutExtension = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));

            // Générer un nom de fichier logique basé sur le nom d'origine du fichier sans l'extension et l'horodatage actuel
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String fileName = fileNameWithoutExtension + "_" + timestamp + fileExtension;


            // Stocker le fichier dans le système de fichiers
            byte[] bytes = file.getBytes();
            Path filePath = Paths.get(uploadDirectory, fileName);
            Files.write(filePath, bytes);


            return ResponseEntity.ok("Le fichier MP4 a été téléchargé avec succès : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement du fichier MP4.");
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadMP4(@PathVariable String fileName) {
        try {
            // Charger le fichier MP4 depuis le système de fichiers
            Path path = Paths.get(uploadDirectory + File.separator + fileName);
            Resource resource = new UrlResource(path.toUri());

            // Vérifier si le fichier existe
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Construire la réponse avec le contenu du fichier MP4
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/getMP4/{fileName}")
    public ResponseEntity<Resource> getVideoFile(@PathVariable String fileName) {
        String filePath = "src/main/resources/MP4/" + fileName;

        Resource resource = new FileSystemResource(filePath);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "video/mp4")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteMP4(@PathVariable String fileName) {
        try {
            // Vérifier si le fichier existe dans le répertoire de stockage
            Path filePath = Paths.get(uploadDirectory, fileName);
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Supprimer le fichier du système de fichiers
            Files.delete(filePath);

            return ResponseEntity.ok("Le fichier MP4 a été supprimé avec succès : " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur s'est produite lors de la suppression du fichier MP4.");
        }
    }

    @PutMapping("/upload/{fileName:.+}")
    public ResponseEntity<String> updateMP4(@PathVariable String fileName, @RequestParam("file") MultipartFile file) {
        try {
            // Vérifier si le fichier est vide ou nul
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner un fichier MP4 à télécharger.");
            }

            // Vérifier la taille du fichier
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().body("La taille du fichier dépasse la limite autorisée (50 Mo).");
            }
            //Supprimer l'ancien MP4
            Path filePathOld = Paths.get(uploadDirectory, fileName);
            Files.delete(filePathOld);

            // Extraire le nom d'origine du fichier sans l'extension .mp4
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


            return ResponseEntity.ok("Le fichier MP4 a été téléchargé avec succès : " + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement du fichier MP4.");
        }
    }
}

