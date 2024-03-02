package repository;

import domain.Entity;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileRepository<T extends Entity>extends MemoryRepository<T>{
    private String fileName;

    public BinaryFileRepository(String fileName) throws IOException, ClassNotFoundException {
        this.fileName = fileName;
        loadFile();
    }

    @Override
    public void add(T o) throws RepositoryException
    {
        super.add(o);
        try {
            saveFile();
        }catch (IOException e) {
            throw new DuplicateObjectException("Error saving file " +e.getMessage());
        }
    }

    @Override
    public void remove(int id) throws RepositoryException
    {
        super.remove(id);
        try {
            saveFile();
        }catch (IOException e) {
            throw new RepositoryException("Error saving file " + e.getMessage());
        }
    }

    @Override
    public void update(T o1 , T o2) throws RepositoryException
    {
        super.update(o1,o2);
        try {
            saveFile();
        }catch (IOException e){
            throw new RepositoryException("error saving file "+e.getMessage());
        }
    }

    private void loadFile() throws IOException , ClassNotFoundException{
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            this.data = (ArrayList<T>) ois.readObject();
        }
        catch (FileNotFoundException e){
            System.out.println("Repo starting a new file");
        }finally {
            if(ois!=null)
                ois.close();
        }
    }

    private void saveFile() throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(data);
        }
    }
}
