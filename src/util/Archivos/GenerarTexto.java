package util.Archivos;

import java.util.Random;

class GenerarTexto {
        public static void main(String[] args) {
                String estaciones[] = {
                                "NEUQUEN", "CIPOLLETTI", "ROCA", "ALLEN", "CENTENARIO", "SENILLOSA",
                                "VILLA EL CHOCON", "VILLA REGINA", "CHINA MUERTA", "VILLA MASCARDI",
                                "VILLA LA ANGOSTURA", "SAN MARTIN DE LOS ANDES", "VILLA MANZANO", "JUNIN DE LOS ANDES",
                                "ALUMINE", "ZAPALA", "CUTRAL CO", "PLAZA HUINCUL", "PRIMEROS PINOS", "VILLA PEHUENIA",
                                "PICUN LEUFU", "PIEDRA DEL AGUILA", "VILLA TRAFUL" };
                int i;
                for (i = 0; i < estaciones.length; i++) {
                        System.out.println("E;" +
                                        estaciones[i] + ";" +
                                        "DIRECCION-" + i + ";" +
                                        i + ";" +
                                        "CP-" + i + ";" +
                                        estaciones[i] + ";" +
                                        new Random().nextInt(5) + ";" +
                                        new Random().nextInt(5) + ";");
                }
                System.out.println("");
                String destinosLinea = "";
                for (i = 0; i < 20; i++) {
                        for (int j = 0; j < 5; j++) {
                                destinosLinea += estaciones[new Random().nextInt(estaciones.length)] + ";";
                        }
                        System.out.println("L;" +
                                        "LINEA-" + i + ";"
                                        + destinosLinea);
                        destinosLinea = "";
                }
                System.out.println("");
                for (i = 0; i < 20; i++) {
                        System.out.println("R;" +
                                        "ESTACION" + i + ";" +
                                        "ESTACION" + i + ";" +
                                        Math.round(new Random().nextFloat(10, 500)) + ";");
                }
                System.out.println("");
                String combustibles[] = { "DIESEL", "ELECTRICIDAD", "NAFTA", "GNC" };
                for (i = 0; i < 40; i++) {
                        System.out.println("T;"
                                        + combustibles[new Random().nextInt(combustibles.length)]
                                        + ";"
                                        + Math.round(new Random().nextFloat(0, 5)) + ";"
                                        + Math.round(new Random().nextFloat(0, 5)) + ";"
                                        + "LINEA-" + new Random().nextInt(20) + ";");
                }

        }

}
