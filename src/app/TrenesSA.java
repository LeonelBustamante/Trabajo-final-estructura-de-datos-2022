package app;

import java.io.IOException;

import tools.*;
import util.TecladoIn;

public class TrenesSA {

    // PARA CARGA AUTOMATICA SE DEBE CAMBIAR LA SIGUIENTE LINEA
    private static String path = "D:\\Documentos\\Leo\\Trabajo-final-estructura-de-datos-2022\\src\\util\\Archivos\\cargaDeDatos.txt";

    public static void main(String[] args) {
        Archivos.limpiarTXT();
        menuPrincipal();
    }

    private static void menuPrincipal() {
        Grafo mapa = new Grafo();
        DiccionarioAVL estaciones = new DiccionarioAVL();
        DiccionarioHash trenes = new DiccionarioHash(20);
        MapeoAMuchos lineas = new MapeoAMuchos(20);
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| MENU PRINCIPAL                                   |");
            System.out.println("----------------------------------------------------");
            System.out.println("| 1. Carga inicial de datos                        |");
            System.out.println("| 2. ABM de Trenes                                 |");
            System.out.println("| 3. ABM de Estaciones                             |");
            System.out.println("| 4. ABM de Lineas                                 |");
            System.out.println("| 5. ABM de Vias                                   |");
            System.out.println("| 6. Consultas de Trenes                           |");
            System.out.println("| 7. Consultas de Estaciones                       |");
            System.out.println("| 8. Consultas de Viajes                           |");
            System.out.println("| 9. Mostrar todos los datos                       |");
            System.out.println("| 10. Salir                                        |");
            System.out.println("----------------------------------------------------");
            System.out.print("Ingrese una opcion: ");
            opcion = TecladoIn.readLineInt();
            switch (opcion) {
                case 1 -> Archivos.leer(path, mapa, estaciones, trenes, lineas);
                case 2 -> abmTrenes(trenes, lineas);
                case 3 -> abmEstaciones(estaciones, mapa);
                case 4 -> abmLineas(lineas, estaciones);
                case 5 -> abmVias(mapa, estaciones);
                case 6 -> consultasTrenes(trenes, lineas);
                case 7 -> consultasEstaciones(estaciones);
                case 8 -> consultasViajes(mapa, estaciones);
                case 9 -> mostrarDatos(mapa, estaciones, trenes, lineas);
                case 10 -> System.out.println("Saliendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 10);
    }

    public static int menuABM() {
        System.out.println("----------------------------------------------------");
        System.out.println("| 1. Agregar                                       |");
        System.out.println("| 2. Modificar                                     |");
        System.out.println("| 3. Eliminar                                      |");
        System.out.println("| 4. Volver                                        |");
        System.out.println("----------------------------------------------------");
        System.out.print("Ingrese una opcion: ");
        return TecladoIn.readLineInt();
    }

    private static Tren buscarTrenSegunInterno(DiccionarioHash trenes) {
        int interno = 0;
        boolean seguir = false;
        do {
            System.out.print("Ingrese el interno del tren: ");
            interno = TecladoIn.readLineInt();
            seguir = trenes.existeClave(interno);
            if (!seguir) {
                System.out.println("!!!ERROR: El tren no existe");
            }
        } while (!seguir);
        return (Tren) trenes.obtenerDato(interno);
    }

    private static Linea pedirLinea(MapeoAMuchos lineasDisponibles) {
        Linea linea = null;
        boolean seguir;
        do {
            if (!lineasDisponibles.esVacio()) {
                System.out.println(lineasDisponibles.obtenerConjuntoDominio());
                System.out.print("Ingrese el nombre de la linea: ");
                linea = new Linea(TecladoIn.readLine().toUpperCase());

                if (lineasDisponibles.obtenerConjuntoDominio().localizar(linea) == -1) {
                    System.out.println("!!!ERROR: No existe la linea " + linea);
                    seguir = false;
                } else if (linea.equals(new Linea("0"))) {
                    linea = new Linea("LIBRE");
                    seguir = true;
                } else {
                    seguir = true;
                }
            } else {
                System.out.println("No hay lineas disponibles, se asignara \"LIBRE\"");
                linea = new Linea("LIBRE");
                seguir = true;
            }
        } while (!seguir);
        return linea;
    }

    private static int pedirVagonesCarga() {
        int vagonesCarga = 0;
        do {
            System.out.print("Ingrese la cantidad de vagones de carga: ");
            vagonesCarga = TecladoIn.readLineInt();
            if (vagonesCarga < 0) {
                System.out.println("!!!ERROR: La cantidad de vagones de carga debe ser mayor o igual a 0");
            }
        } while (vagonesCarga < 0);
        return vagonesCarga;
    }

    private static int pedirVagonesPasajeros() {
        int vagonesPasajeros = 0;
        do {
            System.out.print("Ingrese la cantidad de vagones de pasajeros: ");
            vagonesPasajeros = TecladoIn.readLineInt();
            if (vagonesPasajeros < 0) {
                System.out.println("!!!ERROR: La cantidad de vagones de pasajeros debe ser mayor o igual a 0");
            }
        } while (vagonesPasajeros < 0);
        return vagonesPasajeros;
    }

    private static String pedirCombustible() {
        String combustible = "";
        boolean seguir = false;
        do {
            System.out.print("Ingrese el tipo de combustible (1-DIESEL, 2-ELECTRICIDAD, 3-NAFTA, 4-GNC): ");
            switch (TecladoIn.readLineInt()) {
                case 1 -> combustible = "DIESEL";
                case 2 -> combustible = "ELECTRICIDAD";
                case 3 -> combustible = "NAFTA";
                case 4 -> combustible = "GNC";
                default -> System.out.println("!!!ERROR: El tipo de combustible ingresado no es valido");
            }
            if (!combustible.equals("")) {
                seguir = true;
            }
        } while (!seguir);
        return combustible;
    }

    private static int pedirInterno() {
        int interno;
        do {
            System.out.print("Ingrese el interno del tren: ");
            interno = TecladoIn.readLineInt();
        } while (interno < 0);
        return interno;
    }

    private static void abmTrenes(DiccionarioHash trenes, MapeoAMuchos lineas) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| ABM TRENES                                       |");
            System.out.println("----------------------------------------------------");
            System.out.println(trenes);
            opcion = menuABM();
            if (trenes.esVacio() && (opcion == 2 || opcion == 3)) {
                System.out.println("No hay trenes para modificar o eliminar");
                opcion = 0;
            }
            switch (opcion) {
                case 1 -> agregarTren(trenes, lineas);
                case 2 -> modificarTren(buscarTrenSegunInterno(trenes), lineas);
                case 3 -> eliminarTren(trenes);
                case 4 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 4);
    }

    private static void agregarTren(DiccionarioHash trenes, MapeoAMuchos lineas) {
        Tren tren = new Tren(pedirCombustible(), pedirVagonesCarga(), pedirVagonesPasajeros(), pedirLinea(lineas));
        if (trenes.insertar(Tren.ultimoID(), tren)) {
            Archivos.escribirLog("✅ Se agrego el tren " + tren);
        } else {
            Archivos.escribirLog("⛔️ No se pudo agregar el tren");
        }
    }

    private static void modificarTren(Tren tren, MapeoAMuchos lineas) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| MODIFICAR TREN                                   |");
            System.out.println("----------------------------------------------------");
            System.out.println("Tren actualmente:\t " + tren);
            System.out.println("1. Cambiar combustible");
            System.out.println("2. Cambiar cantidad de vagones de pasajeros");
            System.out.println("3. Cambiar cantidad de vagones de carga");
            System.out.println("4. Cambiar linea");
            System.out.println("5. Volver");
            System.out.print("Ingrese una opcion: ");
            opcion = TecladoIn.readLineInt();
            switch (opcion) {
                case 1 -> cambiarCombustible(tren);
                case 2 -> cambiarVagonesPasajeros(tren);
                case 3 -> cambiarVagonesCarga(tren);
                case 4 -> cambiarLinea(tren, lineas);
                case 5 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 5);
    }

    private static void cambiarCombustible(Tren tren) {
        if (tren != null) {
            String combustibleAnterior = tren.getCombustible();
            tren.setCombustible(pedirCombustible());
            String combustibleNuevo = tren.getCombustible();
            Archivos.escribirLog("✅ Se cambio el combustible del tren con interno " + tren.getId() + " de "
                    + combustibleAnterior + " a " + combustibleNuevo);
        } else {
            Archivos.escribirLog("⛔ No existe el tren buscado");
        }
    }

    private static void cambiarVagonesPasajeros(Tren tren) {
        if (tren != null) {
            int vagonesPasajerosAnterior = tren.getCantidadVagonesPasajeros();
            tren.setCantidadVagonesPasajeros(pedirVagonesPasajeros());
            int vagonesPasajerosNuevo = tren.getCantidadVagonesPasajeros();
            Archivos.escribirLog("✅ Se cambio la cantidad de vagones de pasajeros del tren con interno " + tren.getId()
                    + " de " + vagonesPasajerosAnterior + " a " + vagonesPasajerosNuevo);
        } else {
            Archivos.escribirLog("⛔ No existe el tren buscado");
        }
    }

    private static void cambiarVagonesCarga(Tren tren) {
        if (tren != null) {
            int vagonesCargaAnterior = tren.getCantidadVagonesCarga();
            tren.setCantidadVagonesCarga(pedirVagonesCarga());
            int vagonesCargaNuevo = tren.getCantidadVagonesCarga();
            Archivos.escribirLog("✅ Se cambio la cantidad de vagones de carga del tren con interno " + tren.getId()
                    + " de " + vagonesCargaAnterior + " a " + vagonesCargaNuevo);
        } else {
            Archivos.escribirLog("⛔ No existe el tren buscado");
        }
    }

    private static void cambiarLinea(Tren tren, MapeoAMuchos lineas) {
        if (tren != null) {
            Linea lineaAnterior = tren.getLinea();
            tren.setLinea(pedirLinea(lineas));
            Linea lineaNueva = tren.getLinea();
            Archivos.escribirLog("✅ Se cambio la linea del tren con interno " + tren.getId() + " de " + lineaAnterior
                    + " a " + lineaNueva);
        } else {
            Archivos.escribirLog("⛔ No existe el tren buscado");
        }
    }

    private static void eliminarTren(DiccionarioHash trenes) {
        int interno = pedirInterno();
        if (trenes.eliminar(interno)) {
            Archivos.escribirLog("✅ Se elimino el tren con interno " + interno);
        } else {
            Archivos.escribirLog("⛔ No existe el tren buscado");
        }
    }

    private static Estacion buscarEstacionPorNombre(DiccionarioAVL estaciones) {
        String nombre = "";
        boolean seguir;
        do {
            System.out.print("Ingrese el nombre de la estacion: ");
            nombre = TecladoIn.readLine();
            seguir = estaciones.existeClave(nombre);
            if (!seguir) {
                System.out.println("!!!ERROR: Estacion no encontrada");
            }
        } while (!seguir);
        return (Estacion) estaciones.obtenerDato(nombre);

    }

    private static int pedirCantidadPlataformas() {
        int cantidadPlataformas = 0;
        boolean seguir = false;
        do {
            System.out.print("Ingrese la cantidad de plataformas: ");
            cantidadPlataformas = TecladoIn.readLineInt();
            if (cantidadPlataformas <= 0) {
                System.out.println("!!!ERROR: Ingrese un numero mayor o igual a 0");
            } else {
                seguir = true;
            }
        } while (!seguir);
        return cantidadPlataformas;
    }

    private static int pedirCantidadVias() {
        int cantidadVias = 0;
        boolean seguir = false;
        do {
            System.out.print("Ingrese la cantidad de vias: ");
            cantidadVias = TecladoIn.readLineInt();
            if (cantidadVias <= 0) {
                System.out.println("!!!ERROR: Ingrese un numero mayor o igual a 0");
            } else {
                seguir = true;
            }
        } while (!seguir);
        return cantidadVias;
    }

    private static String pedirCodigoPostal() {
        String codigoPostal = "";
        boolean seguir = false;
        do {
            System.out.print("Ingrese el codigo postal: ");
            codigoPostal = TecladoIn.readLine().toUpperCase();
            if (codigoPostal.length() == 0) {
                System.out.println("!!!ERROR: Ingrese un codigo postal");
            } else {
                seguir = true;
            }
        } while (!seguir);
        return codigoPostal;
    }

    private static String pedirCiudad() {
        String ciudad = "";
        boolean seguir = false;
        do {
            System.out.print("Ingrese la ciudad: ");
            ciudad = TecladoIn.readLine().toUpperCase();
            if (ciudad.length() == 0) {
                System.out.println("!!!ERROR: Ingrese una ciudad");
            } else {
                seguir = true;
            }
        } while (!seguir);
        return ciudad;
    }

    private static int pedirAltura() {
        int altura = 0;
        boolean seguir = false;
        do {
            System.out.print("Ingrese la altura: ");
            altura = TecladoIn.readLineInt();
            if (altura <= 0) {
                System.out.println("!!!ERROR: Ingrese una altura mayor a 0");
            } else {
                seguir = true;
            }
        } while (!seguir);
        return altura;
    }

    private static String pedirNombreCalle() {
        String calle;
        boolean seguir;
        do {
            System.out.print("Ingrese la calle de la estacion: ");
            calle = TecladoIn.readLine().toUpperCase();
            if (!calle.equals("")) {
                seguir = true;
            } else {
                System.out.println("!!!ERROR: No puede ingresar un nombre vacio");
                seguir = false;
            }
        } while (!seguir);
        return calle;
    }

    private static String pedirNombreEstacion() {
        String nombre;
        boolean seguir;
        do {
            System.out.print("Ingrese el nombre de la estacion: ");
            nombre = TecladoIn.readLine().toUpperCase();
            if (!nombre.equals("")) {
                seguir = true;
            } else {
                System.out.println("!!!ERROR: No puede ingresar un nombre vacio");
                seguir = false;
            }
        } while (!seguir);
        return nombre;
    }

    private static void abmEstaciones(DiccionarioAVL estaciones, Grafo mapa) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| ABM ESTACIONES                                   |");
            System.out.println("----------------------------------------------------");
            System.out.println(estaciones);
            opcion = menuABM();
            if (estaciones.esVacio() && (opcion == 2 || opcion == 3)) {
                System.out.println("No se puede agregar o eliminar estaciones, no hay estaciones cargadas");
                opcion = 0;
            }
            switch (opcion) {
                case 1 -> agregarEstacion(estaciones, mapa);
                case 2 -> modificarEstacion(buscarEstacionPorNombre(estaciones));
                case 3 -> eliminarEstacion(estaciones, mapa);
                case 4 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 4);
    }

    private static void agregarEstacion(DiccionarioAVL estaciones, Grafo mapa) {
        String nombre = pedirNombreEstacion();
        if (!estaciones.existeClave(nombre)) {
            Estacion estacion = new Estacion(nombre, pedirNombreCalle(), pedirAltura(), pedirCiudad(),
                    pedirCodigoPostal(), pedirCantidadVias(), pedirCantidadPlataformas());
            if (estaciones.insertar(nombre, estacion)) {
                Archivos.escribirLog("✅ Estacion " + nombre + " agregada a las estaciones");
            } else {
                Archivos.escribirLog("⛔ Error al agregar la estacion " + nombre + " a las estaciones");
            }
            if (mapa.insertarVertice(estacion)) {
                Archivos.escribirLog("✅ Estacion " + nombre + " agregada al mapa");
            } else {
                Archivos.escribirLog("⛔ Error al agregar la estacion " + nombre + " al mapa");
            }
        } else {
            Archivos.escribirLog("⛔ Ya existe una estacion con el nombre " + nombre);
        }
    }

    private static void modificarEstacion(Estacion estacion) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| MODIFICAR ESTACION                               |");
            System.out.println("----------------------------------------------------");
            System.out.println("Estación actualmente:\t" + estacion);
            System.out.println("1. Modificar cantidad de vias");
            System.out.println("2. Modificar cantidad de plataformas");
            System.out.println("3. Volver");
            System.out.println("----------------------------");
            System.out.print("Ingrese una opcion: ");
            opcion = TecladoIn.readLineInt();
            switch (opcion) {
                case 1 -> modificarCantidadVias(estacion);
                case 2 -> modificarCantidadPlataformas(estacion);
                case 3 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion invalida");
            }
        } while (opcion != 3);

    }

    private static void modificarCantidadVias(Estacion estacion) {
        if (estacion != null) {
            int cantidadViasAntes = estacion.getCantidadVias();
            estacion.setCantidadVias(pedirCantidadVias());
            int cantidadViasDespues = estacion.getCantidadVias();
            Archivos.escribirLog("✅ Cantidad de vias de la estacion " + estacion.getNOMBRE() + " modificada de "
                    + cantidadViasAntes + " a " + cantidadViasDespues);
        } else {
            Archivos.escribirLog("⛔ No existe la estacion");
        }
    }

    private static void modificarCantidadPlataformas(Estacion estacion) {
        if (estacion != null) {
            int cantidadPlataformasAntes = estacion.getCantidadPlataformas();
            estacion.setCantidadPlataformas(pedirCantidadPlataformas());
            int cantidadPlataformasDespues = estacion.getCantidadPlataformas();
            Archivos.escribirLog("✅ Cantidad de plataformas de la estacion " + estacion.getNOMBRE() + " modificada de "
                    + cantidadPlataformasAntes + " a " + cantidadPlataformasDespues);
        } else {
            Archivos.escribirLog("⛔ No existe la estacion");
        }
    }

    private static void eliminarEstacion(DiccionarioAVL estaciones, Grafo mapa) {
        String nombre = pedirNombreEstacion();
        if (estaciones.existeClave(nombre)) {
            if (estaciones.eliminar(nombre)) {
                Archivos.escribirLog("✅ Estacion " + nombre + " eliminada de las estaciones");
            } else {
                Archivos.escribirLog("⛔ Error al eliminar la estacion " + nombre + " de las estaciones");
            }
            Estacion estacion = (Estacion) estaciones.obtenerDato(nombre);
            if (mapa.insertarVertice(estacion)) {
                Archivos.escribirLog("✅ Estacion " + nombre + " eliminada del mapa");
            } else {
                Archivos.escribirLog("⛔ Error al eliminar la estacion " + nombre + " del mapa");
            }
        } else {
            Archivos.escribirLog("⛔ No existe una estacion con el nombre " + nombre);
        }
    }

    private static void abmLineas(MapeoAMuchos lineas, DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            int opcion;
            do {
                System.out.println("----------------------------------------------------");
                System.out.println("| ABM LINEAS                                       |");
                System.out.println("----------------------------------------------------");
                // System.out.println(lineas);
                opcion = menuABM();
                if (lineas.esVacio() && (opcion == 2 || opcion == 3)) {
                    System.out.println("No se puede agregar o eliminar lineas, no hay lineas cargadas");
                    opcion = 0;
                }
                switch (opcion) {
                    case 1 -> agregarLinea(lineas, estaciones);
                    case 2 -> modificarLinea(lineas, estaciones);
                    case 3 -> System.out.println("NO SE PUEDE ELIMINAR LINEAS DEBIDO A LA ESTRUCTURA UTILIZADA");
                    case 4 -> System.out.println("Volviendo");
                    default -> System.out.println("Opcion incorrecta");
                }
            } while (opcion != 4);
        } else {
            System.out.println("No se puede agregar o eliminar lineas, no hay estaciones cargadas");
        }
    }

    private static void agregarLinea(MapeoAMuchos lineas, DiccionarioAVL estaciones) {
        Linea linea = pedirLinea(lineas);
        if (lineas.obtenerConjuntoDominio().localizar(linea) == -1) {
            agregarEstacionALinea(lineas, linea, estaciones);
            Archivos.escribirLog("✅ Linea " + linea.getNombre() + " agregada");
        } else {
            Archivos.escribirLog("⛔ Ya existe una linea");
        }
    }

    private static void agregarEstacionALinea(MapeoAMuchos lineas, Linea linea, DiccionarioAVL estaciones) {
        System.out.println("Estaciones disponibles:\n" + estaciones.listarClaves());
        Estacion estacion = buscarEstacionPorNombre(estaciones);
        if (lineas.asociar(linea, estacion)) {
            Archivos.escribirLog("✅ Se agrego la estacion " + estacion.getNOMBRE() + " a la linea " + linea);
        } else {
            Archivos.escribirLog("⛔ Error al agregar la estacion " + estacion.getNOMBRE() + " a la linea " + linea);
        }
    }

    private static void modificarLinea(MapeoAMuchos lineas, DiccionarioAVL estaciones) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| MODIFICAR LINEA                                  |");
            System.out.println("----------------------------------------------------");
            System.out.println("Lineas actuales:\t" + lineas.obtenerConjuntoDominio());
            System.out.println("1. Agregar estaciones a la linea");
            System.out.println("2. Eliminar estaciones de la linea");
            System.out.println("3. Volver");
            System.out.println("----------------------------");
            System.out.print("Ingrese una opcion: ");
            opcion = TecladoIn.readLineInt();
            switch (opcion) {
                case 1 -> agregarEstacionALinea(lineas, pedirLinea(lineas), estaciones);
                case 2 -> eliminarEstacionALinea(lineas, estaciones);
                case 3 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 3);
    }

    private static void eliminarEstacionALinea(MapeoAMuchos lineas, DiccionarioAVL estaciones) {
        Linea linea = pedirLinea(lineas);
        if (lineas.obtenerConjuntoDominio().localizar(linea) > 0) {
            Estacion estacion = buscarEstacionPorNombre(estaciones);
            lineas.desasociar(linea, estacion);
            Archivos.escribirLog("✅ Estacion " + estacion.getNOMBRE() + " eliminada de la linea " + linea);
        } else {
            Archivos.escribirLog("⛔ No existe la linea");
        }
    }

    private static void abmVias(Grafo mapa, DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            int opcion;
            do {
                System.out.println("----------------------------------------------------");
                System.out.println("| ABM VIAS                                         |");
                System.out.println("----------------------------------------------------");
                System.out.println(mapa);
                opcion = menuABM();
                if (estaciones.esVacio() && (opcion == 2 || opcion == 3)) {
                    System.out.println("No se puede agregar o eliminar lineas, no hay estaciones cargadas");
                    opcion = 0;
                }
                switch (opcion) {
                    case 1 -> agregarVia(mapa, estaciones);
                    case 2 -> System.out.println("NO SE PERMITE MODIFICAR VIAS");
                    case 3 -> eliminarVia(mapa, estaciones);
                    case 4 -> System.out.println("Volviendo");
                    default -> System.out.println("Opcion incorrecta");
                }
            } while (opcion != 4);
        } else {
            System.out.println("!!!ERROR: No hay estaciones cargadas");
        }
    }

    private static void eliminarVia(Grafo mapa, DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            System.out.println("Estaciones disponibles:\n" + estaciones.listarClaves());
            Estacion estacionA = buscarEstacionPorNombre(estaciones);
            Estacion estacionB = buscarEstacionPorNombre(estaciones);
            if (estacionA.getNOMBRE().equals(estacionB.getNOMBRE())) {
                System.out.println("!!!ERROR: No existen vias entre la misma estacion");
            } else {
                if (mapa.existeArco(estacionA, estacionB)) {
                    if (mapa.eliminarArco(estacionA, estacionB)) {
                        System.out.println("Via eliminada");
                        Archivos.escribirLog(
                                "Se elimino la via entre " + estacionA.getNOMBRE() + " y " + estacionB.getNOMBRE());
                    } else {
                        System.out.println("!!!ERROR: No se pudo eliminar la via");
                    }
                } else {
                    System.out.println("!!!ERROR: No existe via entre las estaciones");
                }
            }
        } else {
            System.out.println("!!!ERROR: No hay estaciones cargadas");
            Archivos.escribirLog("!!!ERROR: No hay estaciones cargadas");
        }
    }

    private static void agregarVia(Grafo mapa, DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            Estacion estacionA = buscarEstacionPorNombre(estaciones);
            Estacion estacionB = buscarEstacionPorNombre(estaciones);
            int kilometros;
            if (estacionA.getNOMBRE().equals(estacionB.getNOMBRE())) {
                System.out.println("!!!ERROR: No se puede agregar una via a una misma estacion");
            } else {
                boolean seguir;
                do {
                    System.out.print("Ingrese los kilometros: ");
                    kilometros = TecladoIn.readLineInt();
                    if (kilometros < 1) {
                        System.out.println("!!!ERROR: No puede ingresar un kilometro menor a 1");
                        seguir = false;
                    } else {
                        seguir = true;
                    }
                } while (!seguir);
                mapa.insertarArco(estacionA, estacionB, kilometros);
                Archivos.escribirLog(
                        "Se agrego una via entre " + estacionA.getNOMBRE() + " y " + estacionB.getNOMBRE());
            }
        } else {
            System.out.println("!!!ERROR: No hay estaciones cargadas");
            Archivos.escribirLog("!!!ERROR: No hay estaciones cargadas");
        }
    }

    private static void consultasTrenes(DiccionarioHash trenes, MapeoAMuchos lineas) {
        if (!trenes.esVacio()) {
            int opcion;
            do {
                System.out.println("----------------------------------------------------");
                System.out.println("| CONSULTAS TRENES                                 |");
                System.out.println("----------------------------------------------------");
                System.out.println("1. Consultar informacion de un tren");
                System.out.println("2. Consultar linea, y estaciones de un tren");
                System.out.println("3. Volver");
                System.out.println("----------------------------");
                System.out.print("Ingrese una opcion: ");
                opcion = TecladoIn.readLineInt();
                switch (opcion) {
                    case 1 -> System.out.println(buscarTrenSegunInterno(trenes));
                    case 2 -> consutlarRecorridoTren(trenes, lineas);
                    case 3 -> System.out.println("Volviendo");
                    default -> System.out.println("Opcion incorrecta");
                }
            } while (opcion != 3);
        } else {
            System.out.println("!!!ERROR: No hay trenes cargados");
        }
    }

    private static void consutlarRecorridoTren(DiccionarioHash trenes, MapeoAMuchos lineas) {
        Tren tren = buscarTrenSegunInterno(trenes);
        System.out.println("El tren " + tren.getId() + " esta en la linea " + tren.getLinea());
        if (!tren.getLinea().equals(new Linea("LIBRE"))) {
            System.out.println("El tren visita las estaciones:\n" + lineas.obtenerValores(tren.getLinea()));
        } else {
            System.out.println("El tren no tiene destinos");
        }
    }

    private static void consultasEstaciones(DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            int opcion;
            do {
                System.out.println("----------------------------------------------------");
                System.out.println("| CONSULTAS ESTACIONES                             |");
                System.out.println("----------------------------------------------------");
                System.out.println("1. Consultar informacion de una estacion");
                System.out.println("2. Consultar destinos de una estacion");
                System.out.println("3. Volver");
                System.out.print("Ingrese una opcion: ");
                opcion = TecladoIn.readLineInt();
                switch (opcion) {
                    case 1 -> System.out.println(estaciones.obtenerDato(pedirNombreEstacion()));
                    case 2 -> {
                        String estacion = pedirNombreEstacion();
                        System.out.println(estaciones.listarRango(estacion, estacion + "zzz"));
                    }
                    case 3 -> System.out.println("Volviendo");
                    default -> System.out.println("Opcion incorrecta");
                }
            } while (opcion != 3);
        } else {
            System.out.println("!!!ERROR: No hay estaciones cargadas");
        }
    }

    private static void consultasViajes(Grafo mapa, DiccionarioAVL estaciones) {
        if (!estaciones.esVacio()) {
            int opcion;
            do {
                System.out.println("----------------------------------------------------");
                System.out.println("| CONSULTAS VIAJES                                 |");
                System.out.println("----------------------------------------------------");
                System.out.println("1. Consultar recorrido con menos kilometros");
                System.out.println("2. Consultar recorrido que pase por menos estaciones");
                System.out.println("3. Volver");
                System.out.print("Ingrese una opcion: ");
                opcion = TecladoIn.readLineInt();
                switch (opcion) {
                    case 1 -> recorridoMasCorto(mapa, buscarEstacionPorNombre(estaciones),
                            buscarEstacionPorNombre(estaciones));
                    case 2 -> recorridoMenosEstaciones(mapa, buscarEstacionPorNombre(estaciones),
                            buscarEstacionPorNombre(estaciones));
                    case 3 -> System.out.println("Volviendo");
                    default -> System.out.println("Opcion incorrecta");
                }
            } while (opcion != 3);
        } else {
            System.out.println("!!!ERROR: No hay estaciones cargadas");
        }
    }

    private static void recorridoMenosEstaciones(Grafo mapa, Estacion estacion, Estacion estacion2) {
        if (!estacion.equals(estacion2)) {
            System.out.println("Recorrido:\n" + mapa.caminoMasCorto(estacion, estacion2));
        } else {
            System.out.println("!!!ERROR: No existe un camino entre las estaciones ingresadas o son iguales");
        }
    }

    private static void recorridoMasCorto(Grafo mapa, Estacion estacionA, Estacion estacionB) {
        if (!estacionA.equals(estacionB)) {
            System.out.println("Recorrido:\n" + mapa.minimoPesoParaPasar(estacionA, estacionB));
        } else {
            System.out.println("!!!ERROR: No existe un camino entre las estaciones ingresadas o son iguales");
        }
    }

    private static void mostrarDatos(Grafo mapa, DiccionarioAVL estaciones, DiccionarioHash trenes,
            MapeoAMuchos lineas) {
        int opcion;
        do {
            System.out.println("----------------------------------------------------");
            System.out.println("| MOSTRAR DATOS                                    |");
            System.out.println("----------------------------------------------------");
            System.out.println("1. Mostrar Mapa");
            System.out.println("2. Mostrar Todos los Trenes");
            System.out.println("3. Mostrar Todas las Estaciones");
            System.out.println("4. Mostrar Todas las Lineas");
            System.out.println("5. Mostrar Todas las Vias");
            System.out.println("6. Volver");
            System.out.println("----------------------------");
            System.out.print("Ingrese una opcion: ");
            opcion = TecladoIn.readLineInt();
            switch (opcion) {
                case 1 -> {
                    System.out.println(mapa);
                    try {
                        Archivos.mostrarImagen();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> System.out.println(trenes);
                case 3 -> System.out.println(estaciones);
                case 4 -> System.out.println(lineas);
                case 5 -> System.out.println(mapa);
                case 6 -> System.out.println("Volviendo");
                default -> System.out.println("Opcion incorrecta");
            }
        } while (opcion != 6);
    }
}
