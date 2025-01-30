package morenotauiledd1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author moren
 */
/**
 * Implementación manual de una cola genérica.
 *
 * @param <T> Tipo de elementos almacenados en la cola.
 */
public class MiCola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;

    public MiCola() {
        this.frente = null;
        this.fin = null;
    }

    /**
     * Encola un elemento.
     *
     * @param elemento Elemento a encolar.
     */
    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            frente = nuevoNodo;
        } else {
            fin.siguiente = nuevoNodo;
        }
        fin = nuevoNodo;
    }

    /**
     * Desencola un elemento.
     *
     * @return Elemento desencolado.
     */
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        T elemento = frente.valor;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return elemento;
    }

    /**
     * Verifica si la cola está vacía.
     *
     * @return true si la cola está vacía, false en caso contrario.
     */
    public boolean estaVacia() {
        return frente == null;
    }

    /**
     * Clase interna para representar un nodo de la cola.
     */
    private static class Nodo<T> {
        T valor;
        Nodo<T> siguiente;

        public Nodo(T valor) {
            this.valor = valor;
            this.siguiente = null;
        }
    }
}