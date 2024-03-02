package ui;

import domain.Comanda;
import domain.Tort;
import exceptions.DuplicateObjectException;
import exceptions.RepositoryException;
import service.ComandaService;
import service.TortService;

import java.text.SimpleDateFormat;
import java.util.*;

public class Console {

    private Scanner scanner;

    private TortService tortService;
    private ComandaService comandaService;

    public Console(ComandaService comandaService, TortService tortService) {
        this.comandaService = comandaService;
        this.tortService = tortService;
        scanner = new Scanner(System.in);

    }

    private static void printMenu() {
        System.out.println("0. Afiseaza meniul: ");
        System.out.println("1. Adauga un tort: ");
        System.out.println("2. Modifica un tort: ");
        System.out.println("3. Sterge un tort: ");
        System.out.println("4. Afiseaza lista de torturi: ");
        System.out.println("5. Adauga o comanda: ");
        System.out.println("6. Modifica o comanda: ");
        System.out.println("7. Sterge o comanda: ");
        System.out.println("8. Afiseaza lista de comenzi: ");
        System.out.println("9. Numărul de torturi comandate în fiecare zi: ");
        System.out.println("10. Numărul de torturi comandate în fiecare lună a anului: ");
        System.out.println("11. Cele mai des comandate torturi: ");
        System.out.println("12. Iesire din meniu: ");

    }

    public void numarTorturi(){
        Map<String, Long> numberOfTorturiPerDay = comandaService.getNumberOfTorturiPerDay();

        System.out.println("Numarul de torturi comandate in fiecare zi:");
        numberOfTorturiPerDay.forEach((data, count) ->
                System.out.println("Data: " + data + ", Numar de torturi: " + count)
        );
    }

    public void numarTorturiPerLuna(){
        Map<String, Long> numberOfTorturiPerMonth = comandaService.getNumberOfTorturiPerMonth();

        System.out.println("Numarul de torturi comandate in fiecare luna:");
        numberOfTorturiPerMonth.forEach((month, count) ->
                System.out.println("Luna: " + month + ", Numar de torturi: " + count)
        );
    }

    public void celeMaiComandateTorturi(){
        Map<Tort, Long> mostOrderedTorturi = tortService.getMostOrderedTorturi();

        System.out.println("Cele mai des comandate torturi:");
        mostOrderedTorturi.forEach((tort, count) ->
                System.out.println(tort + ", Numar de comenzi: " + count)
        );
    }


    public void generate100Torturi() {
        int i1,i2;
        String[] tipuri = new String[]{"tarta", "tort", "prajitura", "ecler", "placinta"
                , "strudel" , "cozonac" , "pandispan" , "budinca" , "baclava" , "croissant" , "briosa" , "clatita" , "cremsnit" ,
                "foietaj" , "crema" , "vafe", "soufle"};
        String[] arome = new String[]{"ciocolata", "vanilie", "zmeura", "cirese", "caramel"
                , "afine", "capsuni", "visine", "mere", "pere", "caise", "nuca",
                "rom", "lamaie", "coacaze", "cocos", "migdale", "prune", "struguri", "banane"};
        System.out.println(tipuri.length);
        System.out.println(arome.length);
        try {
            for (int i = 6; i <= 106; i++) {
                i1 = ((int) (Math.random() * 10000)) % 18;
                i2 = ((int) (Math.random() * 10000)) % 20;
                String tip = tipuri[i1] + "_" + arome[i2];
                tortService.add(i, tip);
            }

        }catch (DuplicateObjectException e){
            System.out.println(e.getMessage());
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void generate100Comenzi()
    {
        Random random = new Random();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String[] tipuri = new String[]{"tarta", "tort", "prajitura", "ecler", "placinta"
                , "strudel" , "cozonac" , "pandispan" , "budinca" , "baclava" , "croissant" , "briosa" , "clatita" , "cremsnit" ,
                "foietaj" , "crema" , "vafe", "soufle"};
        String[] arome = new String[]{"ciocolata", "vanilie", "zmeura", "cirese", "caramel"
                , "afine", "capsuni", "visine", "mere", "pere", "caise", "nuca",
                "rom", "lamaie", "coacaze", "cocos", "migdale", "prune", "struguri", "banane"};

        try {
            int id=tortService.getAll().size()+1;
            int idTort = tortService.getAll().size();
            for(int i=1;i<=100;i++) {

                ArrayList<Tort> torturi = new ArrayList<>();

                int numberOfTorturi = 1 + random.nextInt(5);

                for (int j = 0; j < numberOfTorturi; j++) {
                    int i1 = random.nextInt(tipuri.length);
                    int i2 = random.nextInt(arome.length);
                    String tip = tipuri[i1] + "_" + arome[i2];
                    torturi.add(new Tort(j+1, tip));
                }

                long ms = -946771200000L + (Math.abs(random.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));
                String data = dateFormat.format(new Date(ms));
                comandaService.add(id, torturi, data);
                id++;

            }
        }catch (RepositoryException e){
            System.out.println(e.getMessage());
        }
    }

    private void addTort() throws RepositoryException {
        System.out.print("ID tort: ");
        int id_tort = scanner.nextInt();
        System.out.print("Tipul tortului: ");
        String tip_tort = scanner.next();
        try {
            tortService.add(id_tort, tip_tort);
            System.out.println("Tortul a fost adaugat cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addComanda() throws RepositoryException {
        System.out.print("ID comanda: ");
        int id_comanda = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Dati lista de torturi dupa id (separate de spatiu sau virgula): ");

        String torturiInput = scanner.nextLine();
        String[] torturiIds = torturiInput.split("[,\\s]+");
        ArrayList<Tort> torturi = new ArrayList<>();
        for (String tortId : torturiIds) {
            if (!tortId.isEmpty()) {
                try {
                    int id = Integer.parseInt(tortId);
                    Tort tort = tortService.findByID(id);
                    torturi.add(tort);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Tort ID: " + tortId);
                }
            }
        }
        System.out.println("Data comenzii: ");
        String data = scanner.next();
        try {
            comandaService.add(id_comanda, torturi, data);
            System.out.println("Comanda a fost adaugat cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateTort() throws RepositoryException{
        System.out.print("ID tort de modificat: ");
        int id_tort1 = scanner.nextInt();
        System.out.print("Tipul tortului de modificat: ");
        String tip_tort1 = scanner.next();
        System.out.print("ID tort nou: ");
        int id_tort2 = scanner.nextInt();
        System.out.print("Tipul tortului nou: ");
        String tip_tort2 = scanner.next();

        try {
            tortService.update(id_tort1, id_tort2, tip_tort1, tip_tort2);
            System.out.println("Tortul a fost modificat cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void updateComanda() throws RepositoryException{
        System.out.print("ID comanda de modificat: ");
        int id_comanda1 = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Dati lista de torturi dupa id (separate de spatiu sau virgula): ");

        String torturiInput1 = scanner.nextLine();
        String[] torturiIds1 = torturiInput1.split("[,\\s]+");
        ArrayList<Tort> torturi1 = new ArrayList<>();
        for (String tortId1 : torturiIds1) {
            if (!tortId1.isEmpty()) {
                try {
                    int id1 = Integer.parseInt(tortId1);
                    Tort tort1 = tortService.findByID(id1);
                    torturi1.add(tort1);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Tort ID: " + tortId1);
                }
            }
        }
        System.out.println("Data comenzii de modificat: ");
        String data1 = scanner.next();

        System.out.print("ID comanda noua: ");
        int id_comanda2 = scanner.nextInt();

        scanner.nextLine();

        System.out.print("Dati lista de torturi dupa id (separate de spatiu sau virgula): ");

        String torturiInput2 = scanner.nextLine();
        String[] torturiIds2 = torturiInput1.split("[,\\s]+");
        ArrayList<Tort> torturi2 = new ArrayList<>();
        for (String tortId2 : torturiIds1) {
            if (!tortId2.isEmpty()) {
                try {
                    int id2 = Integer.parseInt(tortId2);
                    Tort tort2 = tortService.findByID(id2);
                    torturi2.add(tort2);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Tort ID: " + tortId2);
                }
            }
        }
        System.out.println("Data comenzii noua: ");
        String data2 = scanner.next();

        try {
            comandaService.update(id_comanda1, id_comanda2, torturi1, torturi2, data1, data2);
            System.out.println("Comanda a fost modificata cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void stergereTort() throws RepositoryException{
        System.out.print("ID tort de sters: ");
        int id_tort1 = scanner.nextInt();
        try {
            tortService.remove(id_tort1);
            System.out.println("Tortul a fost sters cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void stergereComanda() throws RepositoryException{
        System.out.print("ID comanda de sters: ");
        int id_comanda1 = scanner.nextInt();
        try {
            comandaService.remove(id_comanda1);
            System.out.println("Comanda a fost stearsa cu succes!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void afisareTorturi(){
        Iterable<Tort> torturi = tortService.getAll();
        boolean found = false;
        for(Tort t: torturi){
            System.out.print(t);
            System.out.println();
            found = true;
        }
        if (!found){
            System.out.println("Nu exista niciun tort!");
        }
    }
    private void afisareComenzi(){
        Iterable<Comanda> comenzi = comandaService.getAll();
        boolean found = false;
        for(Comanda c: comenzi){
            System.out.print(c);
            System.out.println();
            found = true;
        }
        if (!found){
            System.out.println("Nu exista nicio comanda!");
        }
    }

    public void startConsole() throws RepositoryException{
        boolean run = true;
        Console.printMenu();
        while(run){
            int command = 0;
            while(true){
                try{
                    System.out.println();
                    System.out.print("Introduceti comanda: ");
                    command = scanner.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("Nu ati introdus o optiune valida.");
                }
            }
            switch (command){
                case 12:
                    System.out.println("La revedere!");
                    run = false;
                    break;
                case 0:
                    Console.printMenu();
                    break;
                case 1:
                    addTort();
                    break;
                case 2:
                    updateTort();
                    break;
                case 3:
                    stergereTort();
                    break;
                case 4:
                    afisareTorturi();
                    break;
                case 5:
                    addComanda();
                    break;
                case 6:
                    updateComanda();
                    break;
                case 7:
                    stergereComanda();
                    break;
                case 8:
                    afisareComenzi();
                    break;
                case 9:
                    numarTorturi();
                    break;
                case 10:
                    numarTorturiPerLuna();
                    break;
                case 11:
                    celeMaiComandateTorturi();
                default:
                    System.out.println("Nu ati introdus o optiune valida");
            }
        }
    }
}
