package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PetControllerTest {

    @Mock
    OwnerService ownerService;

    @Mock
    PetService petService;

    @Mock
    PetTypeService petTypeService;

    @InjectMocks
    PetController petControllerUnderTest;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(petControllerUnderTest).build();
    }

    @Test
    void initPetCreationForm() throws Exception {

        when(ownerService.findById(2L)).thenReturn(Owner.builder().id(2L).build());

        mockMvc.perform(get("/owners/2/pets/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"))
                .andExpect(model().attribute("pet", instanceOf(Pet.class)));

        verify(ownerService).findById(anyLong());
    }

    @Test
    void processPetCreationForm() throws Exception {

        when(petService.save(any(Pet.class))).thenReturn(new Pet());
        when(ownerService.findById(2L)).thenReturn(Owner.builder().id(2L).pets(new HashSet<>()).build());
        when(ownerService.save(any(Owner.class))).thenReturn(Owner.builder().id(2L).build());

        mockMvc.perform(post("/owners/2/pets/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/2"));

        verify(ownerService).findById(anyLong());
        verify(petService).save(any(Pet.class));
        verify(ownerService).save(any(Owner.class));
    }

    @Test
    void initPetUpdateForm() throws Exception {

        when(ownerService.findById(2L)).thenReturn(Owner.builder().id(2L).build());
        when(petService.findById(3L)).thenReturn(Pet.builder().id(3L).build());

        mockMvc.perform(get("/owners/2/pets/3/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdatePetForm"));

        verify(ownerService).findById(anyLong());
        verify(petService).findById(anyLong());
    }

    @Test
    void processPetUpdateForm() throws Exception {

        when(petService.save(any(Pet.class))).thenReturn(new Pet());
        when(ownerService.findById(2L)).thenReturn(Owner.builder().id(2L).pets(new HashSet<>()).build());
        when(ownerService.save(any(Owner.class))).thenReturn(Owner.builder().id(2L).build());

        mockMvc.perform(post("/owners/2/pets/3/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/2"));

        verify(ownerService).findById(anyLong());
        verify(petService).save(any(Pet.class));
        verify(ownerService).save(any(Owner.class));
    }
}