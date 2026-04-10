package org.example;
import java.util.Scanner;
import static org.example.data.Banner.*;


public class Menu {

    Circuito infCircuitos = new Circuito();
    Piloto infPilotos = new Piloto();
    Escuderia infEscuderia = new Escuderia();
    Carrera infCarrera = new Carrera();

    private int opcion = 0;
    Scanner scanner = new Scanner(System.in);

    public void menuPrincipal() {
        do {
            limpiarPantalla();
            System.out.println(bannerFormula1);
            System.out.println(bannerMenuPrincipal);
            System.out.println("MENU PRINCIPAL");
            System.out.println("1. CONSULTAR POR CARRERAS");
            System.out.println("2. CONSULTAR POR PILOTOS");
            System.out.println("3. CONSULTAR POR ESCUDERIAS");
            System.out.println("4. CONSULTAR CRONOGRAMA");
            System.out.println("5. COMPARACION");
            System.out.println("6. SALIR DEL SISTEMA");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    menuCarreras();
                    break;
                case 2:
                    menuPilotos();
                    break;
                case 3:
                    menuEscuderias();
                    break;
                case 4:
                    meuCronograma();
                    break;
                case 5:
                    menuComparacion();
                    break;
                case 6:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }

        } while (opcion != 6);
    }

    public void menuCarreras() {
        do {
            limpiarPantalla();
            System.out.println(bannerMenuCarreras);
            System.out.println("MENU CARRERAS");
            System.out.println("11. LISTAR CARRERAS");
            System.out.println("12. SELECCIONAR CARRERA");
            System.out.println("13. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 11:
                    infCircuitos.listarCarreras();
                    break;
                case 12:
                    menuSeleccionarCarrera();
                    break;
                case 13:
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 13);
    }

    public void menuSeleccionarCarrera() {
        do {
            limpiarPantalla();
            System.out.println(bannerSeleccionCarrera);

            System.out.println("121. IMPRIMIR INFORMACION DEL CIRCUITO");
            System.out.println("122. IMPRIMIR INFORMACION DEL MUNDIAL DE CONSTRUCTORES EN ESTA CARRERA");
            System.out.println("123. IMPRIMIR INFORMACION DEL MUNDIAL DE PILOTOS EN ESTA CARRERA");
            System.out.println("124. VOLVER MENU DE CARRERAS");
            System.out.println("125. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 121:
                    infCircuitos.listarCarreras();
                    System.out.println("\nINFORMACION DEL CIRCUITO");
                    System.out.println("SELECCIONE EL NUMERO DE LA CARRERA");
                    infCircuitos.informacionCircuito(scanner.nextInt());
                    break;
                case 122:
                    infCircuitos.listarCarreras();
                    System.out.println("\nINFORMACION DEL MUNDIAL DE CONSTRUCTORES");
                    System.out.println("SELECCIONE EL NUMERO DE CARRERA");
                    infCarrera.infoContructoresXCarrera(scanner.nextInt());
                    break;
                case 123:
                    infCircuitos.listarCarreras();
                    System.out.println("\nINFORMACION DEL MUNDIAL DE PILOTOS");
                    System.out.println("SELECCIONE EL NÚMERO DE CARRERA");
                    infCarrera.infoPilotosXCarrera(scanner.nextInt());
                    break;
                case 124:
                    return;
                case 125:
                    this.menuPrincipal();
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 125);
    }

    public void menuPilotos() {
        do {
            limpiarPantalla();
            System.out.println(bannerMenuPilotos);
            System.out.println("MENU PILOTOS");
            System.out.println("21. LISTAR PILOTOS");
            System.out.println("22. SELECCIONAR PILOTO");
            System.out.println("23. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 21:
                    infPilotos.listarPilotos();
                    break;
                case 22:
                    menuSeleccionarPiloto();
                    break;
                case 23:
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 23);
    }

    public void menuSeleccionarPiloto() {
        do {
            limpiarPantalla();
            System.out.println(bannerSeleccionPiloto);
            System.out.println("221. IMPRIMIR INFORMACION DEL PILOTO");
            System.out.println("222. IMPRIMIR INFORMACION DEL MUNDIAL DE PILOTOS");
            System.out.println("223. VOLVER MENU DE PILOTOS");
            System.out.println("224. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 221:
                    infPilotos.listarPilotos();
                    System.out.println("\nINFORMACION DEL PILOTO");
                    System.out.println("SELECCIONE EL NÚMERO DEL PILOTO");
                    infPilotos.informacionPiloto(scanner.nextInt());
                    break;
                case 222:
                    infPilotos.listarPilotos();
                    System.out.println("\nINFORMACION DEL MUNDIAL DE PILOTOS");
                    System.out.println("SELECCIONE EL NÚMERO DEL PILOTO:");
                    infPilotos.mundialXPilotos(scanner.nextInt());
                    break;
                case 223:
                    return;
                case 224:
                    this.menuPrincipal();
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 224);
    }

    public void menuEscuderias() {
        do {
            limpiarPantalla();
            System.out.println(bannerMenuEscuderias);
            System.out.println("MENU ESCUDERIAS");
            System.out.println("31. LISTAR ESCUDERIAS");
            System.out.println("32. SELECCIONAR ESCUDERIA");
            System.out.println("33. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 31:
                    infEscuderia.listarEscuderias();
                    break;
                case 32:
                    menuSeleccionarEscuderia();
                    break;
                case 33:
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 33);
    }

    public void menuSeleccionarEscuderia() {
        do {
            limpiarPantalla();
            System.out.println(bannerSeleccionarEscuderia);
            System.out.println("321. IMPRIMIR INFORMACION DE LA ESCUDERIA");
            System.out.println("322. IMPRIMIR INFORMACION DEL MUNDIAL DE CONSTRUCTORES");
            System.out.println("323. IMPRIMIR INFORMACION DEL MUNDIAL DE PILOTOS PARA PILOTOS DE ESTA ESCUDERIA");
            System.out.println("324. VOLVER MENU DE ESCUDERIAS");
            System.out.println("325. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 321:
                    infEscuderia.listarEscuderias();
                    System.out.println("\nINFORMACION DE LA ESCUDERIA");
                    System.out.println("SELECCIONE EL NUMERO DE LA ESCUDERIA");
                    infEscuderia.informacionEscuderia(scanner.nextInt());
                    break;
                case 322:
                    infEscuderia.listarEscuderias();
                    System.out.println("\nINFORMACION DEL MUNDIAL DE CONSTRUCTORES");
                    System.out.println("SELECCIONE EL NÚMERO DE LA ESCUDERÍA");
                    infEscuderia.contructoresXEscuderia(scanner.nextInt());
                    break;
                case 323:
                    infEscuderia.listarEscuderias();
                    System.out.println("\nINFORMACION DEL MUNDIAL DE PILOTOS");
                    System.out.println("SELECCIONE EL NÚMERO DE LA ESCUDERÍA:");
                    infEscuderia.pilotosXEscuderia(scanner.nextInt());
                    break;
                case 324:
                    return;
                case 325:
                    this.menuPrincipal();
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 325);
    }
    public void meuCronograma(){
        do {
            limpiarPantalla();
            System.out.println(bannerCronograma);
            System.out.println("\nMENU CRONOGRAMA");
            System.out.println("41. VISUALIZAR CRONOGRAMA");
            System.out.println("42. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  ");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 41:
                    System.out.println(bannerCronograma);
                    infCircuitos.cronogramaCarreras();
                    break;
                case 42:
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 42);

    }
    public void menuComparacion() {
        do {
            limpiarPantalla();
            System.out.println(bannerCompararEntre);
            System.out.println("MENU COMPARACION");
            System.out.println("51. ENTRE PILOTOS");
            System.out.println("52. ENTRE ESCUDERIAS");
            System.out.println("53. VOLVER MENU PRINCIPAL");
            System.out.print("Ingrese su opción:  \n");
            this.opcion = scanner.nextInt();

            switch (opcion) {
                case 51:
                    System.out.println(bannerCompararPilotos);
                    infPilotos.listarPilotos();
                    System.out.println("SELECCIONE EL PILOTO-1:");
                    int p1 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("SELECCIONE EL PILOTO-2:");
                    int p2 = scanner.nextInt();
                    scanner.nextLine();
                    infPilotos.compararPilotos(p1, p2);

                    break;
                case 52:
                    System.out.println(bannerCompararEscuderias);
                    infEscuderia.listarEscuderias();
                    System.out.println("SELECCIONE LA ESCUDERIA-1:");
                    int e1 = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("SELECCIONE LA ESCUDERIA-2:");
                    int e2 = scanner.nextInt();
                    scanner.nextLine();
                    infEscuderia.compararEscuderia(e1, e2);
                    break;
                case 53:
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        } while (this.opcion != 53);

    }

    public static void limpiarPantalla() {
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }
}