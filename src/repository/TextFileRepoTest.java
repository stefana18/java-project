package repository;

import domain.EntityConverter;
import domain.Tort;
import domain.TortConverter;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import org.junit.jupiter.api.Test;
import repository.IRepository;
import repository.MemoryRepository;

public class TextFileRepoTest {
    @Test
    public void testFileRepo(){
        EntityConverter<Tort> ecTort = new TortConverter();
        IRepository<Tort> repoTort = new TextFileRepository<>("C:\\Users\\Stefana\\Documents\\GitHub\\a3-stefana18\\repository\\tortTest.txt",ecTort);
        assert repoTort.getAll().size()==1;
        try{
            Tort t1 = repoTort.getById(1);
            assert "cioco".equals(t1.getTip_tort());
        }catch (RepositoryException e){
            assert false;
        }

        try {
            repoTort.add(new Tort(3,"Zmeura"));
            assert (repoTort.getAll().size()==2);
        }catch (RepositoryException e){
            assert true;
        }

        try{
            Tort t2 = repoTort.getById(3);
            repoTort.update(t2, new Tort(4, "banane"));
            t2 = repoTort.getById(4);
            assert("banane".equals(t2.getTip_tort()));
        }catch (RepositoryException e){
            assert false;
        }

        Tort tortToRemove = new Tort(5, "test");
        try {
            repoTort.add(tortToRemove);
        } catch (RepositoryException e) {
            assert false;
        }
        try{
            repoTort.remove(5);
        }catch(RepositoryException e){
            assert false;
        }
        assert repoTort.getAll().size() == 2;
    }

}
