package isec.memorygame;


public class Carta {
    int id;
    int cartaVirada;
    int cartaPorVirar;
    boolean descoberta;


    public Carta(int id,int cartaVirada, int cartaPorVirar) {
        this.id = id;
        this.cartaVirada = cartaVirada;
        this.cartaPorVirar = cartaPorVirar;
        descoberta = false;
    }

    public int getId() {
        return id;
    }

    public int getCartaVirada() {
        return cartaVirada;
    }

    public int getCartaPorVirar() {
        return cartaPorVirar;
    }

    public void setDescoberta(boolean descoberta) {
        this.descoberta = descoberta;
    }

    public boolean isDescoberta() {
        return descoberta;
    }

}
