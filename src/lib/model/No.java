package lib.model;

public class No<T> {
    private T valor;
    private No<T> filhoDireita;
    private No<T> filhoEsquerda;


    public No(T valor){
        this.valor = valor;
        this.filhoDireita = null;
        this.filhoEsquerda = null;
    }

    @Override
    public String toString(){
        return "[" + valor.toString() + "] \n";
    }

    /**
     * @return the valor
     */
    public T getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * @return the filhoDireita
     */
    public No<T> getFilhoDireita() {
        return filhoDireita;
    }

    /**
     * @param filhoDireita the filhoDireita to set
     */
    public void setFilhoDireita(No<T> filhoDireita) {
        this.filhoDireita = filhoDireita;
    }

    /**
     * @return the filhoEsquerda
     */
    public No<T> getFilhoEsquerda() {
        return filhoEsquerda;
    }

    /**
     * @param filhoEsquerda the filhoEsquerda to set
     */
    public void setFilhoEsquerda(No<T> filhoEsquerda) {
        this.filhoEsquerda = filhoEsquerda;
    }

    /**
     * Método para verificar se o nó é uma folha
     * @return caso tenha filhos a esquerda e a direita o retorno é false, se não tiver o retorno é true.
     */
     boolean IsFolha(){
        if(!this.TemFilhoAEsquerda() && !this.TemFilhoADireita()) return true;

        return false;
    }

    /**
     * Método para verificar se o nó tem filho a esquerda
     * @return caso tenha filhos a esquerda o retorno é true, se não tiver o retorno é false.
     */
    boolean TemFilhoAEsquerda(){
        if(this.getFilhoEsquerda() != null) return true;

        return false;
    }

    /**
     * Método para verificar se o nó tem filho a direita
     * @return caso tenha filhos a direta o retorno é true, se não tiver o retorno é false.
     */
    boolean TemFilhoADireita(){
        if(this.getFilhoDireita() != null) return true;

        return false;
    }

    /**
     * Método para verificar se o nó tem apenas um filho
     * @return caso tenha um filho o retorno é true, se não tiver o retorno é false.
     */
    boolean TemApenasUmFilho(){
        if((this.TemFilhoAEsquerda() && !this.TemFilhoADireita()) ||
                (this.TemFilhoADireita() && !this.TemFilhoAEsquerda())){
            return true;
        }

        return false;
    }

    public int obterAltura(){
        return obterAltura(this);
    }

    private int obterAltura(No<T> r){
        if(r==null){
            return -1;
        } else {
            int hd = obterAltura(r.getFilhoDireita());
            int he = obterAltura(r.getFilhoEsquerda());

            if(hd > he){
                return hd+1;
            } else {
                return he+1;
            }
        }
    }
    public int fatorBalanceamento(){
        return obterAltura(this.filhoDireita) - obterAltura(this.filhoEsquerda);
    }
}
