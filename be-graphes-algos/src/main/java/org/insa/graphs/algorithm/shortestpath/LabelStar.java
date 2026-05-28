package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    float coutSupp ;

    public LabelStar(Node sommet_courant, boolean marque, float cout_realise, Arc pere, LabelStar dest){
        super(sommet_courant, marque, cout_realise, pere);
        this.cout_realise = cout_realise ;
        if (dest != null){
            this.coutSupp = (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
        }
        else {
            this.coutSupp = 0;
        }
        
    }

    public LabelStar(Node sommet_courant, boolean marque, float cout_realise, LabelStar dest){
        super(sommet_courant, marque, cout_realise);
        this.cout_realise = cout_realise ;
        if (dest != null){
            this.coutSupp = (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
        }
        else {
            this.coutSupp = 0;
        }    }

    public LabelStar(Node sommet_courant, boolean marque, LabelStar dest){
        super(sommet_courant, marque);
        this.cout_realise = Float.POSITIVE_INFINITY;
        if (dest != null){
            this.coutSupp = (float) Point.distance(sommet_courant.getPoint(), dest.sommet_courant.getPoint());
        }
        else {
            this.coutSupp = 0;
        }
    }

    @Override

    public float getTotalCost(){
        return this.coutSupp + this.cout_realise;
    }
}
