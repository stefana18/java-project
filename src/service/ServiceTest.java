package service;

import domain.Comanda;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import repository.IRepository;
import repository.MemoryRepository;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ServiceTest {

    @Test
    public void testTortService() {
        IRepository<Tort> tortRepo = new MemoryRepository<>();
        IRepository<Comanda> comandaRepo = new MemoryRepository<>();
        TortService tortService = new TortService(tortRepo, comandaRepo);

        // Test adding a Tort
        int tortId = 1;
        try {
            tortService.add(tortId, "ciocolata");
            assertEquals(1, tortService.getAll().size());
        } catch (RepositoryException e) {
            fail("Adding a valid Tort should not throw an exception.");
        }

        // Test removing a Tort
        try {
            tortService.remove(tortId);
            assertEquals(0, tortService.getAll().size());
        } catch (RepositoryException e) {
            fail("Removing an existing Tort should not throw an exception.");
        }

        // Test updating a Tort
        int updatedTortId = 2;
        try {
            tortService.add(tortId, "vanilie");
            tortService.update(tortId, updatedTortId, "ciocolata", "caramel");
            assertEquals("caramel", tortService.findByID(updatedTortId).getTip_tort());
        } catch (RepositoryException e) {
            fail("Updating a Tort should not throw an exception.");
        }
    }

    @Test
    public void testComandaService() {
        IRepository<Comanda> comandaRepo = new MemoryRepository<>();
        ComandaService comandaService = new ComandaService(comandaRepo);

        // Test adding a Comanda
        ArrayList<Tort> torturi = new ArrayList<>();
        int comandaId = 1;
        String data = "2023-12-10";
        try {
            comandaService.add(comandaId, torturi, data);
            assertEquals(1, comandaService.size());
        } catch (RepositoryException e) {
            fail("Adding a valid Comanda should not throw an exception.");
        }

        // Test removing a Comanda
        try {
            comandaService.remove(comandaId);
            assertEquals(0, comandaService.size());
        } catch (RepositoryException e) {
            fail("Removing an existing Comanda should not throw an exception.");
        }

        // Test updating a Comanda
        int updatedComandaId = 2;
        try {
            comandaService.add(comandaId, torturi, data);
            comandaService.update(comandaId, updatedComandaId, new ArrayList<>(), new ArrayList<>(), "2023-12-11", "2023-12-12");
            assertEquals("2023-12-12", comandaService.findByID(updatedComandaId).getData());
        } catch (RepositoryException e) {
            fail("Updating a Comanda should not throw an exception.");
        }
    }
}
