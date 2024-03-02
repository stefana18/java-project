package domain;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    protected int ID;

    public Entity(int ID) {
        this.ID = ID;
    }

    public Entity(){
    }

    public int getID(){
        return ID;
    }

}
