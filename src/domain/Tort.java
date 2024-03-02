package domain;
import java.io.Serializable;
public class Tort extends Entity implements Serializable{
    private static final long serialVersionUID = 1L;

    private String tip_tort;

    public Tort(int ID, String tip_tort){
        super(ID);
        this.tip_tort = tip_tort;
    }

    public Tort(){
    }

    @Override
    public String toString(){
        return "Tort{"+
                "tip_tort='" + tip_tort + '\''+
                ", ID=" + ID+
                '}';
    }

    public String getTip_tort(){
        return tip_tort;
    }

    public void setTip_tort(String tipTort){
        this.tip_tort = tipTort;
    }

    public Tort(int ID){
        super(ID);
    }
}
