package tools;

public class Lista {

    private Nodo cabecera;
    private int longitud;

    public Lista() {
        this.cabecera = null;
        this.longitud = 0;
    }

    public Lista clone() {
        Lista clon = new Lista();
        if (!esVacia()) {
            clon.cabecera = new Nodo(this.cabecera.getElem(), null);
            Nodo auxOrig = this.cabecera.getEnlace();
            Nodo auxClon = clon.cabecera;
            while (auxOrig != null) {
                auxClon.setEnlace(new Nodo(auxOrig.getElem(), null));
                auxOrig = auxOrig.getEnlace();
                auxClon = auxClon.getEnlace();
            }
            clon.longitud = this.longitud;
        }
        return clon;
    }

    public boolean eliminar(int pos) {

        boolean exito = true;
        if (pos < 1 || pos > this.longitud) {
            exito = false;
        } else {
            if (pos == 1) {
                this.cabecera = this.cabecera.getEnlace();
                this.longitud--;
            } else {
                Nodo aux = this.cabecera;
                int i = 1;
                while (i < pos - 1) {
                    aux = aux.getEnlace();
                    i++;
                }
                aux.setEnlace(aux.getEnlace().getEnlace());
                this.longitud--;
            }
        }
        return exito;
    }

    public boolean esVacia() {
        return this.cabecera == null;
    }

    public boolean insertar(Object nuevoElem, int pos) {
        boolean exito = true;
        if (pos < 1 || pos > this.longitud() + 1) {
            exito = false;
        } else {
            if (pos == 1) {
                this.cabecera = new Nodo(nuevoElem, this.cabecera);
            } else {
                Nodo aux = this.cabecera;
                int i = 1;
                while (i < pos - 1) {
                    aux = aux.getEnlace();
                    i++;
                }
                Nodo nuevo = new Nodo(nuevoElem, aux.getEnlace());
                aux.setEnlace(nuevo);
            }
            this.longitud++;
        }
        return exito;
    }

    public int localizar(Object buscado) {
        int res = -1;
        if (!esVacia()) {
            Nodo aux = this.cabecera;
            int i = 1;
            boolean corte = false;
            while (aux != null && !corte) {
                if (aux.getElem().equals(buscado)) {
                    res = i;
                    corte = true;
                }
                i++;
                aux = aux.getEnlace();
            }
        }
        return res;
    }

    public int longitud() {
        return this.longitud;
    }

    public Object recuperar(int pos) {
        Object res;
        if (pos < 1 || pos > this.longitud) {
            res = null;
        } else {
            int i = 1;
            Nodo aux = this.cabecera;
            while (i < pos) {
                aux = aux.getEnlace();
                i++;
            }
            res = aux.getElem();
        }
        return res;
    }

    public String toString() {
        String cadena = "ESTRUCTURA VACIA";
        if (!esVacia()) {
            cadena = "";
            Nodo aux = this.cabecera;
            while (aux != null) {
                cadena += aux.getElem() + " \n";
                aux = aux.getEnlace();
            }

        }
        return cadena;
    }

    public void vaciar() {
        this.cabecera = null;
        this.longitud = 0;
    }

    public void invertir() {
        Nodo ultimo = invertirAux(this.cabecera);
        ultimo.setEnlace(null);
    }

    private Nodo invertirAux(Nodo nodoActual) {
        if (nodoActual.getEnlace() == null) {
            this.cabecera = nodoActual;
        } else {
            Nodo nodoVolviendo = invertirAux(nodoActual.getEnlace());
            nodoVolviendo.setEnlace(nodoActual);
        }
        return nodoActual;
    }

    public Lista obtenerMultiplos(int num) {
        Lista res = new Lista();

        if (this.cabecera != null && num > 0 && num < this.longitud) {
            Nodo puntero = this.cabecera;
            int posActual = 1;

            while (puntero != null) {

                if (posActual % num == 0) {
                    Nodo nuevo = new Nodo(puntero.getElem(), null);

                    if (res.cabecera == null) {
                        res.cabecera = nuevo;
                    } else {
                        nuevo.setEnlace(res.cabecera);
                        res.cabecera = nuevo;
                    }

                    res.longitud += 1;
                }

                posActual += 1;
                puntero = puntero.getEnlace();

            }
        }
        return res;
    }

    public void eliminarApariciones(Object buscado) {
        Nodo punteroAvanzado = cabecera;
        Nodo punteroPrevio = null;

        while (punteroAvanzado != null) {
            if (punteroAvanzado.getElem().equals(buscado)) {
                punteroAvanzado = punteroAvanzado.getEnlace();

                if (punteroAvanzado == cabecera) {
                    cabecera = punteroAvanzado;
                } else {
                    punteroPrevio.setEnlace(punteroAvanzado);
                }

                this.longitud--;
            } else {
                punteroPrevio = punteroAvanzado;
                punteroAvanzado = punteroAvanzado.getEnlace();
            }
        }
    }

    public void insertarAnterior(Object elem1, Object elem2) {
        if (!esVacia()) {
            Nodo aux = this.cabecera;
            while (aux != null) {
                if (aux.getElem().equals(elem1)) {

                }

            }

        }

    }

}
