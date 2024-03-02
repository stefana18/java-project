package domain;
import java.io.Serializable;
import java.util.ArrayList;
public class Comanda extends Entity implements Serializable{
    private static final long serialVersionUID = 1L;

    ArrayList<Tort> torturi;
    private String data;

    public Comanda(int ID){
        super(ID);
    }

    public Comanda(int ID, ArrayList<Tort> torturi, String data){
        super(ID);
        this.torturi = torturi;
        this.data = data;
    }

    public Comanda(){
    }

    @Override
    public String toString(){
        return "Comanda{"+
                "torturi='" + torturi + '\''+
                ", data='" + data+'\''+
                ", ID=" + ID+
                '}';
    }

    public ArrayList<Tort> getTorturi(){
        return torturi;
    }

    public String getData() {
        return data;
    }

    public void setTorturi(ArrayList<Tort> torturi){
        this.torturi = torturi;
    }

    public void setData(String data){
        this.data = data;
    }




}

