package repository;

import domain.Entity;
import domain.Tort;
import exceptions.RepositoryException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryFileRepoTest {

    @Test
    public void testBinaryFileRepository() {
        // Provide a valid file path for the test
        String filePath = "C:\\Users\\Stefana\\Documents\\GitHub\\a3-stefana18\\repository\\binaryFileTest.bin";

        // Initialize the repository
        BinaryFileRepository<Entity> repoEntity = null;
        try {
            repoEntity = new BinaryFileRepository<>(filePath);
        } catch (IOException | ClassNotFoundException e) {
            fail("Failed to initialize BinaryFileRepository");
        }

        // Test adding an entity to the repository
        Tort tort = new Tort(1, "Example");
        try {
            repoEntity.add(tort);
        } catch (RepositoryException e) {
            fail("Failed to add entity to the repository");
        }

        // Verify the entity was added successfully
        try {
            assertEquals(1, repoEntity.size());
            assertEquals(tort, repoEntity.getById(1));
        } catch (RepositoryException e) {
            fail("Failed to retrieve added entity from the repository");
        }

        // Test removing an entity from the repository
        try {
            repoEntity.remove(1);
        } catch (RepositoryException e) {
            fail("Failed to remove entity from the repository");
        }

        // Verify the entity was removed successfully
        try {
            assertEquals(0, repoEntity.size());
            assertNull(repoEntity.getById(1));
        } catch (RepositoryException e) {
            assertTrue(e instanceof RepositoryException);
        }

        // Test updating an entity in the repository
        Tort updatedTort = new Tort(2, "UpdatedExample");
        try {
            repoEntity.add(tort);
            repoEntity.update(tort, updatedTort);
        } catch (RepositoryException e) {
            fail("Failed to update entity in the repository");
        }

        // Verify the entity was updated successfully
        try {
            assertEquals(updatedTort, repoEntity.getById(2));
        } catch (RepositoryException e) {
            fail("Failed to retrieve updated entity from the repository");
        }

        // Clean up - delete the test file created during testing
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}

