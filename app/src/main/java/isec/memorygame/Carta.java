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

    public void setDescoberta(boolean descoberta) {
        this.descoberta = descoberta;
    }



}
