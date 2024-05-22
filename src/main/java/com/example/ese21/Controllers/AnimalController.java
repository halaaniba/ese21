package com.example.ese21.Controllers;


import com.example.ese21.Entities.Animal;
import com.example.ese21.Entities.DownloadProfilePicDTO;
import com.example.ese21.Repisitories.AnimalRepository;
import com.example.ese21.Services.AnimalService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private AnimalService animalService;

    @PostMapping("/new")
    public Animal newAnimal(@RequestBody Animal animal){
        return animalRepository.save(animal);
    }
    @PostMapping("/{id}/upload-image")
    public Animal uploadProfileImage(@PathVariable Long id, @RequestParam MultipartFile file) throws Exception {
        return animalService.uploadNewImage(id, file);
    }

    @GetMapping("/{id}/download-image")
    public @ResponseBody byte[] getProfileImage(@PathVariable Long id, HttpServletResponse response) throws Exception {
        DownloadProfilePicDTO downloadProfilePicDTO = animalService.downloadProfilePic(id);
        String fileName = downloadProfilePicDTO.getAnimal().getProfilePicture();
        if(fileName == null) throw  new Exception("Animal does not have a profile pic");

        String extension = FilenameUtils.getExtension(fileName);
        switch (extension){
            case "gif":
                response.setContentType(MediaType.IMAGE_GIF_VALUE);
                break;
            case "jpeg":
            case "jpg":
                response.setContentType(MediaType.IMAGE_JPEG_VALUE);
                break;
            case "png":
                response.setContentType(MediaType.IMAGE_PNG_VALUE);
                break;
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        return downloadProfilePicDTO.getProfileImage();
    }

    @GetMapping("/all")
    public List<Animal> getAnimals(){
        return animalRepository.findAll();
    }

    @GetMapping("/{id}")
    public Animal getAnimal(@PathVariable Long id){
        return animalRepository.findById(id).orElse(null);
    }
    @PutMapping("/update/{id}")
    public Animal updateAnimal(@PathVariable Long id, @RequestParam Animal animal) throws Exception {
        Animal updatedAnimal = animalRepository.findById(id).orElse(null);

        if(updatedAnimal == null) throw new Exception("Animal not found");

        updatedAnimal.setColour(animal.getColour());
        updatedAnimal.setName(animal.getName());
        return animalRepository.save(updatedAnimal);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteAnimal(@PathVariable Long id) throws Exception {
        animalService.remove(id);
        animalRepository.deleteById(id);
    }
}
