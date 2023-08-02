package helha.tems.helha_langue.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MP4ServiceDbImpl implements IMP4Service{

    @Value("${mp4.upload.directory}")
    private String uploadDirectory; // Chemin du répertoire où les fichiers MP4 seront stockés

    // Limite de la taille du fichier en bytes (50 Mo)
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    @Override
    public String uploadMP4(MultipartFile file) {
        try {
            // Vérifier si le fichier est vide ou nul
            if (file.isEmpty()) {
                return "Veuillez sélectionner un fichier MP4 à télécharger.";
            }

            // Vérifier la taille du fichier
            if (file.getSize() > MAX_FILE_SIZE) {
                return "La taille du fichier dépasse la limite autorisée (50 Mo).";
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


            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur s'est produite lors du téléchargement du fichier MP4.";
        }
    }

    @Override
    public Resource downloadMP4(String fileName) {
        return null;
    }

    @Override
    public String deleteMP4(String fileName) {

        try {
            // Vérifier si le fichier existe dans le répertoire de stockage
            Path filePath = Paths.get(uploadDirectory, fileName);
            if (!Files.exists(filePath)) {
                return "Le fichier MP4 n existe pas";
            }

            // Supprimer le fichier du système de fichiers
            Files.delete(filePath);

            return "Le fichier MP4 a été supprimé avec succès : " + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur s'est produite lors de la suppression du fichier MP4.";
        }
    }

    @Override
    public String updateMP4(String fileName, MultipartFile file) {

        try {
            // Vérifier si le fichier est vide ou nul
            if (file.isEmpty()) {
                return "Veuillez sélectionner un fichier MP4 à télécharger.";
            }

            // Vérifier la taille du fichier
            if (file.getSize() > MAX_FILE_SIZE) {
                return "La taille du fichier dépasse la limite autorisée (50 Mo).";
            }
            //Supprimer l'ancien MP4
            Path filePathOld = Paths.get(uploadDirectory, fileName);
            if (Files.exists(filePathOld)) {
                try {
                    Files.delete(filePathOld);
                    System.out.println("Fichier supprimé avec succès : " + filePathOld);
                } catch (IOException e) {
                    System.out.println("Une erreur est survenue lors de la suppression du fichier : " + filePathOld);
                    e.printStackTrace();
                }
            }

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


            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Une erreur s'est produite lors du téléchargement du fichier MP4.";
        }
    }

}
