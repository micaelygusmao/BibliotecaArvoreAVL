package lib.model;

import lib.interfaces.IArvoreBinaria;

import java.util.Comparator;

public class ArvoreAVL<T extends Comparable> extends ArvoreBinaria<T> {
    public ArvoreAVL(Comparator<T> comp) {
        super(comp);
    }

    private No<T> rotacaoEsquerda(No<T> r){
        No<T> f = r.getFilhoDireita();
        r.setFilhoDireita(r.getFilhoEsquerda());
        f.setFilhoEsquerda(r);

        return r;
    }

    private No<T> rotacaoDireita(No<T> r){
        No<T> f = r.getFilhoEsquerda();
        r.setFilhoEsquerda(r.getFilhoDireita());
        f.setFilhoDireita(r);

        return r;
    }

    private No<T> rotacaoEsquerdaDireita(No<T> r){
        r.setFilhoDireita(rotacaoEsquerda(r.getFilhoEsquerda()));

        return rotacaoDireita(r);
    }

    private No<T> rotacaoDiretaEsquerda(No<T> r){
        r.setFilhoDireita(rotacaoDireita(r.getFilhoDireita()));

        return rotacaoEsquerda(r);
    }

    @Override
    public void adicionar(T novoValor) {
        super.adicionar(novoValor);

        if(raiz.fatorBalanceamento() > 1){
            if(raiz.getFilhoDireita().fatorBalanceamento() > 0){
                raiz = this.rotacaoEsquerda(raiz);
            } else {
                raiz = this.rotacaoDiretaEsquerda(raiz);
            }
        } else if (raiz.fatorBalanceamento() <- 1){
            if(raiz.getFilhoEsquerda().fatorBalanceamento() < 0){
                raiz = this.rotacaoDireita(raiz);
            } else {
                raiz = this.rotacaoEsquerdaDireita(raiz);
            }
        }
    }

    @Override
    public T remover(T valor) {
        this.raiz = removerNo(this.raiz, valor);

        return valor;
    }

    private No<T> minValueNode(No<T> node) {
        No<T> atual = node;

        // Loop para encontrar a folha mais à esquerda
        while (atual.getFilhoEsquerda() != null) {
            atual = atual.getFilhoEsquerda();
        }
        return atual;
    }

    private No<T> balancear(No<T> node) {
        int balance = node.fatorBalanceamento();

        // Caso de rotação simples à esquerda
        if (balance > 1 && node.getFilhoDireita().fatorBalanceamento() >= 0) {
            return rotacaoEsquerda(node);
        }

        // Caso de rotação dupla à esquerda
        if (balance > 1 && node.getFilhoDireita().fatorBalanceamento() < 0) {
            node.setFilhoDireita(rotacaoDireita(node.getFilhoDireita()));
            return rotacaoEsquerda(node);
        }

        // Caso de rotação simples à direita
        if (balance < -1 && node.getFilhoEsquerda().fatorBalanceamento() <= 0) {
            return rotacaoDireita(node);
        }

        // Caso de rotação dupla à direita
        if (balance < -1 && node.getFilhoEsquerda().fatorBalanceamento() > 0) {
            node.setFilhoEsquerda(rotacaoEsquerda(node.getFilhoEsquerda()));
            return rotacaoDireita(node);
        }

        return node;
    }

    private No<T> removerNo(No<T> root, T valor) {
        if (root == null) {
            return root;
        }

        // Realizar remoção normal de BST
        if (valor.compareTo(root.getValor()) < 0) {
            root.setFilhoEsquerda(removerNo(root.getFilhoEsquerda(), valor));
        } else if (valor.compareTo(root.getValor()) > 0) {
            root.setFilhoDireita(removerNo(root.getFilhoDireita(), valor));
        } else {
            // Nó com um filho ou nenhum filho
            if ((root.getFilhoEsquerda() == null) || (root.getFilhoDireita() == null)) {
                No<T> temp = (root.getFilhoEsquerda() != null) ? root.getFilhoDireita() : root.getFilhoDireita();

                // Sem filhos
                if (temp == null) {
                    temp = root;
                    root = null;
                } else { // Um filho
                    root = temp;
                }
            } else {
                // Nó com dois filhos: obter sucessor in-order (menor na subárvore direita)
                No<T> temp = minValueNode(root.getFilhoDireita());

                // Copiar o valor do sucessor in-order para este nó
                root.setValor(temp.getValor());

                // Remover o sucessor in-order
                root.setFilhoDireita(removerNo(root.getFilhoDireita(), temp.getValor()));
            }
        }

        // Se a árvore tiver apenas um nó então retornar
        if (root == null) {
            return root;
        }

        // Balancear o nó
        return balancear(root);
    }
}
