package org.example.practica4poo;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private ArrayList<carta> mano;

    public Jugador(String nombre){
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    public void tomarCarta(carta carta){
        mano.add(carta);
    }

    public carta jugarCartaValida(int index, carta cartaEnJuego) {
        if(index >= 0 && index < mano.size()) {
            carta seleccionada = mano.get(index);
            if(seleccionada.esCartaValida(cartaEnJuego)) {
                return mano.remove(index);
            } else {
                System.out.println("No se puede jugar esa carta");
            }
        }
        return null;
    }

    public void mostrarMano(){
        System.out.println("\nCartas de " + nombre + ":");
        for(int i = 0; i < mano.size(); i++){
            System.out.println((i + 1) + ". " + mano.get(i));
        }
    }

    public int cantidadCartas(){
        return mano.size();
    }

    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public ArrayList<carta> getMano(){
        return mano;
    }
}
