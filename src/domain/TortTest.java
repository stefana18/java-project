package domain;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class TortTest {
    private Tort tort;

    @BeforeEach
    void setUp() {
        tort = new Tort(1, "tort cu ciocolata");
    }

    @Test
    void getTip_tort(){
        assert("tort cu ciocolata".equals(tort.getTip_tort()));
    }

    @Test
    void setTip_tort(){
        tort.setTip_tort("tort cu vanilie");
        assert("tort cu vanilie".equals(tort.getTip_tort()));
    }
}

