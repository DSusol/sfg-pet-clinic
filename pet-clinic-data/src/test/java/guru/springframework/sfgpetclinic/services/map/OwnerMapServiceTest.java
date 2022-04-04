package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OwnerMapServiceTest {

    OwnerMapService ownerMapService;

    final Long ownerId = 1L;
    final String lastName = "TestLastName";
    Owner testOwner = Owner.builder().id(ownerId).lastName(lastName).build();

    @BeforeEach
    void setUp() {
        ownerMapService = new OwnerMapService(new PetTypeMapService(), new PetMapService());

        ownerMapService.save(testOwner);
    }

    @Test
    void findAll() {
        Set<Owner> ownerSet = ownerMapService.findAll();

        assertEquals(1, ownerSet.size());
    }

    @Test
    void findById() {
        Owner owner = ownerMapService.findById(ownerId);

        assertNotNull(owner);
        assertEquals(ownerId, owner.getId());
    }

    @Test
    void save_forExistingId() {
        Long newId = 2L;

        Owner owner2 = ownerMapService.save(Owner.builder().id(newId).build());

        assertEquals(2, ownerMapService.findAll().size());
        assertEquals(owner2, ownerMapService.findById(newId));
    }

    @Test
    void save_forNullId() {
        Owner owner2 = ownerMapService.save(mock(Owner.class));

        assertEquals(2, ownerMapService.findAll().size());
        assertNotNull(owner2.getId());

    }

    @Test
    void delete() {
        ownerMapService.delete(testOwner);

        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void deleteById() {
        ownerMapService.deleteById(ownerId);

        assertEquals(0, ownerMapService.findAll().size());
    }

    @Test
    void findByLastName() {
        Owner owner = ownerMapService.findByLastName(lastName);

        assertNotNull(owner);

        assertEquals(ownerId, owner.getId());
    }

    @Test
    void findByLastName_notFound() {
        Owner owner = ownerMapService.findByLastName("Not to be found");

        assertNull(owner);
    }

    @Test
    void findAllByLastName() {
        //todo
    }
}