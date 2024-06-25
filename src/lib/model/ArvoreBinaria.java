package lib.model;

import lib.interfaces.IArvoreBinaria;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArvoreBinaria<T> implements IArvoreBinaria<T> {
    protected No<T> raiz = null;
    protected Comparator<T> comparador;

    public ArvoreBinaria(Comparator<T> comp) {
        comparador = comp;
    }

    @Override
    public void adicionar(T novoValor) {
        No<T> novoNo = new No<T>(novoValor);

        if (this.raiz == null) {
            this.raiz = novoNo;
        } else {
            setPosicaoENovoValor(this.raiz, novoValor);
        }
    }

    public void setPosicaoENovoValor(No<T> no, T novoValor){
        if(comparador.compare(novoValor, no.getValor()) < 0){
            if(no.getFilhoEsquerda() == null){
                no.setFilhoEsquerda(new No<>(novoValor));
            } else {
                setPosicaoENovoValor(no.getFilhoEsquerda(), novoValor);
            }
        } else {
            if(no.getFilhoDireita() == null){
                no.setFilhoDireita(new No<>(novoValor));
            } else {
                setPosicaoENovoValor(no.getFilhoDireita(), novoValor);
            }
        }
    }

    @Override
    public T pesquisar(T valor) {
        No<T> no = GetNoByValor(this.raiz, valor);

        if(no == null) {
            System.out.println("ERROR: Não foi encontrado o nó escolhido.");
            return null;
        }

        return no.getValor();
    }

    @Override
    public T pesquisar(T valor, Comparator comparador) {
        No<T> no = GetNoByValorComparador(this.raiz, valor, comparador);

        if(no == null) {
            System.out.println("ERROR: Não foi encontrado o nó escolhido.");
            return null;
        }

        return no.getValor();
    }

    @Override
    public T remover(T valor) {
        No<T> noParaRemover = GetNoByValor(this.raiz, valor);

        if(noParaRemover == null){
            System.out.println("ERROR: Não foi encontrado o nó para remover.");
        } else {
            if(noParaRemover.IsFolha()){
                //o nó que tinha ele como filho passa a apontar para null
                SetAlteracaoPai(noParaRemover, null);
            } else {
                if(noParaRemover.TemApenasUmFilho()){
                    //o nó que tinha ele como filho passar ter o filho dele como filho
                    if(noParaRemover.TemFilhoADireita()) {
                        SetAlteracaoPai(noParaRemover, noParaRemover.getFilhoDireita());
                        return valor;
                    }

                    if(noParaRemover.TemFilhoAEsquerda()) {
                        SetAlteracaoPai(noParaRemover, noParaRemover.getFilhoEsquerda());
                        return valor;
                    }

                    return valor;
                } else {
                    No<T> maiorNo = GetMaiorValorSubArvoreEsquerda(noParaRemover.getFilhoEsquerda());
                    //o pai do maior no vai passar a apontar para null
                    SetAlteracaoPai(maiorNo, null);

                    //o pai do nó para remover vai apontar para o maior no da esquerda
                    SetAlteracaoPai(noParaRemover, maiorNo);

                    maiorNo.setFilhoDireita(noParaRemover.getFilhoDireita());

                    if(!maiorNo.TemFilhoAEsquerda()) {
                        maiorNo.setFilhoEsquerda(noParaRemover.getFilhoEsquerda());
                    }
                }
            }
        }

        return null;
    }

    /**
     * Método para alterar o valor do nó filho de um nó pai
     * @param noParaRemover - nó que será removido, cujo valor será verificado.
     * @param novoFilho - nó que será utilizado para ser o novo filho do nó pai, pode ser null.
     */
    public void SetAlteracaoPai(No<T> noParaRemover, No<T> novoFilho){
        No<T> pai = GetNoPaiByFilho(this.raiz, noParaRemover.getValor());

        if(pai.TemFilhoAEsquerda() && IsNoDaBusca(pai.getFilhoEsquerda(), noParaRemover.getValor(), null)){
            pai.setFilhoEsquerda(novoFilho);
        }

        if(pai.TemFilhoADireita() && IsNoDaBusca(pai.getFilhoDireita(), noParaRemover.getValor(), null)){
            pai.setFilhoDireita(novoFilho);
        }
    }

    /**
     * Método para pegar o nó pai a partir de um nó filho
     * @param no - nó cujo valor será verificado.
     * @param valorFilho - valor do filho que está sendo usadno para buscar o pai.
     * @return o nó pai.
     */
    private No<T> GetNoPaiByFilho(No<T> no, T valorFilho) {
        No<T> pai = null;

        if (no == null) { return null; }

        if(!no.IsFolha()){
            if(!IsValorNoMenor(no, valorFilho)){
                if(no.TemFilhoAEsquerda()){
                    if(IsNoDaBusca(no.getFilhoEsquerda(), valorFilho, null)){
                        return no;
                    } else {
                        pai = GetNoPaiByFilho(no.getFilhoEsquerda(), valorFilho);
                    }
                }
            } else {
                if(no.TemFilhoAEsquerda()){
                    if(IsNoDaBusca(no.getFilhoDireita(), valorFilho, null)){
                        return no;
                    } else {
                        pai = GetNoPaiByFilho(no.getFilhoDireita(), valorFilho);
                    }
                }
            }
        }

        return pai;
    }

    /**
     * Método para achar o nó que tem o valor passado como parâmetro
     * @param no - nó cujo valor será verificado.
     * @param valor - valor que será utilizado para verificar o nó.
     * @return o nó que tem o valor passado por parâmetro.
     */
    public No<T> GetNoByValor(No<T> no, T valor){
        if(IsNoDaBusca(no, valor, null)){ return no;  }

        if(no.TemFilhoADireita() && !IsValorNoMaior(no, valor)){ return GetNoByValor(no.getFilhoDireita(), valor); }

        if(no.TemFilhoAEsquerda() && !IsValorNoMenor(no, valor)){  return GetNoByValor(no.getFilhoEsquerda(), valor); }

        return null;
    }

    /**
     * Método para fazer uma busca no nó utilizando um comparador genérico
     * @param no - nó cujo valor será verificado.
     * @param comparador - comparador que será utilizado.
     * @return o nó que será buscado, pode ser null caso não ache o nó.
     */
    private No<T> GetNoByValorComparador(No<T> no, T valor, Comparator comparador){
        if(IsNoDaBusca(no, valor, comparador)){ return no;  }

        No<T> noBusca = null;

        noBusca = GetNoByValorComparador(no.getFilhoDireita(), valor, comparador);

        if(noBusca == null){
            noBusca = GetNoByValorComparador(no.getFilhoEsquerda(), valor, comparador);
        }

        return noBusca;
    }

    /**
     * Método para verificar se o nó tem um valor maior que o valor passado como parâmetro
     * @param no - nó cujo valor será verificado.
     * @param valor - valor que será utilizado para verificar o nó.
     * @return caso o valor do nó seja maior o retorno é false, se não for retorno é true.
     */
    private boolean IsValorNoMaior(No<T> no, T valor){
        if(comparador.compare(no.getValor(), valor) > 0) { return true; }

        return false;
    }

    /**
     * Método para verificar se o nó tem um valor menor que o valor passado como parâmetro
     * @param no - nó cujo valor será verificado.
     * @param valor - valor que será utilizado para verificar o nó.
     * @return caso o valor do nó seja menor o retorno é false, se não for retorno é true.
     */
    private boolean IsValorNoMenor(No<T> no, T valor){
        if(comparador.compare(no.getValor(), valor) < 0) { return true; }

        return false;
    }

    /**
     * Método para verificar se o nó é o nó procurado
     * @param no - será utilizado para verificar se o nó é o procurado.
     * @return caso não seja o nó procurado o retorno é false, se for retorno é true.
     */
    private boolean IsNoDaBusca(No<T> no, T valor, Comparator comparador){
        if(comparador == null){
            comparador = this.comparador;
        }

        if(no.getValor() != null){
            if(comparador.compare(valor, no.getValor()) == 0){  return true; }
            return false;
        }
        
        return false;
    }

    /**
     * Método para pegar o maior valor da subarvore da esquerda da raiz passada como parametro
     * @param raiz - será utilizada como indice de partida para procurar o maior.
     * @return caso seja encontrado, o nó é retornado. Se não, retorna null.
     */
    private No<T> GetMaiorValorSubArvoreEsquerda(No<T> raiz){
        if(raiz == null) {
            return null; // Se a raiz for nula, retorna null
        }

        if(raiz.TemFilhoADireita()){
            return GetMaiorValorSubArvoreEsquerda(raiz.getFilhoDireita());
        }

        return raiz; // Se não tiver filho à direita, retorna a própria raiz
    }

    @Override
    public int altura() {
        return AlturaRecorrente(this.raiz);
    }

    private int AlturaRecorrente(No<T> no){
        if(no == null){
            return 0;
        } else if(no.getFilhoEsquerda() == null && no.getFilhoDireita() == null){
            return 0;
        } else {
            int alturaDireita = AlturaRecorrente(no.getFilhoDireita());
            int alturaEsquerda = AlturaRecorrente(no.getFilhoEsquerda());

            if(alturaDireita > alturaEsquerda){
                return 1 + alturaDireita;
            } else {
                return 1 + alturaEsquerda;
            }
        }
    }

    @Override
    public int quantidadeNos() {
        return percorreTodaArvore(this.raiz);
    }

    /**
     * Método para percorrer e contar a quantidade de nós que tem na arvore.
     * @param no - será utilizada como indice de partida para começar a contagem.
     * @return um inteiro que representará a quantidade de nós da árvore.
     */
    private int percorreTodaArvore(No<T> no) {
        if (no == null) {
            return 0;
        }

        return 1 + percorreTodaArvore(no.getFilhoEsquerda()) + percorreTodaArvore(no.getFilhoDireita());
    }

    @Override
    public String caminharEmNivel() {
        int qtdNos = quantidadeNos();
        String retorno = "";
        List<No<T>> lista = new ArrayList<>();

        caminharEmNivelRecursivamente(this.raiz, qtdNos, lista);

        for (int i = 0; i < lista.size(); i++){
            retorno += lista.get(i).toString();
        }

        return retorno;
    }

    /**
     * Método para percorrer a árvore em nivel
     * @param no - será utilizada como indice de partida.
     * @param qtd - quantidade de nós que se espera percorrer.
     * @param lista - terá todos os nós da árvore na ordem esperada.
     * @return a lista com os nós da árvore percorridos em nivel.
     */
    public List<No<T>> caminharEmNivelRecursivamente(No<T> no, int qtd, List<No<T>> lista){
        if(lista.isEmpty()){
            lista.add(no);
        }

        if(lista.size() == qtd){
            return lista;
        } else {
            if(no.TemFilhoAEsquerda()){
                lista.add(no.getFilhoEsquerda());
            }

            if(no.TemFilhoADireita()){
                lista.add(no.getFilhoDireita());
            }

            if(no.TemFilhoAEsquerda()){
                return caminharEmNivelRecursivamente(no.getFilhoEsquerda(), qtd, lista);
            }

            if(no.TemFilhoADireita()){
                return caminharEmNivelRecursivamente(no.getFilhoDireita(), qtd, lista);
            }
        }
        return lista;
    }
    @Override
    public String caminharEmOrdem() {
        List<No<T>> lista = new ArrayList<>();
        String retorno = "";

        caminharEmOrdemRecursivamente(this.raiz, lista);

        for (int i = 0; i < lista.size(); i++){
            retorno += lista.get(i).toString();
        }

        return retorno;
    }

    /**
     * Método para percorrer a árvore em nivel
     * @param no - será utilizada como indice de partida.
     * @param lista - terá todos os nós da árvore na ordem esperada.
     * @return a lista com os nós da árvore percorridos em ordem.
     */
    private void caminharEmOrdemRecursivamente(No<T> no,  List<No<T>> lista) {
        if (no != null) {
            caminharEmOrdemRecursivamente(no.getFilhoEsquerda(), lista);
            lista.add(no);
            caminharEmOrdemRecursivamente(no.getFilhoDireita(), lista);
        }
    }
}
