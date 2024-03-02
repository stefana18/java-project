package service;

import domain.Entity;
import exceptions.RepositoryException;

import java.util.Collection;

public interface IService<T extends Entity> extends Iterable<T> {


    public void remove(int id) throws RepositoryException;

    public T findByID(int id) throws RepositoryException;

    public int size();

    public Collection<T> getAll();
}
