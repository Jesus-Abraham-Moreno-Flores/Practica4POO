package org.example.practica4poo;

import java.util.ArrayList;
import java.util.Collections;

public class baraja {
    private ArrayList<carta> cartas;

    public baraja() {
        cartas = new ArrayList<>();
    }

    public void crearBaraja() {
        String[]colores = {"red","blue","green","yellow"};
        String[]cartaEspecial = {"Bloquear","Reversa","+2"};
        String[]comodines = {"Comidin","+4"};

        //Se generan las cartas normales de 0 al 9 para cada color
        for (int i = 0; i < colores.length; i++) {
            for (int j = 0; j < 10 ; j++) {
                carta molde = new carta(j, colores[i]);
                cartas.add(molde);
            }
        }

        //Para cartas especiales que tienen color
        for (String color : colores) {
            for (String especial : cartaEspecial) {
                cartas.add(new carta(especial, color));
            }
        }

        //Este ultimo añade los comodines
        for (String comodin : comodines) {
            for (int i = 0; i < 4; i++) {
                cartas.add(new carta(comodin));
            }
        }
    }

    public void mostrarBaraja(){
        cartas.forEach(carta ->
                System.out.println(carta));
        }

    public void barajar(){
        Collections.shuffle(cartas);
    }

    //Tomar carta del mazo
    public carta robarCarta(){
        if(!cartas.isEmpty()){
            return cartas.remove(0);
        } else {
            return null;
        }
    }

    public int cantidadCartas(){
        return cartas.size();
    }

}
