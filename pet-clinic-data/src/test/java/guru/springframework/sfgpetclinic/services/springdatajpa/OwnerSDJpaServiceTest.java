package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerSDJpaServiceTest {

    OwnerRepository ownerRepository = mock(OwnerRepository.class);
    PetRepository petRepository = mock(PetRepository.class);
    PetTypeRepository petTypeRepository = mock(PetTypeRepository.class);

    @InjectMocks
    OwnerSDJpaService serviceUnderTest;

    final Long id = 1L;
    final String lastName = "TestLastName";
    Owner owner;

    @BeforeEach
    void setUp() {
        owner = Owner.builder().id(id).lastName(lastName).build();
    }

    @Test
    void findByLastName() {
        when(ownerRepository.findByLastName(lastName)).thenReturn(owner);
        Owner foundOwner = serviceUnderTest.findByLastName(lastName);

        verify(ownerRepository).findByLastName(any());
        assertEquals(foundOwner, owner);
    }

    @Test
    void findAll() {
        Set<Owner> owners = new HashSet<>();
        owners.add(owner);
        owners.add(mock(Owner.class));
        when(ownerRepository.findAll()).thenReturn(owners);

        assertEquals(2, serviceUnderTest.findAll().size());
        verify(ownerRepository).findAll();
    }

    @Test
    void findById_notNullable() {
        when(ownerRepository.findById(id)).thenReturn(Optional.of(owner));
        Owner returnedOwner = serviceUnderTest.findById(id);

        assertEquals(owner, returnedOwner);
        assertNotNull(returnedOwner);
        verify(ownerRepository).findById(anyLong());
    }

    @Test
    void findById_Nullable() {
        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Owner returnedOwner = serviceUnderTest.findById(id);

        assertNull(returnedOwner);
        verify(ownerRepository).findById(anyLong());
    }

    @Test
    void save() {
        when(ownerRepository.save(owner)).thenReturn(owner);
        Owner returnedOwner = serviceUnderTest.save(owner);

        assertEquals(owner, returnedOwner);
        verify(ownerRepository).save(any());
    }

    @Test
    void delete() {
        serviceUnderTest.delete(owner);

        verify(ownerRepository).delete(any());
    }

    @Test
    void deleteById() {
        serviceUnderTest.deleteById(id);

        verify(ownerRepository).deleteById(anyLong());
    }

    @Test
    void findAllByLastName() {
        Owner owner2 = Owner.builder().lastName("AlsoTest").build();
        List<Owner> owners = new ArrayList<>();
        owners.add(owner);
        owners.add(owner2);

        when(ownerRepository.findAllByLastNameContainingIgnoreCase("Test")).thenReturn(owners);
        List<Owner> checkedOwnerList = serviceUnderTest.findAllByLastName("Test");

        assertEquals(2, checkedOwnerList.size());
        verify(ownerRepository).findAllByLastNameContainingIgnoreCase(anyString());
    }
}