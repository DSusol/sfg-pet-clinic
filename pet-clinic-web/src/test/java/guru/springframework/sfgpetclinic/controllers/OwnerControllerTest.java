package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    OwnerService ownerService = mock(OwnerService.class);

    @InjectMocks
    OwnerController underTestController;

    MockMvc mockMvc;
    Set<Owner> ownerSet;

    @BeforeEach
    void setUp() {
        ownerSet = new HashSet<>();
        ownerSet.add(mock(Owner.class));
        ownerSet.add(mock(Owner.class));

        mockMvc = MockMvcBuilders.standaloneSetup(underTestController).build();
    }

    @Test
    void findOwners() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

        verifyZeroInteractions(ownerService);
    }

    @Test
    void showOwner() throws Exception {
        when(ownerService.findById(2L)).thenReturn(Owner.builder().id(2L).build());

        mockMvc.perform(get("/owners/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownerDetails"))
                .andExpect(model().attribute("owner", instanceOf(Owner.class)));

        verify(ownerService).findById(anyLong());
    }

    @Test
    void findAllOwners_toManyOwners() throws Exception {
        when(ownerService.findAllByLastName(anyString())).thenReturn(Arrays.asList(
            Owner.builder().id(1L).build(), Owner.builder().id(2L).build()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"))
                .andExpect(model().attribute("selections", hasSize(2)));

        verify(ownerService).findAllByLastName(anyString());
    }

    @Test
    void findAllOwners_toOneOwner() throws Exception {
        when(ownerService.findAllByLastName(anyString())).thenReturn(Arrays.asList(
                Owner.builder().id(2L).build()));

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:owners/2"));

        verify(ownerService).findAllByLastName(anyString());
    }

    @Test
    void findAllOwners_toNone() throws Exception {
        when(ownerService.findAllByLastName(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));

        verify(ownerService).findAllByLastName(anyString());
    }
}