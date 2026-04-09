package org.example.data;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import org.example.*;

import java.io.File;
import java.util.List;

import static org.example.Carrera.*;
import static org.example.Circuito.*;
import static org.example.Escuderia.*;
import static org.example.Piloto.*;


public class ExcelDataImporter {
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
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .sheetName(hoja) // Aquí se especifica el nombre de la hoja
                .build();
        return options;
    }

    private static File getFileExcel() {
        try {
            return new File(ClassLoader.getSystemResource("dataFormulaUno.xlsx").toURI());
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar el archivo Excel desde resources.", e);
        }
    }



}
