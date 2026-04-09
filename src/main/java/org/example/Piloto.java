package org.example;

import com.poiji.annotation.ExcelCellName;
import org.example.data.ExcelDataImporter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Piloto {
    Scanner scanner = new Scanner(System.in);
    @ExcelCellName("ORDENPILOTO")
    private int ordenPiloto;
    @ExcelCellName("NOMBREPILOTO")
    private String nombreApellido;
    @ExcelCellName("EQUIPO")
    private String equipo;
    @ExcelCellName("EDAD")
    private int edad;
    @ExcelCellName("PAIS")
    private String paisOrigen;
    @ExcelCellName("CAMPGANADOS")
    private int campeonatosGanados;
    @ExcelCellName("CARRERASDISP")
    private int carrerasDisputadas;
    @ExcelCellName("PUNTOS2024")
    private int puntosAcumulados2024;
    @ExcelCellName("RANKING2024")
    private int ranking2024;
    @ExcelCellName("PUESTO")
    private String puesto;

    private static List<Piloto> dataPilotos= new ArrayList<>();
    public int getOrdenPiloto() {
        return ordenPiloto;
    }
    public String getNombreApellido() {
        return nombreApellido;
    }
    public String getEquipo() {
        return equipo;
    }
    public int getEdad() {
        return edad;
    }
    public String getPaisOrigen() {
        return paisOrigen;
    }
    public int getCampeonatosGanados() {
        return campeonatosGanados;
    }
    public int getCarrerasDisputadas() {
        return carrerasDisputadas;
    }
    public int getPuntosAcumulados2024() {
        return puntosAcumulados2024;
    }
    public String getPuesto() {return puesto;}
    public int getRanking2024() {
        return ranking2024;
    }
    public static List<Piloto> getDataPilotos() {
        return dataPilotos;
    }
    public static void setDataPilotos(List<Piloto> dataPilotos) {
        Piloto.dataPilotos.addAll(dataPilotos);
    }

    public void listarPilotos(){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Piloto> Pilotos = new ArrayList<>(getDataPilotos());

        int total = Pilotos.size();
        int mitad = (int) Math.ceil(total / 2.0);
        System.out.println("\nLISTA PILOTOS\n");
        for (int i = 0; i < mitad; i++) {

            Piloto pilotoA = Pilotos.get(i);
            String columna1 = String.format("%-30s", pilotoA.getOrdenPiloto() + ". " + pilotoA.getNombreApellido());

            String columna2 = "";
            if (i + mitad < total) {
                Piloto pilotoB = Pilotos.get(i + mitad);
                columna2  = (pilotoB.getOrdenPiloto() + ". " + pilotoB.getNombreApellido());
            }
            System.out.println(columna1 + columna2);
        }
    }

    public void informacionPiloto(){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Piloto> infoPiloto = new ArrayList<>(getDataPilotos());

        System.out.println("SELECCIONE EL NÚMERO DEL PILOTO");
        int opcion = scanner.nextInt();
        boolean encontrado = false;

        System.out.println();
        System.out.println("+-----+------------------------+------------------------------+------+------------------------+---------------------+---------------------+-------------+--------------+-----------+");
        System.out.println("| No. |        NOMBRE          |           EQUIPO             | EDAD |     PAÍS DE ORIGEN     | CAMPEONATOS GANADOS | CARRERAS DISPUTADAS | PUNTOS 2024 | RANKING 2024 |  PUESTO   |");
        System.out.println("+-----+------------------------+------------------------------+------+------------------------+---------------------+---------------------+-------------+--------------+-----------+");

        for (Piloto piloto : infoPiloto) {
            if (opcion == piloto.getOrdenPiloto()) {
                String ranking = (piloto.getRanking2024() == 0) ? "NO APLICA" : String.valueOf(piloto.getRanking2024());
                System.out.printf("| %-3d | %-22s | %-28s | %-4d | %-22s | %-19d | %-19d | %-11d | %-12s | %-9s |%n",
                        piloto.getOrdenPiloto(),
                        piloto.getNombreApellido(),
                        piloto.getEquipo(),
                        piloto.getEdad(),
                        piloto.getPaisOrigen(),
                        piloto.getCampeonatosGanados(),
                        piloto.getCarrerasDisputadas(),
                        piloto.getPuntosAcumulados2024(),
                        ranking,
                        piloto.getPuesto()
                );
                System.out.println("+-----+------------------------+------------------------------+------+------------------------+---------------------+---------------------+-------------+--------------+-----------+");
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("OPCIÓN NO VÁLIDA");
        }

    }

    public void mundialXPilotos(){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Carrera> carreras = Carrera.getCarrerasListDto();
        List<Piloto> pilotosOrdenados = getDataPilotos();


        System.out.println("SELECCIONE EL NÚMERO DEL PILOTO:");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        if (opcion < 1 || opcion > pilotosOrdenados.size()) {
            System.out.println("OPCIÓN NO VÁLIDA");
            return;
        }

        Piloto pilotoSeleccionado = pilotosOrdenados.get(opcion - 1);
        String nombrePiloto = pilotoSeleccionado.getNombreApellido();

        List<Carrera> carrerasPiloto = carreras.stream() //crea un stream secuencia de datos que permite procesarlos
                .filter(c -> c.getPiloto().equalsIgnoreCase(nombrePiloto)) //filtrra los objetos de carrera que sean igual al nombre del piloto seleccionado
                .collect(Collectors.toList());//recolecta la inf filtrada y la convierte en lista

        if (carrerasPiloto.isEmpty()) {
            System.out.println("\nEl piloto " + nombrePiloto.toUpperCase() + " no corrió en la temporada 2024.");
            return;
        }

        int totalPuntos = 0;

        System.out.println("\nINFORMACIÓN DEL MUNDIAL - PILOTO: " + nombrePiloto.toUpperCase());
        System.out.println("+-----------------------------------------------+-----------+--------+-------------+------------+");
        System.out.println("| CARRERA                                       | POSICIÓN  | PUNTOS | SPRINT POS. | SPRINT PTS |");
        System.out.println("+-----------------------------------------------+-----------+--------+-------------+------------+");

        for (Carrera carrera : carrerasPiloto) {
            String circuito = carrera.getNombreCarrera();
            int posFinal = carrera.getPosicionFinal();
            int puntos = carrera.getPuntos();
            int posSprint = carrera.getPosicionFinalSp();
            int puntosSprint = carrera.getPuntosSp();

            totalPuntos += puntos + puntosSprint;

            String posSprintStr = (posSprint > 0) ? String.valueOf(posSprint) : "-";
            String puntosSprintStr = (puntosSprint > 0) ? String.valueOf(puntosSprint) : "-";

            System.out.printf("| %-45s | %-9d | %-6d | %-11s | %-10s |%n",
                    circuito, posFinal, puntos, posSprintStr, puntosSprintStr);
        }

        System.out.println("+-----------------------------------------------+-----------+--------+-------------+------------+");
        System.out.println("TOTAL PUNTOS: " + totalPuntos);

    }

    public void compararPilotos (){
        ExcelDataImporter.loadDatasFromeExcel();
        List<Carrera> carreras = Carrera.getCarrerasListDto();
        List<Piloto> pilotos = getDataPilotos();

        System.out.println("SELECCIONE EL PILOTO-1:");
        int opcion1 = scanner.nextInt();
        scanner.nextLine();
        System.out.println("SELECCIONE EL PILOTO-2:");
        int opcion2 = scanner.nextInt();
        scanner.nextLine();

        if (opcion1 < 1 || opcion1 > pilotos.size() || opcion2 < 1 || opcion2 > pilotos.size()) {
            System.out.println("HAY UNA OPCION NO VÁLIDA");
            return;
        }

        Piloto piloto1 = pilotos.get(opcion1 - 1);
        Piloto piloto2 = pilotos.get(opcion2 - 1);

        String nombre1 = piloto1.getNombreApellido();
        String nombre2 = piloto2.getNombreApellido();

        List<Carrera> carrerasP1 = carreras.stream()
                .filter(c -> c.getPiloto().equalsIgnoreCase(nombre1))
                .collect(Collectors.toList());

        List<Carrera> carrerasP2 = carreras.stream()
                .filter(c -> c.getPiloto().equalsIgnoreCase(nombre2))
                .collect(Collectors.toList());

        if (carrerasP1.isEmpty() || carrerasP2.isEmpty()) {
            if (carrerasP1.isEmpty() && carrerasP2.isEmpty()) {
                System.out.println("\nNINGUNO DE LOS DOS PILOTOS PARTICIPÓ EN LA TEMPORADA 2024. NO SE PUEDE HACER COMPARACIÓN.");
            } else if (carrerasP1.isEmpty()) {
                System.out.println("\n" + nombre1 + " NO PARTICIPÓ EN LA TEMPORADA 2024. NO SE PUEDE HACER COMPARACIÓN.");
            } else {
                System.out.println("\n" + nombre2 + " NO PARTICIPÓ EN LA TEMPORADA 2024. NO SE PUEDE HACER COMPARACIÓN.");
            }
            return;
        }

        int totalPuntos1 = 0;
        int totalPuntos2 = 0;
        System.out.println("+-----------------------------------------------+-----------------------------------+-----------------------------------+--------------------------------------------------+");
        System.out.printf("| %-45s | %-33s | %-33s | %-48s |%n", "CARRERA", nombre1.toUpperCase(), nombre2.toUpperCase(), "COMPARACIÓN");
        System.out.printf("| %-45s | %-9s | %-9s | %-9s | %-9s | %-9s | %-9s | %-48s |\n",
                "", "POS.CAR", "POS.SPR", "PUNTOS", "POS.CAR", "POS.SPR", "PUNTOS", "                                        ");
        System.out.println("+-----------------------------------------------+-----------+-----------+-----------+-----------+-----------+-----------+--------------------------------------------------+");

        for (Carrera c1 : carrerasP1) {
            String nombreCarrera = c1.getNombreCarrera();
            int posFinal1 = c1.getPosicionFinal();
            int posSprint1 = c1.getPosicionFinalSp();
            int puntosCarrera1 = c1.getPuntos() + c1.getPuntosSp();
            totalPuntos1 += puntosCarrera1;

            // Buscar si el piloto 2 participó en la misma carrera
            Optional<Carrera> c2Opt = carrerasP2.stream()
                    .filter(c -> c.getNombreCarrera().equalsIgnoreCase(nombreCarrera))
                    .findFirst();

            String posFinal2 = "-", posSprint2 = "-";
            int puntosCarrera2 = 0;
            String comparacion = "";

            if (c2Opt.isPresent()) {
                Carrera c2 = c2Opt.get();
                int posFinal2Int = c2.getPosicionFinal();
                int posSprint2Int = c2.getPosicionFinalSp();
                puntosCarrera2 = c2.getPuntos() + c2.getPuntosSp();
                totalPuntos2 += puntosCarrera2;

                posFinal2 = String.valueOf(posFinal2Int);
                posSprint2 = posSprint2Int > 0 ? String.valueOf(posSprint2Int) : "-";

                int diferenciaPuntos = puntosCarrera1 - puntosCarrera2;
                if (diferenciaPuntos > 0) {
                    comparacion = "Fue mejor " + nombre1 + " por " + diferenciaPuntos + " pts";
                } else if (diferenciaPuntos < 0) {
                    comparacion = "Fue mejor " + nombre2 + " por " + Math.abs(diferenciaPuntos) + " pts";
                } else {
                    comparacion = "Empate en puntos";
                }

            } else {
                comparacion = "Solo participó " + nombre1;
            }

            System.out.printf(
                    "|%-46s | %-9d | %-9s | %-9d | %-9s | %-9s | %-9d | %-48s |%n",
                    nombreCarrera,
                    posFinal1,
                    (posSprint1 > 0 ? posSprint1 : "-"),
                    puntosCarrera1,
                    posFinal2,
                    posSprint2,
                    puntosCarrera2,
                    comparacion
            );
        }
        System.out.println("+-----------------------------------------------+-----------+-----------+-----------+-----------+-----------+-----------+--------------------------------------------------+");

        String resumenFinal;
        int diferenciaTotal = totalPuntos1 - totalPuntos2;
        if (diferenciaTotal > 0) {
            resumenFinal = "TOTAL: " + nombre1 + " fue mejor por " + diferenciaTotal + " puntos";
        } else if (diferenciaTotal < 0) {
            resumenFinal = "TOTAL: " + nombre2 + " fue mejor por " + Math.abs(diferenciaTotal) + " puntos";
        } else {
            resumenFinal = "TOTAL: Ambos pilotos empataron en puntos";
        }

        System.out.printf("| %-45s | %-9s | %-9s | %-9d | %-9s | %-9s | %-9d | %-48s |%n",
                "TOTAL PUNTOS", "", "", totalPuntos1, "", "", totalPuntos2, resumenFinal);
        System.out.println("+-----------------------------------------------+-----------+-----------+-----------+-----------+-----------+-----------+--------------------------------------------------+");

        System.out.println();
        System.out.println(nombre1+ " obtuvo "+ totalPuntos1 + " puntos dejandolo en la temporada 2024 con la posicion Final "+ piloto1.getRanking2024());
        System.out.println(nombre2+" obtuvo "+ totalPuntos2 + " puntos dejandolo en la temporada 2024 con la posicion Final "+ piloto2.getRanking2024());
    }


}