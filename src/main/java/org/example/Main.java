package org.example;

import org.example.ui.SimuladorVentana;

/**
 * Arranque por defecto: interfaz gráfica (Swing).
 * <p>
 * Modo consola: pasar el argumento {@code consola}, o ejecutar {@link MainConsola} desde el IDE.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "consola".equalsIgnoreCase(args[0])) {
            new Menu().menuPrincipal();
        } else {
            SimuladorVentana.iniciar();
        }
    }
}























