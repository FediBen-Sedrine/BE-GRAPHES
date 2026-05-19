package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    float coutTotal ;

    public LabelStar(Node sommet_courant, boolean marque, float cout_realise, Arc pere, LabelStar dest){
        super(sommet_courant, marque, cout_realise, pere);
        this.coutTotal = (float) cout_realise + (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
    }

    public LabelStar(Node sommet_courant, boolean marque, float cout_realise, LabelStar dest){
        super(sommet_courant, marque, cout_realise);
        this.coutTotal = (float) cout_realise + (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
    }

    public LabelStar(Node sommet_courant, boolean marque, LabelStar dest){
        super(sommet_courant, marque);
        this.coutTotal = (float) cout_realise + (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
    }

    public float getTotalCost(LabelStar o){
        return this.coutTotal;
    }
}
