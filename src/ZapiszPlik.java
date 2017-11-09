import java.io.*;


public class ZapiszPlik{

    public static void zapisz(String zadanie, String zapytanie) throws IOException {
        FileWriter plik = new FileWriter("Odp.txt", true);
        BufferedWriter doPliku = new BufferedWriter(plik);
        plik.write("Zadanie nr: [" + zadanie +"] [=>] Odpowiedź: " + zapytanie + "\n\r");
        doPliku.close();

    }
}