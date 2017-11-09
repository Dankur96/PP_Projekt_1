import java.util.ArrayList;
import java.util.List;

public class WalidatorSQL{

    public static boolean sprawdz(String zapytanie) {
        String[] wyrazy = zapytanie.split(" ");
        List<String> przytnij = new ArrayList<>();
        for (String wyraz : wyrazy) {
            przytnij.add(wyraz.trim().toUpperCase());
        }
        int selectIndex = przytnij.indexOf("SELECT");
        int fromIndex = przytnij.indexOf("FROM");
        int whereIndex = przytnij.indexOf("WHERE");
        int orderIndex = przytnij.indexOf("ORDER");

        if (selectIndex == -1 && fromIndex == -1) {
            return false;
        }

        if (selectIndex < fromIndex || fromIndex < whereIndex || fromIndex < orderIndex) {
            return true;
        }

        return false;
    }


}