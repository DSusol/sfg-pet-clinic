package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VisitControllerTest {

    @Mock
    PetService petService;

    @Mock
    VisitService visitService;

    @InjectMocks
    VisitController visitControllerUnderTest;

    MockMvc mockMvc;

    Pet pet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(visitControllerUnderTest).build();

        Owner owner = Owner.builder().id(2L).build();
        pet = Pet.builder().id(3L).owner(owner).visits(new HashSet<>()).build();

        when(petService.findById(3L)).thenReturn(pet);
    }

    @Test
    public void initVisitNewForm() throws Exception {

        mockMvc.perform(get("/owners/2/pets/3/visits/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
                .andExpect(model().attribute("visit", instanceOf(Visit.class)));

        verify(petService).findById(3L);
    }

    @Test
    public void processVisitNewForm() throws Exception {

        when(visitService.save(any(Visit.class))).thenReturn(new Visit());
        when(petService.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/owners/2/pets/3/visits/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/2"));

        verify(petService).findById(3L);
        verify(visitService).save(any(Visit.class));
        verify(petService).save(any(Pet.class));
    }

    @Test
    public void initVisitUpdateForm() throws Exception {

        Visit visit = Visit.builder().id(4L).pet(pet).build();
        when(visitService.findById(4L)).thenReturn(visit);

        mockMvc.perform(get("/owners/2/pets/3/visits/4/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateVisitForm"))
                .andExpect(model().attribute("visit", instanceOf(Visit.class)));

        verify(petService).findById(3L);
        verify(visitService).findById(4L);
    }

    @Test
    public void processVisitUpdateForm() throws Exception {

        when(visitService.save(any(Visit.class))).thenReturn(new Visit());
        when(petService.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/owners/2/pets/3/visits/4/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/2"));

        verify(petService).findById(3L);
        verify(visitService).save(any(Visit.class));
        verify(petService).save(any(Pet.class));
    }
}