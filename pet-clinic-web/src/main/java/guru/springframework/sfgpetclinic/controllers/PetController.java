package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping("/owners/{ownerId}")
public class PetController {

    private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

    private final PetService petService;
    private final OwnerService ownerService;
    private final PetTypeService petTypeService;

    public PetController(PetService petService, OwnerService ownerService, PetTypeService petTypeService) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.petTypeService = petTypeService;
    }

    @ModelAttribute("types")
    public Collection<PetType> populatePetTypes() {
        return petTypeService.findAll();
    }

    @ModelAttribute("owner")
    public Owner findOwner(@PathVariable("ownerId") Long ownerId) {
        return ownerService.findById(ownerId);
    }

    @InitBinder("owner")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/pets/new")
    public String initPetCreationForm(Owner owner, Model model) {

        model.addAttribute("pet", Pet.builder().owner(owner).build());

        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/new")
    public String processPetCreationForm(Owner owner, @Valid Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pets/createOrUpdatePetForm";
        }
        pet.setOwner(owner);
        Pet savedPet = petService.save(pet);
        owner.getPets().add(savedPet);
        Owner savedOwner = ownerService.save(owner);
        return "redirect:/owners/" + savedOwner.getId();
    }

    @GetMapping("/pets/{petId}/edit")
    public String initPetUpdateForm(Owner owner, @PathVariable Long petId, Model model) {
        model.addAttribute("pet", petService.findById(petId));
        return "pets/createOrUpdatePetForm";
    }

    @PostMapping("/pets/{petId}/edit")
    public String processPetUpdateForm(Owner owner, @PathVariable Long petId, @Valid Pet pet, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pets/createOrUpdatePetForm";
        }
        pet.setOwner(owner);
        Pet savedPet = petService.save(pet);
        owner.getPets().add(savedPet);
        Owner savedOwner = ownerService.save(owner);
        return "redirect:/owners/" + savedOwner.getId();
    }
}
