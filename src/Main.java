import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import static java.util.concurrent.TimeUnit.SECONDS;

public class Main {


    private static final Map<DayOfWeek, List<Para>> periods = new HashMap<>();

    public static void main(String[] args) throws IOException {

        //implementacja mojego rozkladu zajec

        periods.put(DayOfWeek.MONDAY, Arrays.asList(
                new Para(new Godzina(8,15), new Godzina(9,45)),
                new Para(new Godzina(10,00), new Godzina(11,30)),
                new Para(new Godzina(11,45), new Godzina(13,15))));

        periods.put(DayOfWeek.TUESDAY, Arrays.asList(
                new Para(new Godzina(8,0), new Godzina(9,30))));

        periods.put(DayOfWeek.WEDNESDAY, Arrays.asList(
                new Para(new Godzina(8,15), new Godzina(9,45)),
                new Para(new Godzina(11,45), new Godzina(13,15))));

        periods.put(DayOfWeek.THURSDAY, Arrays.asList(
                new Para(new Godzina(8,15), new Godzina(9,45)),
                new Para(new Godzina(11,15), new Godzina(13,45))));

        periods.put(DayOfWeek.FRIDAY, Arrays.asList(
                new Para(new Godzina(8,15), new Godzina(9,45)),
                new Para(new Godzina(10,00), new Godzina(11,30)),
                new Para(new Godzina(11,45), new Godzina(13,15))));


        ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
        scheduler1.scheduleAtFixedRate(() -> {

            DayOfWeek dzis =  DayOfWeek.from(LocalDate.now());
            boolean przerwa = true;
            for (Para para : periods.get(dzis)) {
                Godzina teraz = new Godzina(LocalTime.now().getHour(), LocalTime.now().getMinute());
                if (teraz.czyWieksza(para.getGodzinaOd()) && teraz.czyMniejsza(para.getGodzinaDo())) {
                    System.out.println("Pozostało: " + teraz.roznicaMinut(para.getGodzinaDo()) + " min. do końca zajęć!");
                    przerwa = false;
                    break;
                }
            }
            if (przerwa){System.out.println("Ciesz się wolnością! Masz przerwę!");}
        }, 0, 60, SECONDS);

        // Walidator SQL

        String zapytanie; // tresc zapytania
        String nrZad; // numer zadania
        int przerwij = 1;

        Scanner czytajNrZad = new Scanner(System.in);
        Scanner czytajZapytanie = new Scanner(System.in);
        Scanner czytajPrzerwij = new Scanner(System.in);



        WalidatorSQL sqlWalidator = new WalidatorSQL();
        TreeMap<String, String> map = new TreeMap<String, String>();

        ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
        int przerwijCalkiem = przerwij;
        scheduler2.scheduleAtFixedRate(() -> {
            if (przerwijCalkiem != 0) {
                Files plik = new Files();
                plik.UsunPlik("Odp.txt");
                Set<Map.Entry<String, String>> entrySet = map.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    ZapiszPlik zapiszPlik = new ZapiszPlik();
                    try {
                        zapiszPlik.zapisz(entry.getKey(), entry.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 30, SECONDS);

        while(przerwij != 0){
            System.out.println("Wpisz numer zadania!");
            nrZad = czytajNrZad.nextLine();
            System.out.println("Wpisz zapytanie SQL!");
            zapytanie = czytajZapytanie.nextLine();

            if (WalidatorSQL.sprawdz(zapytanie) == true) {
                map.put(nrZad, zapytanie);


            }
            else {
                System.out.println("Składnie SQL niepoprawna!");
            }
            System.out.println("Próbujesz dalej? (1 = Tak || 0 = Nie)");
            przerwij = czytajPrzerwij.nextInt();


        }
        //if(stop == 0){
        Files plik2 = new Files();
        plik2.UsunPlik("Odp.txt");
        Set<Map.Entry<String,String>> entrySet = map.entrySet();
        for(Map.Entry<String, String> entry: entrySet) {
            ZapiszPlik zapiszPlik2 = new ZapiszPlik();
            zapiszPlik2.zapisz(entry.getKey(), entry.getValue());

        }
    }

}

class Para {
    private Godzina godzinaOd;
    private Godzina godzinaDo;

    public Para(Godzina godzinaOd, Godzina godzinaDo) {
        this.godzinaOd = godzinaOd;
        this.godzinaDo = godzinaDo;
    }

    public Godzina getGodzinaOd() {
        return godzinaOd;
    }

    public void setGodzinaOd(Godzina godzinaOd) {
        this.godzinaOd = godzinaOd;
    }

    public Godzina getGodzinaDo() {
        return godzinaDo;
    }

    public void setGodzinaDo(Godzina godzinaDo) {
        this.godzinaDo = godzinaDo;
    }

}

class Godzina {
    int g;
    int m;


    public Godzina(int g, int m) {
        this.g = g;
        this.m = m;

    }

    public int roznicaMinut(Godzina godzina) {

        int aktualnaGodzina = LocalTime.now().getHour() ;
        int aktualnaMinuta = LocalTime.now().getMinute();
        int roznica; // przechowuje ilosc minut do konca zajec

        int a = (godzina.g*60) + godzina.m;
        int b = (aktualnaGodzina*60) + aktualnaMinuta;
        roznica = a - b;

        return roznica;
    }
    public boolean czyWieksza(Godzina godzina) { //sprawdza czy jest pozniejsza godzina

        if(this.g > godzina.g || (this.g == godzina.g && this.m > godzina.m)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean czyMniejsza(Godzina godzina) { // sprawdza czy jest wczesniejsza godzina

        if(this.g < godzina.g || (this.g == godzina.g && this.m < godzina.m)){
            return true;
        }
        else {
            return false;
        }
    }
}

class Files{
    public static void UsunPlik(String sciezka)
    {
        try {

            File plik = new File(sciezka);
            plik.delete();

        }
        catch(Exception w) {

            w.printStackTrace();

        }
    }
}
