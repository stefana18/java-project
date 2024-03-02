package repository;

import domain.Entity;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MemoryRepository<T extends Entity> extends AbstractRepository<T> {
    @Override
    public void add(T o) throws RepositoryException {
        if (o == null)
            throw new IllegalArgumentException("Entitatea trebuie sa existe!");
        if (findByID(o.getID()) != null)
            throw new DuplicateObjectException("Entitatea deja exista!");
        this.data.add(o);
    }

    @Override
    public void update(T oVechi, T oNou) throws  RepositoryException {
        if (oVechi == null) {
            throw new IllegalArgumentException("Entitatea trebuie sa existe!");
        }

        boolean gasit = false;
        for (int i = 0; i < this.data.size(); i++) {
            T entity = this.data.get(i);
            if (entity.getID() == oVechi.getID()) {
                this.data.set(i, oNou);
                gasit = true;
                break;
            }
        }
        if (!gasit) {
            throw new NoSuchElementException("Entity with ID " + oVechi.getID() + " not found");
        }
    }

    public void updateEntity(T elem) throws RepositoryException {
        if(findByID(elem.getID()) == null)
            throw new NoSuchElementException("ID-ul nu a fost gasit");
        for(int i  = 0;i<data.size();i++)
            if(data.get(i).getID() == elem.getID())
                data.set(i, elem);
    }

    @Override
    public void remove(int id) throws RepositoryException {
        //for (T e : this.data) {
        //   if (e.getID() == id) {
        //       this.data.remove(e);
        //   }
        //}

        Iterator<T> iterator = this.data.iterator();
        while (iterator.hasNext()) {
            T e = iterator.next();
            if (e.getID() == id) {
                iterator.remove(); // Safe removal using iterator
            }
        }
    }

    @Override
    public T findByID(int id) {
        for (T e : this.data) {
            if (e.getID() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public T getById(int id) throws RepositoryException{
        for(T elem:data)
            if(elem.getID() == id)
                return elem;
        throw new RepositoryException("Nu exista element cu acest id");
    }

    public void clear(){
        this.data.clear();
    }

    @Override
    public Collection<T> getAll() {
        return new ArrayList(this.data);
    }

    @Override
    public int size(){
        return data.size();
    }


}
