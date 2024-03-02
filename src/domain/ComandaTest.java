package domain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class ComandaTest {

    private Comanda comanda;

    @BeforeEach
    void setUp(){
        Tort tort1 = new Tort(1,"tort cu ciocolata" );
        Tort tort2 = new Tort(2, "tort cu vanilie");
        ArrayList<Tort> listaTorturi = new ArrayList<Tort>();
        listaTorturi.add(tort1);
        listaTorturi.add(tort2);
        comanda = new Comanda(1, listaTorturi, "20.11.2023");

    }

    @Test
    void getTorturi(){
        assert("tort cu vanilie".equals(comanda.torturi.get(1).getTip_tort()));

    }

    @Test
    void getData(){
        assert("20.11.2023".equals(comanda.getData()));
    }

    @Test
    void setData(){
        comanda.setData("30.11.2023");
        assert("30.11.2023".equals(comanda.getData()));
    }

    @Test
    void setTorturi(){
        Tort tort3 = new Tort(3, "tort cu migdale");
        Tort tort4 = new Tort(4, "tort cu caramel sarat");
        ArrayList<Tort> listaTorturi2 = new ArrayList<Tort>();
        listaTorturi2.add(tort3);
        listaTorturi2.add(tort4);
        comanda.setTorturi(listaTorturi2);
        assert("tort cu migdale".equals(comanda.torturi.get(0).getTip_tort()));
    }

}

