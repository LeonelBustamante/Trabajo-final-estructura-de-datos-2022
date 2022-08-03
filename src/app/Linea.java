package app;

public class Linea {

    private final String NOMBRE;

    public Linea(String nombre) {
        this.NOMBRE = nombre;
    }

    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public String toString() {
        return NOMBRE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Linea && ((Linea) obj).NOMBRE.equals(this.NOMBRE);
    }

    @Override
    public int hashCode() {
        return NOMBRE.hashCode();
    }
}
