package org.example.ui;

import org.example.Carrera;
import org.example.Circuito;
import org.example.Escuderia;
import org.example.Piloto;
import org.example.data.ExcelDataImporter;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimuladorVentana extends JFrame {

    private List<Piloto> pilotosLista = Collections.emptyList();
    private List<Piloto> pilotosOrdenFicha = Collections.emptyList();
    private List<Escuderia> escuderiasLista = Collections.emptyList();
    private List<Escuderia> escuderiasOrdenFicha = Collections.emptyList();

    private final Circuito circuito = new Circuito();
    private final Piloto piloto = new Piloto();
    private final Escuderia escuderia = new Escuderia();
    private final Carrera carrera = new Carrera();

    private final JTextArea salida = new JTextArea(24, 90);
    private final JComboBox<Integer> comboOrdenCarrera = new JComboBox<>();
    private final JComboBox<String> comboPilotoOrden = new JComboBox<>();
    private final JComboBox<String> comboPilotoIndice = new JComboBox<>();
    private final JComboBox<String> comboEscuderiaOrden = new JComboBox<>();
    private final JComboBox<String> comboPilotoCmp1 = new JComboBox<>();
    private final JComboBox<String> comboPilotoCmp2 = new JComboBox<>();
    private final JComboBox<String> comboEscCmp1 = new JComboBox<>();
    private final JComboBox<String> comboEscCmp2 = new JComboBox<>();

    public SimuladorVentana() {
        super("Simulador F1 — Temporada 2024");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 620));

        salida.setEditable(false);
        salida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollSalida = new JScrollPane(salida);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Carreras / circuitos", panelCarreras());
        pestañas.addTab("Pilotos", panelPilotos());
        pestañas.addTab("Escuderías", panelEscuderias());
        pestañas.addTab("Cronograma", panelCronograma());
        pestañas.addTab("Comparación", panelComparacion());

        JPanel inferior = new JPanel(new BorderLayout(8, 8));
        inferior.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        inferior.add(pestañas, BorderLayout.NORTH);
        inferior.add(scrollSalida, BorderLayout.CENTER);

        add(inferior, BorderLayout.CENTER);

        JPanel cabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cabecera.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 12));
        JLabel titulo = new JLabel("Consultas sobre datos cargados desde Excel (resources).");
        cabecera.add(titulo);
        JButton recargar = new JButton("Recargar datos");
        recargar.addActionListener(e -> recargarCombos());
        cabecera.add(recargar);
        add(cabecera, BorderLayout.NORTH);

        recargarCombos();
        appendSalida("Bienvenido. Use las pestañas y botones; el resultado aparece aquí.\n\n");
    }

    private JPanel panelCarreras() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("N.º carrera / circuito:"));
        p.add(comboOrdenCarrera);

        p.add(boton("Listar carreras", () -> appendCaptura(() -> circuito.listarCarreras())));
        p.add(boton("Info del circuito", () -> {
            Integer o = (Integer) comboOrdenCarrera.getSelectedItem();
            if (o == null) {
                appendSalida("No hay carreras cargadas.\n");
                return;
            }
            appendCaptura(() -> circuito.informacionCircuito(o));
        }));
        p.add(boton("Mundial constructores (esta carrera)", () -> {
            Integer o = (Integer) comboOrdenCarrera.getSelectedItem();
            if (o == null) {
                appendSalida("No hay carreras cargadas.\n");
                return;
            }
            appendCaptura(() -> carrera.infoContructoresXCarrera(o));
        }));
        p.add(boton("Mundial pilotos (esta carrera)", () -> {
            Integer o = (Integer) comboOrdenCarrera.getSelectedItem();
            if (o == null) {
                appendSalida("No hay carreras cargadas.\n");
                return;
            }
            appendCaptura(() -> carrera.infoPilotosXCarrera(o));
        }));

        return p;
    }

    private JPanel panelPilotos() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(boton("Listar pilotos", () -> appendCaptura(() -> piloto.listarPilotos())));

        p.add(new JLabel("Por n.º en ficha (orden piloto):"));
        p.add(comboPilotoOrden);
        p.add(boton("Ficha del piloto", () -> {
            int i = comboPilotoOrden.getSelectedIndex();
            if (i < 0 || i >= pilotosOrdenFicha.size()) {
                appendSalida("Seleccione un piloto.\n");
                return;
            }
            int ord = pilotosOrdenFicha.get(i).getOrdenPiloto();
            appendCaptura(() -> piloto.informacionPiloto(ord));
        }));

        p.add(new JLabel("Por posición en lista (mundial):"));
        p.add(comboPilotoIndice);
        p.add(boton("Resultados del piloto en 2024", () -> {
            Integer idx = indiceLista1Based(comboPilotoIndice, pilotosLista.size());
            if (idx == null) {
                return;
            }
            appendCaptura(() -> piloto.mundialXPilotos(idx));
        }));

        return p;
    }

    private JPanel panelEscuderias() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(boton("Listar escuderías", () -> appendCaptura(() -> escuderia.listarEscuderias())));
        p.add(new JLabel("Escudería:"));
        p.add(comboEscuderiaOrden);
        p.add(boton("Datos de la escudería", () -> {
            int i = comboEscuderiaOrden.getSelectedIndex();
            if (i < 0 || i >= escuderiasOrdenFicha.size()) {
                appendSalida("Seleccione una escudería.\n");
                return;
            }
            int o = escuderiasOrdenFicha.get(i).getOrdenEscuderia();
            appendCaptura(() -> escuderia.informacionEscuderia(o));
        }));
        p.add(boton("Mundial constructores (escudería)", () -> {
            int i = comboEscuderiaOrden.getSelectedIndex();
            if (i < 0 || i >= escuderiasOrdenFicha.size()) {
                appendSalida("Seleccione una escudería.\n");
                return;
            }
            int o = escuderiasOrdenFicha.get(i).getOrdenEscuderia();
            appendCaptura(() -> escuderia.contructoresXEscuderia(o));
        }));
        p.add(boton("Pilotos de la escudería (mundial)", () -> {
            int i = comboEscuderiaOrden.getSelectedIndex();
            if (i < 0 || i >= escuderiasOrdenFicha.size()) {
                appendSalida("Seleccione una escudería.\n");
                return;
            }
            int o = escuderiasOrdenFicha.get(i).getOrdenEscuderia();
            appendCaptura(() -> escuderia.pilotosXEscuderia(o));
        }));
        return p;
    }

    private JPanel panelCronograma() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(boton("Mostrar cronograma", () -> appendCaptura(() -> circuito.cronogramaCarreras())));
        return p;
    }

    private JPanel panelComparacion() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        p.add(new JLabel("Piloto 1 (lista):"));
        p.add(comboPilotoCmp1);
        p.add(new JLabel("Piloto 2:"));
        p.add(comboPilotoCmp2);
        p.add(boton("Comparar pilotos", () -> {
            Integer a = indiceLista1Based(comboPilotoCmp1, pilotosLista.size());
            Integer b = indiceLista1Based(comboPilotoCmp2, pilotosLista.size());
            if (a == null || b == null) {
                appendSalida("Seleccione ambos pilotos.\n");
                return;
            }
            appendCaptura(() -> piloto.compararPilotos(a, b));
        }));

        p.add(new JLabel("   |   "));

        p.add(new JLabel("Escudería 1:"));
        p.add(comboEscCmp1);
        p.add(new JLabel("Escudería 2:"));
        p.add(comboEscCmp2);
        p.add(boton("Comparar escuderías", () -> {
            Integer a = indiceLista1Based(comboEscCmp1, escuderiasLista.size());
            Integer b = indiceLista1Based(comboEscCmp2, escuderiasLista.size());
            if (a == null || b == null) {
                appendSalida("Seleccione ambas escuderías.\n");
                return;
            }
            appendCaptura(() -> escuderia.compararEscuderia(a, b));
        }));

        return p;
    }

    private static JButton boton(String texto, Runnable accion) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> accion.run());
        return b;
    }

    private void appendSalida(String texto) {
        salida.append(texto);
        salida.setCaretPosition(salida.getDocument().getLength());
    }

    private void appendCaptura(Runnable accion) {
        try {
            String bloque = OutputCapture.capture(accion);
            appendSalida(bloque);
            if (!bloque.endsWith("\n")) {
                appendSalida("\n");
            }
            appendSalida("\n");
        } catch (Exception ex) {
            appendSalida("Error: " + ex.getMessage() + "\n\n");
        }
    }

    private void recargarCombos() {
        try {
            ExcelDataImporter.loadDatasFromeExcel();
        } catch (Exception ex) {
            appendSalida("No se pudo cargar Excel: " + ex.getMessage() + "\n");
            Throwable c = ex.getCause();
            if (c != null) {
                appendSalida("Causa: " + c.getMessage() + "\n");
            }
            appendSalida("Recompila el proyecto (Build → Rebuild) y comprueba que exista src/main/resources/dataFormulaUno.xlsx\n\n");
            return;
        }

        List<Circuito> circuitos = new ArrayList<>(Circuito.getDataListCircuito());
        circuitos.sort(Comparator.comparingInt(Circuito::getOrdenCircuito));
        comboOrdenCarrera.setModel(new DefaultComboBoxModel<>(circuitos.stream()
                .map(Circuito::getOrdenCircuito)
                .toArray(Integer[]::new)));

        pilotosLista = new ArrayList<>(Piloto.getDataPilotos());
        pilotosOrdenFicha = pilotosLista.stream()
                .sorted(Comparator.comparingInt(Piloto::getOrdenPiloto))
                .collect(Collectors.toList());
        List<String> etiquetasOrden = pilotosOrdenFicha.stream()
                .map(pl -> pl.getOrdenPiloto() + ". " + pl.getNombreApellido())
                .collect(Collectors.toList());
        comboPilotoOrden.setModel(new DefaultComboBoxModel<>(etiquetasOrden.toArray(new String[0])));

        List<String> etiquetasIndice = new ArrayList<>();
        for (int i = 0; i < pilotosLista.size(); i++) {
            Piloto pl = pilotosLista.get(i);
            etiquetasIndice.add((i + 1) + ". " + pl.getNombreApellido());
        }
        comboPilotoIndice.setModel(new DefaultComboBoxModel<>(etiquetasIndice.toArray(new String[0])));
        comboPilotoCmp1.setModel(new DefaultComboBoxModel<>(etiquetasIndice.toArray(new String[0])));
        comboPilotoCmp2.setModel(new DefaultComboBoxModel<>(etiquetasIndice.toArray(new String[0])));

        escuderiasLista = new ArrayList<>(Escuderia.getDataListEscuderia());
        escuderiasOrdenFicha = escuderiasLista.stream()
                .sorted(Comparator.comparingInt(Escuderia::getOrdenEscuderia))
                .collect(Collectors.toList());
        List<String> escEtiq = escuderiasOrdenFicha.stream()
                .map(es -> es.getOrdenEscuderia() + ". " + es.getNombreEquipo())
                .collect(Collectors.toList());
        comboEscuderiaOrden.setModel(new DefaultComboBoxModel<>(escEtiq.toArray(new String[0])));

        List<String> escIndice = new ArrayList<>();
        for (int i = 0; i < escuderiasLista.size(); i++) {
            Escuderia es = escuderiasLista.get(i);
            escIndice.add((i + 1) + ". " + es.getNombreEquipo());
        }
        comboEscCmp1.setModel(new DefaultComboBoxModel<>(escIndice.toArray(new String[0])));
        comboEscCmp2.setModel(new DefaultComboBoxModel<>(escIndice.toArray(new String[0])));

        appendSalida("Datos recargados correctamente.\n\n");
    }

    /**
     * Índice 1-based en la lista de Excel (igual que en consola para comparaciones y mundial por lista).
     */
    private Integer indiceLista1Based(JComboBox<String> combo, int tamLista) {
        int i = combo.getSelectedIndex();
        if (i < 0 || tamLista == 0) {
            appendSalida("Seleccione un elemento válido.\n");
            return null;
        }
        return i + 1;
    }

    public static void iniciar() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            SimuladorVentana v = new SimuladorVentana();
            v.setLocationRelativeTo(null);
            v.setVisible(true);
        });
    }
}
