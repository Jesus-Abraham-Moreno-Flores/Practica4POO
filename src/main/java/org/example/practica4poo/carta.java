package org.example.practica4poo;

public class carta {
    private int numeroCarta;
    private String color;
    private String tipo;

    public carta(int numeroCarta, String color) {
        this.numeroCarta = numeroCarta;
        this.color = color;
        this.tipo = "normal";
    }

    public carta(String tipo, String color) {
        this.numeroCarta = -1;
        this.color = color;
        this.tipo = tipo;
    }

    public carta(String tipo) {
        this.numeroCarta = -1;
        this.color = "black";
        this.tipo = tipo;
    }

    public boolean esCartaValida(carta otraCarta) {
        if (this.color.equals("black")) {
            return true; // Los comodines siempre se pueden jugar
        }

        return this.color.equals(otraCarta.getColor())
                || this.numeroCarta == otraCarta.getNumeroCarta()
                || this.tipo.equals(otraCarta.getTipo());
    }

    @Override
    public String toString() {
        if (tipo.equals("normal")) {
            return "Carta " + color + " " + numeroCarta;
        } else {
            return "Carta " + color + " " + tipo;
        }
    }

    public int getNumeroCarta() {
        return numeroCarta;
    }

    public void setNumeroCarta(int numeroCarta) {
        this.numeroCarta = numeroCarta;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipo() {
        return tipo;
    }
}