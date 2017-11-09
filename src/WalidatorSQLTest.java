import org.junit.Test;

import static org.junit.Assert.*;

public class WalidatorSQLTest {

    private WalidatorSQL sqlValidator = new WalidatorSQL();

    @Test
    public void test1() {
        assertTrue(WalidatorSQL.sprawdz("SELECT sth FROM smw WHERE smwissth"));
    }

    @Test
    public void test2() {
        assertFalse(WalidatorSQL.sprawdz("FROM smw SELECT sth WHERE smwiswth"));
    }



}