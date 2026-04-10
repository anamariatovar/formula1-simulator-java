package org.example;

/**
 * Punto de entrada solo para el menú por consola (Scanner).
 * La ventana gráfica se lanza con {@link Main} sin argumentos.
 */
public final class MainConsola {

    private MainConsola() {
    }

    public static void main(String[] args) {
        new Menu().menuPrincipal();
    }
}
