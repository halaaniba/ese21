package com.example.ese21.Services;



import com.example.ese21.Entities.Animal;
import com.example.ese21.Entities.DownloadProfilePicDTO;
import com.example.ese21.Repisitories.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AnimalService {
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private FileStorageService fileStorageService;

    public Animal uploadNewImage(Long id, MultipartFile file) throws Exception {
        Animal animal = animalRepository.findById(id).orElse(null);
        if(animal == null) throw new Exception("Animal not found");
        if(animal.getProfilePicture() != null){
            fileStorageService.removeFile(animal.getProfilePicture());
        }
        String fileName = fileStorageService.upload(file);

        animal.setProfilePicture(fileName);
        return animalRepository.save(animal);
    }

    public DownloadProfilePicDTO downloadProfilePic(Long id) throws Exception {
        Animal animal = animalRepository.findById(id).orElse(null);
        if(animal == null) throw new Exception("Animal not found");

        DownloadProfilePicDTO dto = new DownloadProfilePicDTO();
        dto.setAnimal(animal);
        if(animal.getProfilePicture() == null) return dto;

        byte[] profilePictureBytes = fileStorageService.download(animal.getProfilePicture());
        dto.setProfileImage(profilePictureBytes);

        return dto;
    }

    public void remove(Long id) throws Exception {
        Animal animal = animalRepository.findById(id).orElse(null);
        if(animal == null) throw new Exception("Animal not found");

        if(animal.getProfilePicture() != null){
            fileStorageService.removeFile(animal.getProfilePicture());
        }
        animalRepository.deleteById(id);
    }
}
