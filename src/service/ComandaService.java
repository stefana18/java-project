package service;

import domain.Comanda;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import repository.IRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ComandaService implements IService<Comanda> {
    private IRepository<Comanda> comandaRepo;

    public ComandaService(IRepository<Comanda> comandaRepo){
        this.comandaRepo = comandaRepo;
    }

    public void add(int ID, ArrayList<Tort> torturi, String data) throws RepositoryException{
        Comanda comanda = new Comanda(ID, torturi, data);
        comandaRepo.add(comanda);
    }

    public void remove(int ID) throws RepositoryException {
        if (comandaRepo.findByID(ID) == null)
            throw new IllegalArgumentException("Comanda cu id-ul dat nu exista!");
        comandaRepo.remove(ID);
    }

    @Override
    public Comanda findByID(int id){
        if(comandaRepo.findByID(id) == null)
            throw new IllegalArgumentException("Comanda cu id-ul dat nu exista!");
        return comandaRepo.findByID(id);
    }

    @Override
    public int size() {
        return comandaRepo.size();
    }

    @Override
    public Collection<Comanda> getAll() {
        return comandaRepo.getAll();
    }

    public void update(int ID1, int ID2, ArrayList<Tort> torturi1, ArrayList<Tort> torturi2,String data1, String data2) throws RepositoryException {
        Comanda comanda1 = new Comanda(ID1, torturi1, data1);
        if(comandaRepo.findByID(ID1) == null)
            throw new IllegalArgumentException("Comanda cu id-ul dat nu exista!");
        Comanda comandaNoua = new Comanda(ID2, torturi2, data2);
        comandaRepo.update(comanda1, comandaNoua);
    }

    public Map<String, Long> getNumberOfTorturiPerDay() {
        return comandaRepo.getAll().stream()
                .collect(Collectors.groupingBy(
                        Comanda::getData,
                        Collectors.summingLong(comanda -> comanda.getTorturi().size())
                ))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e2,
                                LinkedHashMap::new
                        )
                );
    }

    public Map<String, Long> getNumberOfTorturiPerMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"); // Adjust the pattern as per your date format
        return comandaRepo.getAll().stream()
                .collect(Collectors.groupingBy(
                        comanda -> LocalDate.parse(comanda.getData(), formatter).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingLong(comanda -> comanda.getTorturi().size())
                ))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (e1, e2) -> e2,
                                LinkedHashMap::new
                        )
                );
    }

    @Override
    public Iterator<Comanda> iterator() {

        return new ArrayList<Comanda>(comandaRepo.getAll()).iterator();
    }
}
