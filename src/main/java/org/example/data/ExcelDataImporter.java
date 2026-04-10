package org.example.data;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.Carrera;
import org.example.Circuito;
import org.example.Escuderia;
import org.example.Piloto;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.example.Carrera.getCarrerasListDto;
import static org.example.Carrera.setCarrerasListDto;
import static org.example.Circuito.getDataListCircuito;
import static org.example.Circuito.setDataListCircuito;
import static org.example.Escuderia.getDataListEscuderia;
import static org.example.Escuderia.setDataListEscuderia;
import static org.example.Piloto.getDataPilotos;
import static org.example.Piloto.setDataPilotos;

public class ExcelDataImporter {

    private static final String EXCEL_NAME = "dataFormulaUno.xlsx";
    private static final String PROP_PATH = "formula1.excel.path";
    private static final String ENV_PATH = "FORMULA1_EXCEL_PATH";

    private static File classpathExtracted;

    public static void loadDatasFromeExcel() {

        getDataListEscuderia().clear();
        getDataListCircuito().clear();
        getDataPilotos().clear();
        getCarrerasListDto().clear();
        File archivoExcel = getFileExcel();

        PoijiOptions option = getHojaOptions("ESCUDERIA");
        List<Escuderia> escuderias = Poiji.fromExcel(archivoExcel, Escuderia.class, option);
        setDataListEscuderia(escuderias);

        option = getHojaOptions("CIRCUITO");
        List<Circuito> circuitos = Poiji.fromExcel(archivoExcel, Circuito.class, option);
        setDataListCircuito(circuitos);

        option = getHojaOptions("PILOTO");
        List<Piloto> pilotos = Poiji.fromExcel(archivoExcel, Piloto.class, option);
        setDataPilotos(pilotos);

        option = getHojaOptions("CARRERA");
        List<Carrera> carreras = Poiji.fromExcel(archivoExcel, Carrera.class, option);
        setCarrerasListDto(carreras);
    }

    private static PoijiOptions getHojaOptions(String hoja) {
        return PoijiOptions.PoijiOptionsBuilder.settings()
                .sheetName(hoja)
                .build();
    }

    private static File getFileExcel() {
        try {
            File explicit = resolveExplicitPath();
            if (explicit != null) {
                return explicit;
            }

            File inWorkingDir = Path.of(System.getProperty("user.dir", "."), EXCEL_NAME).toFile();
            if (inWorkingDir.isFile()) {
                return inWorkingDir.getAbsoluteFile();
            }

            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = ExcelDataImporter.class.getClassLoader();
            }

            URL url = cl != null ? cl.getResource(EXCEL_NAME) : null;
            if (url == null) {
                url = ClassLoader.getSystemResource(EXCEL_NAME);
            }

            if (url != null) {
                File fromUrl = tryFileFromUrl(url);
                if (fromUrl != null) {
                    return fromUrl;
                }
            }

            InputStream fromCl = cl != null ? cl.getResourceAsStream(EXCEL_NAME) : null;
            InputStream fromSys = fromCl != null ? null : ClassLoader.getSystemResourceAsStream(EXCEL_NAME);
            InputStream resourceStream = fromCl != null ? fromCl : fromSys;
            if (resourceStream != null) {
                try (InputStream in = resourceStream) {
                    return materializeClasspathExcel(in);
                }
            }

            Path fallbackDir = Path.of(System.getProperty("user.home"), ".formula1-simulator");
            Files.createDirectories(fallbackDir);
            Path fallbackFile = fallbackDir.resolve(EXCEL_NAME);
            if (!Files.isRegularFile(fallbackFile)) {
                writeMinimalWorkbook(fallbackFile);
            }
            return fallbackFile.toAbsolutePath().toFile();
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se pudo obtener el archivo Excel. Coloque " + EXCEL_NAME
                            + " en la carpeta del proyecto, en src/main/resources, o defina la propiedad del sistema "
                            + PROP_PATH + " (o la variable de entorno " + ENV_PATH + ") con la ruta completa al .xlsx.",
                    e);
        }
    }

    private static File resolveExplicitPath() {
        String prop = System.getProperty(PROP_PATH);
        if (prop != null && !prop.isBlank()) {
            File f = new File(prop.trim());
            if (f.isFile()) {
                return f.getAbsoluteFile();
            }
            throw new IllegalArgumentException("La ruta de " + PROP_PATH + " no existe o no es un archivo: " + f);
        }
        String env = System.getenv(ENV_PATH);
        if (env != null && !env.isBlank()) {
            File f = new File(env.trim());
            if (f.isFile()) {
                return f.getAbsoluteFile();
            }
            throw new IllegalArgumentException("La ruta de " + ENV_PATH + " no existe o no es un archivo: " + f);
        }
        return null;
    }

    private static File tryFileFromUrl(URL url) {
        try {
            if (!"file".equalsIgnoreCase(url.getProtocol())) {
                return null;
            }
            URI uri = url.toURI();
            File f = new File(uri);
            return f.isFile() ? f.getAbsoluteFile() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static synchronized File materializeClasspathExcel(InputStream stream) throws java.io.IOException {
        if (classpathExtracted != null && classpathExtracted.isFile()) {
            return classpathExtracted;
        }
        Path dir = Path.of(System.getProperty("user.home"), ".formula1-simulator");
        Files.createDirectories(dir);
        Path target = dir.resolve("cached-" + EXCEL_NAME);
        Files.copy(stream, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        classpathExtracted = target.toAbsolutePath().toFile();
        return classpathExtracted;
    }

    /**
     * Genera un {@code dataFormulaUno.xlsx} mínimo (hojas ESCUDERIA, CIRCUITO, PILOTO, CARRERA).
     * Útil para rellenar {@code src/main/resources} o para pruebas.
     */
    public static void exportMinimalTemplate(Path path) throws java.io.IOException {
        writeMinimalWorkbook(path);
    }

    /**
     * Libro mínimo con datos coherentes entre hojas (solo para poder arrancar sin tu Excel real).
     * Sustituye este archivo por tu dataFormulaUno.xlsx completo en resources o en la carpeta del proyecto.
     */
    private static void writeMinimalWorkbook(Path path) throws java.io.IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            sheetEscuderia(wb);
            sheetCircuito(wb);
            sheetPiloto(wb);
            sheetCarrera(wb);
            Files.createDirectories(path.getParent());
            try (java.io.OutputStream os = Files.newOutputStream(path)) {
                wb.write(os);
            }
        }
    }

    private static void sheetEscuderia(XSSFWorkbook wb) {
        Sheet sh = wb.createSheet("ESCUDERIA");
        Row h = sh.createRow(0);
        String[] cols = {
                "ORDENESCUDERIAS", "ESCUDERIA", "RANKINGCONTRUCTORES", "DIRECTOR", "PAIS",
                "CAMPEONATOSGANADOS", "PUNTOSACUMULADOS", "PILOTO1", "PILOTO2", "PILOTORESERVA"
        };
        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
        }
        Row r = sh.createRow(1);
        r.createCell(0).setCellValue(1);
        r.createCell(1).setCellValue("Aston Martin");
        r.createCell(2).setCellValue("5");
        r.createCell(3).setCellValue("Mike Krack");
        r.createCell(4).setCellValue("Reino Unido");
        r.createCell(5).setCellValue(0);
        r.createCell(6).setCellValue(120);
        r.createCell(7).setCellValue("Fernando Alonso");
        r.createCell(8).setCellValue("Lance Stroll");
        r.createCell(9).setCellValue("Felipe Drugovich");
    }

    private static void sheetCircuito(XSSFWorkbook wb) {
        Sheet sh = wb.createSheet("CIRCUITO");
        Row h = sh.createRow(0);
        String[] cols = {"ORDENCIRCUITO", "CIRCUITO", "PAIS", "VUELTAS", "LONGITUD", "FECHACARRERA", "FECHASPRINT"};
        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
        }
        Row r = sh.createRow(1);
        r.createCell(0).setCellValue(1);
        r.createCell(1).setCellValue("Monaco");
        r.createCell(2).setCellValue("Monaco");
        r.createCell(3).setCellValue(78);
        r.createCell(4).setCellValue(3.337);
        r.createCell(5).setCellValue("26.05.2024");
        r.createCell(6).setCellValue("");
    }

    private static void sheetPiloto(XSSFWorkbook wb) {
        Sheet sh = wb.createSheet("PILOTO");
        Row h = sh.createRow(0);
        String[] cols = {
                "ORDENPILOTO", "NOMBREPILOTO", "EQUIPO", "EDAD", "PAIS", "CAMPGANADOS",
                "CARRERASDISP", "PUNTOS2024", "RANKING2024", "PUESTO"
        };
        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
        }
        Row r1 = sh.createRow(1);
        r1.createCell(0).setCellValue(1);
        r1.createCell(1).setCellValue("Fernando Alonso");
        r1.createCell(2).setCellValue("Aston Martin");
        r1.createCell(3).setCellValue(42);
        r1.createCell(4).setCellValue("España");
        r1.createCell(5).setCellValue(2);
        r1.createCell(6).setCellValue(380);
        r1.createCell(7).setCellValue(80);
        r1.createCell(8).setCellValue(4);
        r1.createCell(9).setCellValue("Titular");

        Row r2 = sh.createRow(2);
        r2.createCell(0).setCellValue(2);
        r2.createCell(1).setCellValue("Lance Stroll");
        r2.createCell(2).setCellValue("Aston Martin");
        r2.createCell(3).setCellValue(25);
        r2.createCell(4).setCellValue("Canadá");
        r2.createCell(5).setCellValue(0);
        r2.createCell(6).setCellValue(150);
        r2.createCell(7).setCellValue(40);
        r2.createCell(8).setCellValue(12);
        r2.createCell(9).setCellValue("Titular");
    }

    private static void sheetCarrera(XSSFWorkbook wb) {
        Sheet sh = wb.createSheet("CARRERA");
        Row h = sh.createRow(0);
        String[] cols = {
                "ORDENCARRERA", "NOMBRECIRCUITO", "PILOTO", "ESCUDERIA", "FECHACARRERA",
                "POSICIONINICIAL", "POSICIONFINAL", "PUNTOS", "FECHASPRINT",
                "POSICIONINICIALSP", "POSICIONFINALSP", "PUNTOSSP"
        };
        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
        }
        Row r1 = sh.createRow(1);
        r1.createCell(0).setCellValue(1);
        r1.createCell(1).setCellValue("Monaco");
        r1.createCell(2).setCellValue("Fernando Alonso");
        r1.createCell(3).setCellValue("Aston Martin");
        r1.createCell(4).setCellValue("26.05.2024");
        r1.createCell(5).setCellValue(5);
        r1.createCell(6).setCellValue(8);
        r1.createCell(7).setCellValue(4);
        r1.createCell(8).setCellValue("");
        r1.createCell(9).setCellValue(0);
        r1.createCell(10).setCellValue(0);
        r1.createCell(11).setCellValue(0);

        Row r2 = sh.createRow(2);
        r2.createCell(0).setCellValue(1);
        r2.createCell(1).setCellValue("Monaco");
        r2.createCell(2).setCellValue("Lance Stroll");
        r2.createCell(3).setCellValue("Aston Martin");
        r2.createCell(4).setCellValue("26.05.2024");
        r2.createCell(5).setCellValue(12);
        r2.createCell(6).setCellValue(10);
        r2.createCell(7).setCellValue(1);
        r2.createCell(8).setCellValue("");
        r2.createCell(9).setCellValue(0);
        r2.createCell(10).setCellValue(0);
        r2.createCell(11).setCellValue(0);
    }
}
