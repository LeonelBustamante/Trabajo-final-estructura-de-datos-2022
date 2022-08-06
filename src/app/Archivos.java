package app;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.*;
import tools.*;

class Archivos {
    // PARA CARGA AUTOMATICA SE DEBE CAMBIAR LA SIGUIENTE LINEA
    private static String path = "D:\\Documentos\\Leo\\Trabajo-final-estructura-de-datos-2022\\src\\util\\Archivos\\log.txt";
    private static int nroLinea = 1;

    public static void mostrarImagen(String dir) throws IOException {
        File file = new File(dir);
        BufferedImage bufferedImage = ImageIO.read(file);

        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        JFrame jFrame = new JFrame();
        jFrame.setSize(500, 500);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jFrame.add(jLabel);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void limpiarTXT() {
        try {
            FileWriter fw = new FileWriter(path);
            fw.write("");
            nroLinea = 1;
        } catch (IOException e) {
            System.out.println("Error al limpiar el archivo");
        }
    }

    public static void leer(String dir, Grafo map, DiccionarioAVL est, DiccionarioHash trenes, MapeoAMuchos lineas) {
        try {
            BufferedReader txt = new BufferedReader(new FileReader(dir));
            String linea = txt.readLine();
            while (linea != null) {
                cargaInicial(linea, map, est, trenes, lineas);
                linea = txt.readLine();
            }
            escribirLog("👍👍👍CARGA INICIAL DE DATOS COMPLETADA");
        } catch (IOException e) {
        }
    }

    public static void escribirLog(String txt) {
        File archivo = new File(path);
        try {
            if (!archivo.exists()) {
                archivo = new File(path);
            }
            try (FileWriter escribir = new FileWriter(archivo, true)) {
                escribir.write(nroLinea + "-\t\t" + txt + "\n");
                nroLinea++;
            }
        } catch (IOException e) {
            System.out.println("ERROR AL ESCRIBIR EL LOG");
        }
    }

    private static void cargaInicial(String renglon, Grafo mapa, DiccionarioAVL estaciones, DiccionarioHash trenes,
            MapeoAMuchos lineas) {
        StringTokenizer tokens = new StringTokenizer(renglon, ";");
        switch (tokens.nextToken()) {
            case "T" -> {
                int id = Integer.parseInt(tokens.nextToken());
                String combustible = tokens.nextToken();
                int cantidadVagonesPasajeros = Integer.parseInt(tokens.nextToken());
                int cantidadVagonesCarga = Integer.parseInt(tokens.nextToken());
                Linea linea = new Linea(tokens.nextToken());

                Tren tren = new Tren(id, combustible, cantidadVagonesPasajeros, cantidadVagonesCarga, linea);

                if (trenes.insertar(tren.getId(), tren)) {
                    Archivos.escribirLog("✅ Se agrego el tren " + tren);
                } else {
                    Archivos.escribirLog("⛔️ No se pudo agregar el tren");
                }
            }
            case "E" -> {
                String nombre = tokens.nextToken();
                String calle = tokens.nextToken();
                int altura = Integer.parseInt(tokens.nextToken());
                String ciudad = tokens.nextToken();
                String codigoPostal = tokens.nextToken();
                int vias = Integer.parseInt(tokens.nextToken());
                int plataformas = Integer.parseInt(tokens.nextToken());

                Estacion estacion = new Estacion(nombre, calle, altura, ciudad, codigoPostal, vias, plataformas);

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
            }
            case "L" -> {
                Linea lineaNueva = new Linea(tokens.nextToken());
                Estacion aux;
                for (int i = 0; i < 5; i++) {
                    aux = (Estacion) estaciones.obtenerDato(tokens.nextToken());
                    if (lineas.asociar(lineaNueva, aux)) {
                        escribirLog("✅ Se agrego la estacion " + aux.getNOMBRE() + " a la linea " + lineaNueva);
                    } else {
                        escribirLog("⛔ ERROR: La estacion " + aux.getNOMBRE() + " ya existe en la linea " + lineaNueva);
                    }
                }
            }
            case "R" -> {
                Estacion estacion1 = (Estacion) estaciones.obtenerDato(tokens.nextToken());
                Estacion estacion2 = (Estacion) estaciones.obtenerDato(tokens.nextToken());
                int distancia = Integer.parseInt(tokens.nextToken());
                if (mapa.insertarArco(estacion1, estacion2, distancia)) {
                    escribirLog("✅ Se agrego el arco " + estacion1.getNOMBRE() + "-" + estacion2.getNOMBRE()
                            + " con distancia " + distancia);
                } else {
                    escribirLog("⛔ ERROR: El arco ya existe");
                }
            }

        }
    }
}
