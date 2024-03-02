package repository;
import domain.Entity;
import domain.EntityConverter;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class TextFileRepository<T extends Entity>extends MemoryRepository<T> {
    private String fileName;

    private EntityConverter<T> converter;

    public TextFileRepository(String fileName , EntityConverter<T> converter)
    {
        this.converter = converter;
        this.fileName = fileName;
        try {
            loadFile();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(T o) throws RepositoryException
    {
        super.add(o);
        try {
            saveFile();
        }
        catch (IOException e) {
            throw new DuplicateObjectException("Error saving object ");
        }
    }

    @Override
    public void remove(int id) throws RepositoryException
    {
        super.remove(id);
        try {
            saveFile();
        }catch (IOException e){
            throw new RepositoryException("Error saving object ");
        }
    }

    @Override
    public void update(T o1 , T o2) throws RepositoryException
    {
        super.update(o1,o2);
        try {
            saveFile();
        }catch (IOException e){
            throw new RepositoryException("Error saving object");
        }
    }

    private void saveFile()throws IOException {
        try(FileWriter fw = new FileWriter(fileName)){
            for(T object:data){
                fw.write(converter.toString(object));
                fw.write("\r\n");
            }
        }
    }

    private void loadFile() throws IOException {
        data.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line = br.readLine();
            while (line!=null && !line.isEmpty()){
                data.add(converter.fromString(line));
                line= br.readLine();
            }
        }
    }

}