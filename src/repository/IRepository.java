package repository;

import domain.Entity;
import exceptions.RepositoryException;

import java.util.Collection;

public interface IRepository<T extends Entity> extends Iterable<T> {
    public void add(T o) throws RepositoryException;

    public void update(T o, T o1) throws RepositoryException;

    public void remove(int id) throws RepositoryException;

    public T findByID(int id);
    public T getById(int id) throws RepositoryException;

    public int size();

    public Collection<T> getAll();
}
