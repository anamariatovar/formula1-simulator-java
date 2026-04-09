package org.example;

import com.poiji.annotation.ExcelCellName;
import org.example.data.ExcelDataImporter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Circuito {
    Scanner scanner = new Scanner(System.in);
    @ExcelCellName("ORDENCIRCUITO")
    private int ordenCircuito;
    @ExcelCellName("CIRCUITO")
    private String nombreCircuito;
    @ExcelCellName("PAIS")
    private String paisCircuito;
    @ExcelCellName("VUELTAS")
    private int numeroVueltas;
    @ExcelCellName("LONGITUD")
    private double longitudCircuito;
    @ExcelCellName("FECHACARRERA")
    private String fechaCarrera;
    @ExcelCellName("FECHASPRINT")
    private String fechaSp;

    private static List<Circuito> dataListCircuito = new ArrayList<>();

    public int getOrdenCircuito() {
        return ordenCircuito;
    }
    public String getNombreCircuito() {
        return nombreCircuito;
    }
    public String getPaisCircuito() {
        return paisCircuito;
    }
    public int getNumeroVueltas() {
        return numeroVueltas;
    }
    public String getFechaCarrera() {
        return fechaCarrera;
    }
    public String getFechaSp() {
        return fechaSp;
    }
    public double getLongitudCircuito() {
        return longitudCircuito;
    }
    public static List<Circuito> getDataListCircuito() {
        return dataListCircuito;
    }
    public static void setDataListCircuito(List<Circuito> dataListCircuito) {
        Circuito.dataListCircuito.addAll(dataListCircuito);
    }


    public void listarCarreras() {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Circuito> Circuitos = new ArrayList<>(getDataListCircuito());

        int total = Circuitos.size();
        int mitad = (int) Math.ceil(total / 2.0);
        System.out.println("\nLISTA CIRCUITOS\n");
        for (int i = 0; i < mitad; i++) {

            Circuito circuitoA = Circuitos.get(i);
            String columna1 = String.format("%-60s", circuitoA.getOrdenCircuito() + ". " + circuitoA.getNombreCircuito());

            String columna2 = "";
            if (i + mitad < total) {
                Circuito circuitoB = Circuitos.get(i + mitad);
                columna2  = (circuitoB.getOrdenCircuito() + ". " + circuitoB.getNombreCircuito());
            }
            System.out.println(columna1 + columna2);
        }
    }

    public void informacionCircuito() {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Circuito> infoCircuito = new ArrayList<>(getDataListCircuito());
        System.out.println("SELECCIONE EL NUMERO DE LA CARRERA");
        int opcion = scanner.nextInt();

        for (Circuito circuito : infoCircuito) {
            if (opcion == circuito.getOrdenCircuito()) {
                Carrera carrera = new Carrera();
                List<Carrera> listaCarreras = Carrera.getCarrerasListDto();
                for (Carrera car : listaCarreras) {
                    if (car.getNombreCarrera().equals(circuito.getNombreCircuito())) {
                        carrera = car;
                        break;
                    }
                }

                System.out.println("\n+-----+----------------------------------------------+------------+----------+----------------+--------------------+--------------------+");
                System.out.println("| No. | NOMBRE CIRCUITO                              | PAIS       | VUELTAS  | LONGITUD (KM)  | FECHA CARRERA     | FECHA SPRINT        |");
                System.out.println("+-----+----------------------------------------------+------------+----------+----------------+--------------------+--------------------+");

                String fechaCarreraStr = (carrera.getFechaCarrera() != null && !carrera.getFechaCarrera().isEmpty())
                        ? carrera.getFechaCarrera() : "No programada";
                String fechaSprintStr = (carrera.getFechasprint() != null && !carrera.getFechasprint().isEmpty())
                        ? carrera.getFechasprint() : "No aplica";

                System.out.printf(
                        "| %-3d | %-44s | %-10s | %-8d | %-14.3f | %-18s | %-18s |%n",
                        circuito.getOrdenCircuito(),
                        circuito.getNombreCircuito(),
                        circuito.getPaisCircuito(),
                        circuito.getNumeroVueltas(),
                        circuito.getLongitudCircuito(),
                        fechaCarreraStr,
                        fechaSprintStr
                );

                System.out.println("+-----+----------------------------------------------+------------+----------+----------------+--------------------+--------------------+");
                break;
            }
        }

    }
    public void cronogramaCarreras() {
        ExcelDataImporter.loadDatasFromeExcel();
        List<Circuito> infoCircuito = new ArrayList<>(getDataListCircuito());

        //Formato de fecha con puntos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        //ordena infocircuito por
        infoCircuito.sort(Comparator.comparing(circuito -> {
            try {
                String fecha = circuito.getFechaCarrera();
                // si la fecha no esta vacia lo convierte a un objeto de fecha
                if (fecha != null && !fecha.isEmpty()) {
                    return LocalDate.parse(fecha, formatter);
                }
            } catch (Exception e) {
                // Ignorar errores de conversión y retorna valor maximo y lo pone de ultimas
            }
            return LocalDate.MAX;
        }));

        System.out.println("+-----------------------------------------------+----------------------+-----------------+-----------------+");
        System.out.printf("| %-45s | %-20s | %-15s | %-15s |%n", "NOMBRE CIRCUITO", "PAIS", "FECHA CARRERA", "FECHA SPRINT");
        System.out.println("+-----------------------------------------------+----------------------+-----------------+-----------------+");

        for (Circuito circuito : infoCircuito) {
            String fechaCarreraStr = (circuito.getFechaCarrera() != null && !circuito.getFechaCarrera().isEmpty())
                    ? circuito.getFechaCarrera() : "No programada";
            String fechaSprintStr = (circuito.getFechaSp() != null && !circuito.getFechaSp().isEmpty())
                    ? circuito.getFechaSp() : "No aplica";

            System.out.printf(
                    "| %-45s | %-20s | %-15s | %-15s |%n",
                    circuito.getNombreCircuito(),
                    circuito.getPaisCircuito(),
                    fechaCarreraStr,
                    fechaSprintStr

            );
        }

        System.out.println("+-----------------------------------------------+----------------------+-----------------+-----------------+");
    }
}