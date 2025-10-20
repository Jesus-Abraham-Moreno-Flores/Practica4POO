package org.example.practica4poo;

import java.util.ArrayList;
import java.util.Scanner;

public class UNOControl {
    private ArrayList<Jugador> jugadores;
    private baraja mazo;
    private carta cartaEnJuego;
    private int turnoActual;
    private boolean sentidoHorario;
    private Scanner scanner;

    public UNOControl(ArrayList<Jugador> jugadores, baraja mazo) {
        this.jugadores = jugadores;
        this.mazo = mazo;
        this.cartaEnJuego = mazo.robarCarta();
        this.turnoActual = 0;
        this.sentidoHorario = true;
        this.scanner = new Scanner(System.in);

        // Si la carta inicial es un comodín o +4, elegir color inicial
        String tipoIni = (cartaEnJuego.getTipo() == null) ? "" : cartaEnJuego.getTipo().toLowerCase().trim();
        String colorIni = (cartaEnJuego.getColor() == null) ? "" : cartaEnJuego.getColor().toLowerCase().trim();
        if (colorIni.equals("black") || tipoIni.contains("comod") || tipoIni.equals("+4")) {
            System.out.println("La carta inicial es un " + cartaEnJuego.getTipo() + ". Debes elegir un color para comenzar.");
            String elegido = elegirColorValido();
            cartaEnJuego.setColor(elegido);
            System.out.println("El color inicial es: " + elegido);
        }
    }

    public void iniciarJuego() {
        boolean juegoActivo = true;

        System.out.println("¡Bienvenido a UNO!");
        System.out.println("Carta inicial: " + cartaEnJuego);

        while (juegoActivo) {
            Jugador jugadorActual = jugadores.get(turnoActual);
            juegoActivo = jugarTurno(jugadorActual);

            if (juegoActivo) {
                avanzarTurno();
            }
        }

        System.out.println("\n¡El juego ha terminado!");
    }

    public boolean jugarTurno(Jugador jugador) {
        System.out.println("\n=======================================");
        System.out.println("Turno de: " + jugador.getNombre());
        System.out.println("Carta en juego: " + cartaEnJuego);

        boolean turnoValido = false;
        carta cartaJugada = null;

        while (!turnoValido) {
            jugador.mostrarMano();
            System.out.print("Elige el número de carta a jugar (0 para robar): ");
            int eleccion;
            try {
                eleccion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(" Opción inválida, intenta de nuevo.");
                continue;
            }

            if (eleccion == 0) {
                carta cartaRobada = mazo.robarCarta();
                jugador.tomarCarta(cartaRobada);
                System.out.println(jugador.getNombre() + " roba una carta: " + cartaRobada);
                turnoValido = true; // termina turno
            } else if (eleccion > 0 && eleccion <= jugador.getMano().size()) {
                carta seleccionada = jugador.getMano().get(eleccion - 1);
                if (esCartaValida(seleccionada, cartaEnJuego)) {
                    cartaJugada = jugador.getMano().remove(eleccion - 1);
                    turnoValido = true;
                } else {
                    System.out.println("No se puede jugar esa carta. Debe coincidir en color, número o tipo.");
                    System.out.println("Carta actual en juego: " + cartaEnJuego);
                    System.out.println("\nTu mano actual:");
                }

            } else {
                System.out.println(" Opción inválida, intenta de nuevo.");
            }
        }

        if (cartaJugada != null) {
            // Colocamos la carta en juego primero
            cartaEnJuego = cartaJugada;
            System.out.println(jugador.getNombre() + " jugó " + cartaJugada);

            // Aplicar efecto (si es comodín o +4, se pedirá color dentro)
            aplicarEfecto(cartaJugada);

            if (jugador.getMano().isEmpty()) {
                System.out.println(jugador.getNombre() + " ha ganado el juego!");
                return false;
            }
        }

        return true;
    }

    // Verifica si la carta seleccionada puede jugarse
    private boolean esCartaValida(carta seleccionada, carta enJuego) {
        if (seleccionada == null || enJuego == null) return false;

        String tipoSel = (seleccionada.getTipo() == null) ? "" : seleccionada.getTipo().toLowerCase().trim();
        String tipoEn = (enJuego.getTipo() == null) ? "" : enJuego.getTipo().toLowerCase().trim();
        String colorSel = (seleccionada.getColor() == null) ? "" : seleccionada.getColor().toLowerCase().trim();
        String colorEn = (enJuego.getColor() == null) ? "" : enJuego.getColor().toLowerCase().trim();

        // 1) Si la carta es negra (comodín / +4) siempre se puede jugar (detectado por color)
        if (colorSel.equals("black")) return true;

        // 2) Si el tipo indica comodín (por si tu constructor usa "Comodin" o "Comidin")
        if (tipoSel.contains("comod")) return true;
        if (tipoSel.equals("+4")) return true;

        // 3) Si el color es el mismo
        if (!colorSel.isEmpty() && colorSel.equals(colorEn)) return true;

        // 4) Si el número es el mismo y se utiliza el -1 para contarlo como si no tuviese numero
        if (seleccionada.getNumeroCarta() != -1 && enJuego.getNumeroCarta() != -1
                && seleccionada.getNumeroCarta() == enJuego.getNumeroCarta()) return true;

        // 5) Si ambos son del mismo tipo especial como el "+2", "bloquear", "reversa"
        if (!tipoSel.equals("normal") && !tipoSel.isEmpty() && tipoSel.equals(tipoEn)) return true;

        return false;
    }

    private void aplicarEfecto(carta carta) {
        // Se detectan primero los comodines
        String tipo = (carta.getTipo() == null) ? "" : carta.getTipo().toLowerCase().trim();
        String color = (carta.getColor() == null) ? "" : carta.getColor().toLowerCase().trim();

        // Si es comodín negro (o tipo contiene "comod"), pedir color (comodin simple)
        if (color.equals("black") || tipo.contains("comod")) {
            String nuevoColor = elegirColorValido();
            // Actualizamos la carta en mesa con el color elegido
            cartaEnJuego.setColor(nuevoColor);
            System.out.println("El nuevo color es: " + nuevoColor);
            // Si además es +4 por tipo, aplicamos efecto de robar 4
            if (tipo.equals("+4")) {
                Jugador siguiente = siguienteJugador();
                for (int i = 0; i < 4; i++) siguiente.tomarCarta(mazo.robarCarta());
                System.out.println("El siguiente jugador roba 4 cartas.");
                avanzarTurno();
            }
            return;
        }

        // No es comodín negro: tratar tipos normales
        switch (tipo) {
            case "+2":
                Jugador siguiente2 = siguienteJugador();
                siguiente2.tomarCarta(mazo.robarCarta());
                siguiente2.tomarCarta(mazo.robarCarta());
                System.out.println("El siguiente jugador roba 2 cartas.");
                avanzarTurno();
                break;

            case "bloquear":
            case "bloqueo":
                System.out.println("El siguiente jugador ha sido bloqueado.");
                avanzarTurno();
                break;

            case "reversa":
            case "revertir":
                sentidoHorario = !sentidoHorario;
                System.out.println("Se invierte el sentido del juego.");
                break;

            default:
                // cartas normales no tienen efecto adicional
                break;
        }
    }

    // Pide color hasta que sea válido
    private String elegirColorValido() {
        while (true) {
            System.out.print("Elige un color válido (red, blue, green, yellow): ");
            String input = scanner.nextLine().toLowerCase().trim();
            if (input.equals("red") || input.equals("blue") || input.equals("green") || input.equals("yellow")) {
                return input;
            }
            System.out.println("Color inválido. Solo puedes elegir: red, blue, green o yellow.");
        }
    }

    private Jugador siguienteJugador() {
        int siguiente;
        if (sentidoHorario) {
            siguiente = (turnoActual + 1) % jugadores.size();
        } else {
            siguiente = (turnoActual - 1 + jugadores.size()) % jugadores.size();
        }
        return jugadores.get(siguiente);
    }

    private void avanzarTurno() {
        if (sentidoHorario) {
            turnoActual = (turnoActual + 1) % jugadores.size();
        } else {
            turnoActual = (turnoActual - 1 + jugadores.size()) % jugadores.size();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        baraja mazo = new baraja();
        mazo.crearBaraja();
        mazo.barajar();

        System.out.println("=================== UNO ===================");
        System.out.print("¿Cuántos jugadores participarán? (mínimo 2): ");
        int numJugadores = Integer.parseInt(scanner.nextLine().trim());

        ArrayList<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= numJugadores; i++) {
            System.out.print("Nombre del jugador " + i + ": ");
            String nombre = scanner.nextLine();
            jugadores.add(new Jugador(nombre));
        }

        for (int i = 0; i < 7; i++) {
            jugadores.forEach(j -> j.tomarCarta(mazo.robarCarta()));
        }

        UNOControl juego = new UNOControl(jugadores, mazo);
        juego.iniciarJuego();
    }
}
