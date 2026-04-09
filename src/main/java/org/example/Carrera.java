package org.example;

import com.poiji.annotation.ExcelCellName;
import org.example.data.ExcelDataImporter;
import java.util.*;

public class Carrera {
    Scanner scanner = new Scanner(System.in);
    @ExcelCellName("ORDENCARRERA")
    private int ordenCarrera;
    @ExcelCellName("NOMBRECIRCUITO")
    private String nombreCarrera;
    @ExcelCellName("PILOTO")
    private String piloto;
    @ExcelCellName("ESCUDERIA")
    private String escuderia;
    @ExcelCellName("FECHACARRERA")
    private String fechaCarrera;
    @ExcelCellName("POSICIONINICIAL")
    private int posicionInicial;
    @ExcelCellName("POSICIONFINAL")
    private int posicionFinal;
    @ExcelCellName("PUNTOS")
    private int puntos;
    @ExcelCellName("FECHASPRINT")
    private String fechasprint;
    @ExcelCellName("POSICIONINICIALSP")
    private int posicionInicialSp;
    @ExcelCellName("POSICIONFINALSP")
    private int posicionFinalSp;
    @ExcelCellName("PUNTOSSP")
    private int puntosSp;


    private static List<Carrera> carrerasListDto = new ArrayList<>();

    public String getNombreCarrera() {
        return nombreCarrera;
    }
    public String getFechaCarrera() {
        return fechaCarrera;
    }
    public String getFechasprint() {
        return fechasprint;
    }
    public String getPiloto() {
        return piloto;
    }
    public int getPosicionFinal() {
        return posicionFinal;
    }
    public int getPuntos() {
        return puntos;
    }
    public int getPosicionInicial() {
        return posicionInicial;
    }
    public int getPosicionFinalSp() {
        return posicionFinalSp;
    }
    public int getPuntosSp() {
        return puntosSp;
    }
    public int getPosicionInicialSp() {
        return posicionInicialSp;
    }
    public String getEscuderia() {
        return escuderia;
    }
    public int getOrdenCarrera() {
        return ordenCarrera;
    }
    public static List<Carrera> getCarrerasListDto() {
        return carrerasListDto;
    }
    public static void setCarrerasListDto(List<Carrera> carrerasListDto) {
        Carrera.carrerasListDto.addAll(carrerasListDto);
    }


    public void infoContructoresXCarrera(){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Carrera> infoCarrera = new ArrayList<>(getCarrerasListDto());

        System.out.println("SELECCIONE EL NUMERO DE CARRERA");
        Map<String, Integer> puntosEscuderias = new HashMap<>();
        int opcion = scanner.nextInt();
        boolean encontrado = false;
        String nombreCircuito = "";

        for (Carrera carrera : infoCarrera) {
            if (opcion == carrera.getOrdenCarrera()) {
                encontrado = true;
                nombreCircuito = carrera.getNombreCarrera();
                String escuderia = carrera.getEscuderia();
                int puntosCarrera = carrera.getPuntos();
                Integer puntosSprint = carrera.getPuntosSp();

                int totalPuntos = puntosCarrera + (puntosSprint != null ? puntosSprint : 0);

                puntosEscuderias.put(escuderia, puntosEscuderias.getOrDefault(escuderia, 0) + totalPuntos);
            }
        }

        if (!encontrado) {
            System.out.println("OPCION NO VALIDA");
            return;
        }

        System.out.println("\nINFORMACION DEL CIRCUITO: " + nombreCircuito);
        System.out.println("\n  PUNTOS ACUMULADOS POR ESCUDERÍA:");
        System.out.println("  +-----------------------------+---------+");
        System.out.println("  | ESCUDERÍA                   | PUNTOS  |");
        System.out.println("  +-----------------------------+---------+");
            //Entry es único par clave-valor dentro del Map  y entrySet() devuelve  una vista de conjunto (Set) orden-valor
            for (Map.Entry<String, Integer> inf : puntosEscuderias.entrySet()) {
            System.out.printf("  | %-27s | %-7d |%n",
                    inf.getKey(),
                    inf.getValue());
        }
        System.out.println("  +-----------------------------+---------+");
    }
    public void infoPilotosXCarrera(){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Carrera> infoCarrera = new ArrayList<>(getCarrerasListDto());

        System.out.println("SELECCIONE EL NÚMERO DE CARRERA");
        int opcion = scanner.nextInt();
        boolean encontrado = false;
        String nombreCircuito = "";
        System.out.println();

        for (Carrera carrera : infoCarrera) {
            if (carrera.getOrdenCarrera() == opcion) {
                nombreCircuito = carrera.getNombreCarrera();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("OPCIÓN NO VÁLIDA");
            return;
        }

        System.out.println("INFORMACIÓN DEL CIRCUITO: " + nombreCircuito + "\n");

        System.out.println("+-----------------------+-------------------------------+---------------------+---------------------+----------------+---------------------+---------------------+----------------+--------+");
        System.out.println("|        PILOTO         |          ESCUDERÍA            | POS INI CARRERA     | POS FIN CARRERA     | PTS CARRERA    | POS INI SPRINT      | POS FIN SPRINT      | PTS SPRINT     | TOTAL  |");
        System.out.println("+-----------------------+-------------------------------+---------------------+---------------------+----------------+---------------------+---------------------+----------------+--------+");

        for (Carrera carrera : infoCarrera) {
            if (carrera.getOrdenCarrera() == opcion) {String piloto = carrera.getPiloto();
                String escuderia = carrera.getEscuderia();
                int posIniCarrera = carrera.getPosicionInicial();
                int posFinCarrera = carrera.getPosicionFinal();
                int puntosCarrera = carrera.getPuntos();
                int posIniSprint = carrera.getPosicionInicialSp();
                int posFinSprint = carrera.getPosicionFinalSp();
                int puntosSprint = carrera.getPuntosSp();
                int total = puntosCarrera + puntosSprint;


                System.out.printf(
                        "| %-21s | %-29s | %-19d | %-19d | %-14d | %-19d | %-19d | %-14d | %-6d |%n",
                        piloto,
                        escuderia,
                        posIniCarrera,
                        posFinCarrera,
                        puntosCarrera,
                        posIniSprint,
                        posFinSprint,
                        puntosSprint,
                        total

                );
            }
        }

        System.out.println("+-----------------------+-------------------------------+---------------------+---------------------+----------------+---------------------+---------------------+----------------+--------+");
    }

}
