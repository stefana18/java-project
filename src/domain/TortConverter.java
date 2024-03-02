package domain;

public class TortConverter implements EntityConverter<Tort> {

    @Override
    public String toString(Tort t){
        return t.getID()+","+t.getTip_tort();
    }

    public Tort fromString(String line)
    {
        String[] tokens = line.split(",");
        return new Tort(Integer.parseInt(tokens[0]),tokens[1]);
    }
}
