package service;

import domain.Comanda;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import repository.IRepository;
import repository.AbstractRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TortService implements IService<Tort> {
    private IRepository<Tort> tortRepo;
    private IRepository<Comanda> comandaRepo;

    public TortService(IRepository<Tort> tortRepo, IRepository<Comanda> comandaRepo) {
        this.tortRepo = tortRepo;
        this.comandaRepo = comandaRepo;
    }

    public void add(int ID, String tip_tort) throws RepositoryException{
        Tort tort = new Tort(ID, tip_tort);
        tortRepo.add(tort);

    }

    public void remove(int ID) throws RepositoryException{
        if(tortRepo.findByID(ID) == null)
            throw new IllegalArgumentException("Tortul cu id-ul dat nu exista!");
        tortRepo.remove(ID);

        for(Comanda c: comandaRepo.getAll()){
            ArrayList<Tort> torturi = c.getTorturi();
            torturi.removeIf(t-> t.getID() == ID);
            c.setTorturi(new ArrayList<>(torturi));
        }

    }

    @Override
    public Tort findByID(int id){
        if(tortRepo.findByID(id) == null)
            throw new IllegalArgumentException("Tortul cu id-ul dat nu exista!");
        return tortRepo.findByID(id);
    }

    @Override
    public int size() {
        return tortRepo.size();
    }

    @Override
    public Collection<Tort> getAll() {
        return tortRepo.getAll();
    }

    public void update(int ID1, int ID2, String tip_tort1, String tip_tort2) throws RepositoryException {
        Tort tort1 = new Tort(ID1, tip_tort1);
        Tort tort2 = new Tort(ID2, tip_tort2);
        if(tortRepo.findByID(ID1) == null)
            throw new IllegalArgumentException("Tortul cu id-ul dat nu exista!");
        if(tortRepo.findByID(ID2) !=null)
            throw new DuplicateObjectException("Tortul cu acest id deja exista!");
        tortRepo.update(tort1, tort2);
        for(Comanda c: comandaRepo.getAll()){
            ArrayList<Tort> torturi = c.getTorturi();
            for (int i = 0; i < torturi.size(); i++) {
                if (torturi.get(i).getID() == ID1) {
                    torturi.set(i, tort2);
                    break;
                }
            }
        }
    }

    public Map<Tort, Long> getMostOrderedTorturi() {
        return comandaRepo.getAll().stream()
                .flatMap(comanda -> comanda.getTorturi().stream())
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Tort, Long>comparingByValue().reversed())
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e1,
                                LinkedHashMap::new
                        )
                );
    }

    @Override
    public Iterator<Tort> iterator() {

        return new ArrayList<Tort>(tortRepo.getAll()).iterator();
    }
}