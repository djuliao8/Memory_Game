package isec.memorygame;


public class Carta {
    int id;
    int cartaVirada;
    int cartaPorVirar;
    String cartaViradaS;
    String cartaPorVirarS;
    boolean descoberta;
    boolean par_intruso;


    public Carta(int id,int cartaVirada, int cartaPorVirar) {
        this.id = id;
        this.cartaVirada = cartaVirada;
        this.cartaPorVirar = cartaPorVirar;
        descoberta = false;
        par_intruso = false;
    }

    public Carta(int id,String cartaVirada,String cartaPorVirar){
        this.id = id;
        this.cartaPorVirarS = cartaPorVirar;
        this.cartaViradaS = cartaVirada;
        descoberta = false;
        par_intruso = false;
    }

    public void setPar_intruso(boolean par_intruso){
        this.par_intruso = par_intruso;
    }

    public void setDescoberta(boolean descoberta) {
        this.descoberta = descoberta;
    }


}
