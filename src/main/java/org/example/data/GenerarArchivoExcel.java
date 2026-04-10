package org.example.data;

import java.nio.file.Path;

/**
 * Ejecutar una vez desde el IDE o con Maven para crear {@code src/main/resources/dataFormulaUno.xlsx}.
 * <pre>
 *   mvn -q compile exec:java -Dexec.mainClass=org.example.data.GenerarArchivoExcel
 * </pre>
 */
public final class GenerarArchivoExcel {

    private GenerarArchivoExcel() {
    }

    public static void main(String[] args) throws Exception {
        Path out = args.length > 0
                ? Path.of(args[0])
                : Path.of("src/main/resources/dataFormulaUno.xlsx");
        Path abs = out.toAbsolutePath().normalize();
        ExcelDataImporter.exportMinimalTemplate(abs);
        System.out.println("Archivo Excel creado: " + abs);
    }
}
