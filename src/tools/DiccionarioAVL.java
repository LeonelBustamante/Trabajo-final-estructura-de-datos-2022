package tools;

public class DiccionarioAVL {

    private NodoAVLDicc raiz;

    // CONSTRUCTOR
    public DiccionarioAVL() {
        this.raiz = null;
    }

    public boolean insertar(Comparable clave, Object dato) {
        boolean exito;

        if (raiz != null) {
            exito = insertarAux(clave, dato, raiz);
            raiz.recalcularAltura();
            rotacion(null, raiz, true);
        } else {
            raiz = new NodoAVLDicc(clave, dato);
            exito = true;
        }

        return exito;
    }

    private boolean insertarAux(Comparable clave, Object dato, NodoAVLDicc nodo) {
        boolean exito, esHijoIzq;

        if (clave.compareTo(nodo.getClave()) == 0) {
            exito = false;
        } else {
            if (clave.compareTo(nodo.getClave()) < 0) {
                if (nodo.getIzquierdo() == null) {
                    exito = true;
                    nodo.setIzquierdo(new NodoAVLDicc(clave, dato));
                } else {
                    exito = insertarAux(clave, dato, nodo.getIzquierdo());
                }

                esHijoIzq = true;
            } else {
                if (nodo.getDerecho() == null) {
                    exito = true;
                    nodo.setDerecho(new NodoAVLDicc(clave, dato));
                } else {
                    exito = insertarAux(clave, dato, nodo.getDerecho());
                }

                esHijoIzq = false;
            }

            if (exito) {
                nodo.recalcularAltura();

                if (esHijoIzq) {
                    rotacion(nodo, nodo.getIzquierdo(), true);
                } else {
                    rotacion(nodo, nodo.getDerecho(), false);
                }
                nodo.recalcularAltura();
            }

        }

        return exito;
    }

    private void rotacion(NodoAVLDicc padre, NodoAVLDicc hijo, boolean esHijoIzq) {
        int balance = balance(hijo);
        NodoAVLDicc nuevaRaizSubarbol;

        if (balance < -1) {
            if (balance(hijo.getDerecho()) <= 0) {
                nuevaRaizSubarbol = rotarIzquierda(hijo);
            } else {
                nuevaRaizSubarbol = rotarDerechaIzquierda(hijo);
            }
        } else if (balance > 1) {
            if (balance(hijo.getIzquierdo()) >= 0) {
                nuevaRaizSubarbol = rotarDerecha(hijo);
            } else {
                nuevaRaizSubarbol = rotarIzquierdaDerecha(hijo);
            }
        } else {
            nuevaRaizSubarbol = null;
        }

        if (nuevaRaizSubarbol != null) {
            if (padre != null) {
                if (esHijoIzq) {
                    padre.setIzquierdo(nuevaRaizSubarbol);
                } else {
                    padre.setDerecho(nuevaRaizSubarbol);
                }
            } else {
                raiz = nuevaRaizSubarbol;
            }

        }
    }

    private int balance(NodoAVLDicc padre) {
        int altIzq, altDer;

        if (padre.getIzquierdo() != null) {
            altIzq = padre.getIzquierdo().getAltura();
        } else {
            altIzq = -1;
        }

        if (padre.getDerecho() != null) {
            altDer = padre.getDerecho().getAltura();
        } else {
            altDer = -1;
        }
        return altIzq - altDer;
    }

    private NodoAVLDicc rotarIzquierda(NodoAVLDicc padre) {
        NodoAVLDicc hijo = padre.getDerecho(), temp = hijo.getIzquierdo();

        hijo.setIzquierdo(padre);
        padre.setDerecho(temp);
        padre.recalcularAltura();
        hijo.recalcularAltura();

        return hijo;
    }

    private NodoAVLDicc rotarDerecha(NodoAVLDicc padre) {

        NodoAVLDicc hijo = padre.getIzquierdo(), temp = hijo.getDerecho();

        hijo.setDerecho(padre);
        padre.setIzquierdo(temp);
        padre.recalcularAltura();
        hijo.recalcularAltura();
        return hijo;
    }

    private NodoAVLDicc rotarIzquierdaDerecha(NodoAVLDicc padre) {

        padre.setIzquierdo(rotarIzquierda(padre.getIzquierdo()));
        return rotarDerecha(padre);
    }

    private NodoAVLDicc rotarDerechaIzquierda(NodoAVLDicc padre) {

        padre.setDerecho(rotarDerecha(padre.getDerecho()));
        return rotarIzquierda(padre);
    }

    public boolean eliminar(Comparable clave) {
        boolean exito = false;
        if (!esVacio()) {
            exito = eliminarAux(clave, raiz, null, true);
            if (exito) {
                raiz.recalcularAltura();
                rotacion(null, raiz, true);
                raiz.recalcularAltura();
            }
        }

        return exito;
    }

    private boolean eliminarAux(Comparable clave, NodoAVLDicc nodo, NodoAVLDicc padre, boolean esHijoIzq) {
        int comparacion;
        boolean exito = false;

        if (nodo != null) {
            comparacion = clave.compareTo(nodo.getClave());
            if (comparacion == 0) {
                // ENCONTRADO
                exito = true;
                if (nodo.getIzquierdo() == null && nodo.getDerecho() == null) {
                    caso1(padre, esHijoIzq);
                    padre.recalcularAltura();
                } else if (nodo.getIzquierdo() != null && nodo.getDerecho() == null) {
                    caso2(padre, esHijoIzq, true);
                    padre.recalcularAltura();
                } else if (nodo.getIzquierdo() == null && nodo.getDerecho() != null) {
                    caso2(padre, esHijoIzq, false);
                    padre.recalcularAltura();
                } else {
                    caso3(nodo);
                }
            } else if (comparacion < 0) {
                exito = eliminarAux(clave, nodo.getIzquierdo(), nodo, true);
            } else {
                exito = eliminarAux(clave, nodo.getDerecho(), nodo, false);
            }

            if (exito) {
                rotacion(padre, nodo, esHijoIzq);
                nodo.recalcularAltura();
            }
        }

        return exito;
    }

    private void caso1(NodoAVLDicc padre, boolean esHijoIzq) {
        if (padre != null) {
            if (esHijoIzq) {
                padre.setIzquierdo(null);
            } else {
                padre.setDerecho(null);
            }
        } else {
            raiz = null;
        }
    }

    private void caso2(NodoAVLDicc padre, boolean esHijoIzq, boolean esNietoIzq) {
        if (padre != null) {
            if (esHijoIzq) {
                if (esNietoIzq) {
                    padre.setIzquierdo(padre.getIzquierdo().getIzquierdo());
                } else {
                    padre.setIzquierdo(padre.getIzquierdo().getDerecho());
                }
            } else {
                if (esNietoIzq) {
                    padre.setDerecho(padre.getDerecho().getIzquierdo());
                } else {
                    padre.setDerecho(padre.getDerecho().getDerecho());
                }
            }
        } else {
            if (esHijoIzq) {
                raiz = raiz.getIzquierdo();
            } else {
                raiz = raiz.getDerecho();
            }
        }
    }

    private void caso3(NodoAVLDicc actual) {
        NodoAVLDicc candidato = candidato(actual.getDerecho(), actual, actual);

        actual.setClave(candidato.getClave());
        actual.setDato(candidato.getDato());
    }

    private NodoAVLDicc candidato(NodoAVLDicc actual, NodoAVLDicc padre, NodoAVLDicc aEliminar) {
        NodoAVLDicc candidato;

        if (actual.getIzquierdo() != null) {
            candidato = candidato(actual.getIzquierdo(), actual, aEliminar);
            actual.recalcularAltura();
            if (padre.equals(aEliminar)) {
                rotacion(padre, actual, false);
            } else {
                rotacion(padre, actual, true);
            }
            actual.recalcularAltura();
        } else {
            if (padre.equals(aEliminar)) {
                padre.setDerecho(actual.getDerecho());
            } else {
                padre.setIzquierdo(actual.getDerecho());
            }
            candidato = actual;
        }
        return candidato;
    }

    private NodoAVLDicc obtenerNodo(NodoAVLDicc nodo, Comparable key) {
        NodoAVLDicc exito = null;
        if (nodo != null) {
            if (key.compareTo(nodo.getClave()) == 0) {
                exito = nodo;
            } else if (key.compareTo(nodo.getClave()) < 0) {
                if (nodo.getIzquierdo() != null) {
                    exito = obtenerNodo(nodo.getIzquierdo(), key);
                }

            } else {
                if (nodo.getDerecho() != null) {
                    exito = obtenerNodo(nodo.getDerecho(), key);
                }

            }
        }
        return exito;
    }

    public boolean existeClave(Comparable key) {
        boolean exito = false;
        if (!this.esVacio()) {
            exito = existeClaveAux(this.raiz, key);
        }
        return exito;
    }

    private boolean existeClaveAux(NodoAVLDicc nodo, Comparable key) {
        boolean exito = false;
        if (nodo != null) {
            if (key.compareTo(nodo.getClave()) == 0) {
                exito = true;
            } else if (key.compareTo(nodo.getClave()) < 0) {
                if (nodo.getIzquierdo() != null) {
                    exito = existeClaveAux(nodo.getIzquierdo(), key);
                }

            } else {
                if (nodo.getDerecho() != null) {
                    exito = existeClaveAux(nodo.getDerecho(), key);
                }

            }
        }
        return exito;
    }

    public Object obtenerDato(Comparable key) {
        Object dato = null;
        NodoAVLDicc aux = obtenerNodo(this.raiz, key);
        if (aux != null) {
            dato = aux.getDato();
        }
        return dato;
    }

    public boolean esVacio() {
        return (this.raiz == null);
    }

    public Lista listarClaves() {
        Lista lis = new Lista();
        listarClavesAux(this.raiz, lis);
        return lis;
    }

    private void listarClavesAux(NodoAVLDicc nodo, Lista lis) {

        if (nodo != null) {
            listarClavesAux(nodo.getIzquierdo(), lis);
            lis.insertar(nodo.getClave(), lis.longitud() + 1);

            listarClavesAux(nodo.getDerecho(), lis);
        }

    }

    public Lista listarDatos() {
        Lista lis = new Lista();
        listarDatosAux(this.raiz, lis);
        return lis;
    }

    private void listarDatosAux(NodoAVLDicc nodo, Lista lis) {

        if (nodo != null) {
            listarDatosAux(nodo.getIzquierdo(), lis);

            lis.insertar(nodo.getDato(), lis.longitud() + 1);

            listarDatosAux(nodo.getDerecho(), lis);
        }

    }

    public DiccionarioAVL clone() {
        DiccionarioAVL copia = new DiccionarioAVL();

        if (!this.esVacio()) {
            copia.raiz = clonAux(this.raiz);
        }

        return copia;
    }

    private NodoAVLDicc clonAux(NodoAVLDicc nodo) {
        NodoAVLDicc nuevoNodo = new NodoAVLDicc(nodo.getClave(), nodo.getDato());

        if (nodo.getIzquierdo() != null) {
            nuevoNodo.setIzquierdo(clonAux(nodo.getIzquierdo()));
        }
        if (nodo.getDerecho() != null) {
            nuevoNodo.setDerecho(clonAux(nodo.getDerecho()));
        }
        return nuevoNodo;
    }

    public void vaciar() {
        this.raiz = null;
    }

    public String toString() {
        String texto = "";

        if (this.esVacio()) {
            texto = "SIN INFORMACION";
        } else {
            texto = toStringAux(this.raiz, texto);
        }

        return texto;
    }

    private String toStringAux(NodoAVLDicc nodo, String texto) {

        if (nodo != null) {
            if (nodo.getIzquierdo() != null && nodo.getDerecho() != null) {
                texto += " NODO: " + nodo.getClave().toString()
                        + " HI: " + nodo.getIzquierdo().getClave().toString() + " "
                        + " HD:  " + nodo.getDerecho().getClave().toString() + "\n";
            } else {
                if (nodo.getIzquierdo() == null && nodo.getDerecho() != null) {
                    texto += " NODO: " + nodo.getClave().toString() + " HI: --" + " HD: "
                            + nodo.getDerecho().getClave().toString() + "\n";
                } else if (nodo.getDerecho() == null && nodo.getIzquierdo() != null) {
                    texto += " NODO: " + nodo.getClave().toString() + " HI: "
                            + nodo.getIzquierdo().getClave().toString() + " " + " HD: --" + "\n";
                } else {
                    texto += " NODO: " + nodo.getClave().toString() + " HI: --" + " HD: --"
                            + "\n";
                }
            }
            texto = toStringAux(nodo.getIzquierdo(), texto);
            texto = toStringAux(nodo.getDerecho(), texto);
        }

        return texto;
    }

    public Lista listarRango(Comparable keyMenor, Comparable keyMayor) {
        Lista lis = new Lista();
        listarRangoAux(this.raiz, lis, keyMenor, keyMayor);
        return lis;
    }

    private void listarRangoAux(NodoAVLDicc nodo, Lista lis, Comparable keyMenor, Comparable keyMayor) {

        if (nodo != null) {
            if (nodo.getIzquierdo() != null && nodo.getClave().compareTo(keyMenor) > 0) {
                listarRangoAux(nodo.getIzquierdo(), lis, keyMenor, keyMayor);
            }
            if (nodo.getClave().compareTo(keyMenor) >= 0 && nodo.getClave().compareTo(keyMayor) <= 0) {
                lis.insertar(nodo.getDato(), lis.longitud() + 1);
            }
            if (nodo.getDerecho() != null && nodo.getClave().compareTo(keyMayor) < 0) {
                listarRangoAux(nodo.getDerecho(), lis, keyMenor, keyMayor);
            }
        }
    }
}