package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label {
    private Node sommet_courant;    // sommet associé à ce label (sommet ou numéro de sommet)
    private boolean marque;         // booléen, vrai lorsque le coût min de ce sommet est 
                            // définitivement connu par l'algorithme
    private float cout_realise;     // valeur courante du plus court chemin depuis l'origine vers 
                            // le sommet
    private Arc pere;               // sommet précédent sur le chemin correspondant au plus court 
                            // chemin courant. Afin de reconstruire le chemin à la fin de 
                            // l'algorithme, mieux vaut stocker l'arc plutôt que seulement 
                            // le père.

    Node get_sommet_courant() {
        return this.sommet_courant;
    }

    boolean get_marque() {
        return this.marque;
    }

    float get_cout_realise () {
        return this.cout_realise;
    }

    Arc pere() {
        return this.pere;
    }

    public Label(Node sommet_courant, boolean marque, float cout_realise, Arc pere) {
        this.sommet_courant = sommet_courant;
        this.marque = marque;
        this.cout_realise = cout_realise;
        this.pere = pere;
    }

    public Label(Node sommet_courant, boolean marque, float cout_realise) {
        this.sommet_courant = sommet_courant;
        this.marque = marque;
        this.cout_realise = cout_realise;
    }
}