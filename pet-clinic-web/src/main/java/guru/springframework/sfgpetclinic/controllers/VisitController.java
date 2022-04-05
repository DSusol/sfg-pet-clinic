package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/owners/{ownerId}/pets/{petId}")
@Controller
public class VisitController {

    private final PetService petService;
    private final VisitService visitService;

    public VisitController(PetService petService, VisitService visitService) {
        this.petService = petService;
        this.visitService = visitService;
    }

    @ModelAttribute("pet")
    public Pet findPet(@PathVariable Long petId) {
        return petService.findById(petId);
    }

    @GetMapping("/visits/new")
    public String initVisitNewForm(Model model, Pet pet) {
        model.addAttribute("visit", Visit.builder().pet(pet).build());
        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping("/visits/new")
    public String processVisitNewForm(@Valid Visit visit, BindingResult bindingResult, Pet pet) {
        if( bindingResult.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        }
        visit.setPet(pet);
        Visit savedVisit = visitService.save(visit);
        pet.getVisits().add(savedVisit);
        Pet savedPet = petService.save(pet);
        return "redirect:/owners/" + savedPet.getOwner().getId();
    }

    @GetMapping("/visits/{visitId}/edit")
    public String initVisitUpdateForm(Model model, Pet pet, @PathVariable Long visitId) {
        model.addAttribute("visit", visitService.findById(visitId));
        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping("/visits/{visitId}/edit")
    public String processVisitUpdateForm(@Valid Visit visit, BindingResult bindingResult, Pet pet) {
        if( bindingResult.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        }
        visit.setPet(pet);
        Visit savedVisit = visitService.save(visit);
        pet.getVisits().add(savedVisit);
        Pet savedPet = petService.save(pet);
        return "redirect:/owners/" + savedPet.getOwner().getId();
    }
}
