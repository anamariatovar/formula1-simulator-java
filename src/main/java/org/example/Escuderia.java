package org.example;

import com.poiji.annotation.ExcelCellName;
import org.example.data.ExcelDataImporter;
import java.util.*;
import java.util.stream.Collectors;
import static org.example.Piloto.getDataPilotos;

public class Escuderia {
    @ExcelCellName("ORDENESCUDERIAS")
    private int ordenEscuderia;
    @ExcelCellName("ESCUDERIA")
    private String nombreEquipo;
    @ExcelCellName("RANKINGCONTRUCTORES")
    private String rankingEscuderia;
    @ExcelCellName("DIRECTOR")
    private String directorGeneral;
    @ExcelCellName("PAIS")
    private String paisOrigen;
    @ExcelCellName("CAMPEONATOSGANADOS")
    private int campeonatosGanados;
    @ExcelCellName("PUNTOSACUMULADOS")
    private int puntosMundialConstructores2024;
    @ExcelCellName("PILOTO1")
    private String pilotosOficial1;
    @ExcelCellName("PILOTO2")
    private String pilotosOficial2;
    @ExcelCellName("PILOTORESERVA")
    private String pilotoReserva2024;


    private static List<Escuderia> dataListEscuderia = new ArrayList<>();

    public int getOrdenEscuderia() {
        return ordenEscuderia;
    }
    public String getNombreEquipo() {
        return nombreEquipo;
    }
    public String getDirectorGeneral() {
        return directorGeneral;
    }
    public String getPaisOrigen() {
        return paisOrigen;
    }
    public int getCampeonatosGanados() {
        return campeonatosGanados;
    }
    public int getPuntosMundialConstructores2024() {
        return puntosMundialConstructores2024;
    }
    public String getPilotosOficial1() {
        return pilotosOficial1;
    }
    public String getPilotosOficial2() {
        return pilotosOficial2;
    }
    public String getPilotoReserva2024() {
        return pilotoReserva2024;
    }
    public String getRankingEscuderia() {
        return rankingEscuderia;
    }


    public static List<Escuderia> getDataListEscuderia() {
        return dataListEscuderia;
    }
    public static void setDataListEscuderia(List<Escuderia> dataListEscuderia) {
        Escuderia.dataListEscuderia.addAll(dataListEscuderia);
    }

    public void listarEscuderias() {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Escuderia> nombreescuderias = new ArrayList<>(getDataListEscuderia());
        System.out.println("\nLISTA ESCUDERIAS\n");
        for (Escuderia escuderia : nombreescuderias) {
            System.out.println(escuderia.getOrdenEscuderia() + "." + escuderia.getNombreEquipo());
        }
    }

    public void informacionEscuderia(int opcion) {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Escuderia> infoEscuderia = new ArrayList<>(getDataListEscuderia());
        boolean encontrado = false;
        for (Escuderia escuderia : infoEscuderia) {
            if (opcion == escuderia.getOrdenEscuderia()) {
                System.out.println("\n+-----+---------------------------+-----------------------------------------+----------------+------------------------------+--------------+----------------------+----------------------+----------------------+");
                System.out.println("| No. |  NOMBRE ESCUDERÍA         |  NOMBRE DIRECTOR                        |     PAÍS       | CAMPEONATOS CONSTRUCTORES    | PUNTOS 2024  |  PILOTO OFICIAL 1    |  PILOTO OFICIAL 2    | PILOTO RESERVA       |");
                System.out.println("+-----+---------------------------+-----------------------------------------+----------------+------------------------------+--------------+----------------------+----------------------+----------------------+");
                System.out.printf(
                        "| %-3d | %-25s | %-39s | %-14s | %-28d | %-12d | %-20s | %-20s | %-20s |%n",
                        escuderia.getOrdenEscuderia(),
                        escuderia.getNombreEquipo(),
                        escuderia.getDirectorGeneral(),
                        escuderia.getPaisOrigen(),
                        escuderia.getCampeonatosGanados(),
                        escuderia.getPuntosMundialConstructores2024(),
                        escuderia.getPilotosOficial1(),
                        escuderia.getPilotosOficial2(),
                        escuderia.getPilotoReserva2024()
                );

                System.out.println("+-----+---------------------------+-----------------------------------------+----------------+------------------------------+--------------+----------------------+----------------------+----------------------+");
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("OPCION NO VALIDA");
        }
    }

    public void contructoresXEscuderia(int opcion) {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Escuderia> infoEscuderia = new ArrayList<>(getDataListEscuderia());

        Escuderia escuderiaSeleccionada = infoEscuderia.stream()
                .filter(e -> opcion == e.getOrdenEscuderia())
                .findFirst()
                .orElse(null);

        if (escuderiaSeleccionada == null) {
            System.out.println("OPCIÓN NO VÁLIDA");
            return;
        }

        List<Carrera> carreras = Carrera.getCarrerasListDto();
        List<Carrera> carrerasEscuderia = carreras.stream()//crea un stream secuencia de datos que permite procesarlos
                .filter(c -> c.getEscuderia().equalsIgnoreCase(escuderiaSeleccionada.getNombreEquipo()))//filtra los objetos de escuderia que sean igual al nombre del equipo seleccionado
                .collect(Collectors.toList());//recolecta la inf filtrada y la convierte en lista

        Map<String, Integer> resumenCarreras = new LinkedHashMap<>();

        for (Carrera carrera : carrerasEscuderia) {
            String nombre = carrera.getNombreCarrera();
            int totalPuntos = carrera.getPuntos() + carrera.getPuntosSp();

            resumenCarreras.put(nombre, resumenCarreras.getOrDefault(nombre, 0) + totalPuntos);
        }

        int puntosTotales = 0;

        System.out.println("\nINFORMACIÓN DEL MUNDIAL DE CONSTRUCTORES - ESCUDERÍA: " + escuderiaSeleccionada.getNombreEquipo());
        System.out.println("+-----------------------------------------------+--------+");
        System.out.println("| CARRERA                                       | PUNTOS |");
        System.out.println("+-----------------------------------------------+--------+");

        for (Map.Entry<String, Integer> entry : resumenCarreras.entrySet()) {
            System.out.printf("| %-45s | %-6d |%n", entry.getKey(), entry.getValue());
            puntosTotales += entry.getValue();
        }

        System.out.println("+-----------------------------------------------+--------+");
        System.out.println("TOTAL PUNTOS OBTENIDOS: " + puntosTotales);
        System.out.println("RANKING 2024: " + escuderiaSeleccionada.getRankingEscuderia());

    }

    public void pilotosXEscuderia(int opcion) {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Escuderia> infoEscuderia = new ArrayList<>(getDataListEscuderia());

        Escuderia escuderiaSeleccionada = infoEscuderia.stream()//crea un stream secuencia de datos que permite procesarlos
                .filter(e -> opcion == e.getOrdenEscuderia())//filtra los objetos de escuderia que sean igual al nombre del equipo seleccionado
                .findFirst()//recolecta la inf filtrada y la convierte en lista
                .orElse(null);

        if (escuderiaSeleccionada == null) {
            System.out.println("OPCIÓN NO VÁLIDA");
            return;
        }

        System.out.println("Escudería seleccionada: " + escuderiaSeleccionada.getNombreEquipo());

        List<Piloto> listaPilotos = getDataPilotos();
        List<Piloto> pilotosEscuderia = new ArrayList<>();
        for (Piloto p : listaPilotos) {
            if (p.getNombreApellido().equalsIgnoreCase(escuderiaSeleccionada.getPilotosOficial1())
                    || p.getNombreApellido().equalsIgnoreCase(escuderiaSeleccionada.getPilotosOficial2())) {
                pilotosEscuderia.add(p);
            }
        }
        System.out.println("\nINFORMACIÓN DEL MUNDIAL DE PILOTOS - ESCUDERÍA: " + escuderiaSeleccionada.getNombreEquipo());
        System.out.println("+---------------------------------------------+--------+-----------+");
        System.out.println("| PILOTO                                      | PUNTOS | RANKING   |");
        System.out.println("+---------------------------------------------+--------+-----------+");

        for (Piloto piloto : pilotosEscuderia) {
            System.out.printf("| %-43s | %-6d | %-9d |%n",
                    piloto.getNombreApellido(),
                    piloto.getPuntosAcumulados2024(),
                    piloto.getRanking2024());
        }
        System.out.println("+---------------------------------------------+--------+-----------+");
    }
    public void compararEscuderia(int opcion1, int opcion2){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Escuderia> escuderias = getDataListEscuderia();

        if (opcion1 < 1 || opcion1 > escuderias.size() || opcion2 < 1 || opcion2 > escuderias.size()) {
            System.out.println("HAY UNA OPCIÓN NO VÁLIDA");
            return;
        }

        Escuderia escuderia1 = escuderias.get(opcion1 - 1);
        Escuderia escuderia2 = escuderias.get(opcion2 - 1);

        String nombre1 = escuderia1.getNombreEquipo();
        String nombre2 = escuderia2.getNombreEquipo();

        List<Carrera> carreras = Carrera.getCarrerasListDto();
        List<Carrera> carrerasEscuderia1 = carreras.stream()
                .filter(c -> c.getEscuderia().equalsIgnoreCase(nombre1))
                .collect(Collectors.toList());

        List<Carrera> carrerasEscuderia2 = carreras.stream()
                .filter(c -> c.getEscuderia().equalsIgnoreCase(nombre2))
                .collect(Collectors.toList());

        // Crear mapas para agrupar puntos por carrera
        Map<String, Integer> carrerasE1 = new HashMap<>();
        Map<String, Integer> carrerasE2 = new HashMap<>();

        for (Carrera carrera : carrerasEscuderia1) {
            String nombreCarrera = carrera.getNombreCarrera();
            int puntos = carrera.getPuntos() + carrera.getPuntosSp();
            carrerasE1.put(nombreCarrera, carrerasE1.getOrDefault(nombreCarrera, 0) + puntos);
        }

        for (Carrera carrera : carrerasEscuderia2) {
            String nombreCarrera = carrera.getNombreCarrera();
            int puntos = carrera.getPuntos() + carrera.getPuntosSp();
            carrerasE2.put(nombreCarrera, carrerasE2.getOrDefault(nombreCarrera, 0) + puntos);
        }

        System.out.println("+-----------------------------------------------+--------------------------------+--------------------------------+---------------------------------------------------------+");
        System.out.printf("| %-45s | %-30s | %-30s | %-55s |%n", "CARRERA", nombre1.toUpperCase(), nombre2.toUpperCase(), "COMPARACIÓN");
        System.out.println("+-----------------------------------------------+--------------------------------+--------------------------------+---------------------------------------------------------+");

        int totalPuntos1 = 0;
        int totalPuntos2 = 0;

        Set<String> carrerasUnicas = new HashSet<>();
        carrerasUnicas.addAll(carrerasE1.keySet());
        carrerasUnicas.addAll(carrerasE2.keySet());

        for (String carrera : carrerasUnicas) {
            int puntos1 = carrerasE1.getOrDefault(carrera, 0);
            int puntos2 = carrerasE2.getOrDefault(carrera, 0);
            totalPuntos1 += puntos1;
            totalPuntos2 += puntos2;

            int diferencia = puntos1 - puntos2;

            String comparacion;
            if (diferencia > 0) {
                comparacion = "Fue mejor " + nombre1 + " por " + diferencia + " pts";
            } else if (diferencia < 0) {
                comparacion = "Fue mejor " + nombre2 + " por " + Math.abs(diferencia) + " pts";
            } else {
                comparacion = "Empate en puntos";
            }

            System.out.printf("| %-45s | %-30d | %-30d | %-55s |%n", carrera, puntos1, puntos2, comparacion);
        }

        System.out.println("+-----------------------------------------------+--------------------------------+--------------------------------+---------------------------------------------------------+");

        String resumenFinal;
        int diferenciaTotal = totalPuntos1 - totalPuntos2;
        if (diferenciaTotal > 0) {
            resumenFinal ="TOTAL: "+ nombre1 + " fue mejor por " + diferenciaTotal + " puntos";
        } else if (diferenciaTotal < 0) {
            resumenFinal = "TOTAL: "+nombre2 + " fue mejor por " + Math.abs(diferenciaTotal) + " pts";
        } else {
            resumenFinal = "Empate total";
        }

        System.out.printf("| %-45s | %-30d | %-30d | %-55s |%n", "TOTAL PUNTOS", totalPuntos1, totalPuntos2, resumenFinal);
        System.out.println("+-----------------------------------------------+--------------------------------+--------------------------------+---------------------------------------------------------+");

        System.out.println();
        System.out.println(nombre1 + " obtuvo " + totalPuntos1 + " puntos dejandolo en la temporada 2024 con la posicion Final " + escuderia1.getRankingEscuderia());
        System.out.println(nombre2 + " obtuvo " + totalPuntos2 + " puntos dejandolo en la temporada 2024 con la posicion Final " + escuderia2.getRankingEscuderia());

    }



}