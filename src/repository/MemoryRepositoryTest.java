package repository;

import domain.Tort;
import exceptions.RepositoryException;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.MemoryRepository;

public class MemoryRepositoryTest {
    @Test
    public void testAdd() throws RepositoryException {
        IRepository<Tort> data = new MemoryRepository<>();
        data.add(new Tort(1, "tort cu ciocolata"));
        data.add(new Tort(2, "tort cu vanilie"));
        data.add(new Tort(3, "tort cu capsuni"));
        data.add(new Tort(4, "tort cu migdale"));
        data.add(new Tort(5, "tort cu caramel"));
        assert(data.size()== 5);

    }

    @Test
    public void testRemove() throws RepositoryException{
        IRepository<Tort> data = new MemoryRepository<>();
        assert data.size() ==0;
        data.add(new Tort(1, "tort cu ciocolata"));
        data.add(new Tort(2, "tort cu vanilie"));
        data.remove(1);
        assert(data.size()==1);

    }

    @Test
    public void testFindByID() throws RepositoryException{
        IRepository<Tort> data = new MemoryRepository<>();

        data.add(new Tort(1, "tort cu ciocolata"));
        data.add(new Tort(2, "tort cu vanilie"));
        data.add(new Tort(3, "tort cu capsuni"));

        Tort tort1 = new Tort(1, "tort cu ciocolata");
        Tort tort2 = new Tort(2, "tort cu vanilie");
        Tort tort3 = new Tort(3, "tort cu capsuni");

        assert(data.getById(1).getID()==tort1.getID());
        assert(data.getById(2).getID()== tort2.getID());
        assert(data.getById(3).getID()==tort3.getID());

        assert(data.getById(1).getTip_tort().equals(tort1.getTip_tort()));
        assert(data.getById(2).getTip_tort().equals(tort2.getTip_tort()));
        assert(data.getById(3).getTip_tort().equals(tort3.getTip_tort()));

    }

    @Test
    public void testGetAll() throws RepositoryException{
        IRepository<Tort> data = new MemoryRepository<>();
        data.add(new Tort(1, "tort cu ciocolata"));
        data.add(new Tort(2, "tort cu vanilie"));
        data.add(new Tort(3, "tort cu capsuni"));
        data.add(new Tort(4, "tort cu migdale"));
        data.add(new Tort(5, "tort cu caramel"));

        assert(data.getAll().size() == 5);
    }

    @Test
    public void TestUpdate() throws RepositoryException{
        IRepository<Tort> data = new MemoryRepository<>();
        data.add(new Tort(1, "tort cu ciocolata"));
        Tort tort2 = new Tort(2, "tort cu vanilie");
        data.update(data.getById(1), tort2);
        assert(data.getById(2).getTip_tort().equals("tort cu vanilie"));

    }

}
