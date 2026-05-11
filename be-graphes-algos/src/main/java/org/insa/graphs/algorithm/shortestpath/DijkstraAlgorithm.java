package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        // TODO: implement the Dijkstra algorithm

        Node origine = super.getInputData().getOrigin();
        Node destination = super.getInputData().getDestination();
        
        Label origineLabel = new Label(origine, false, 0);
        Label destinationLabel = new Label(destination, false, -1);
        Label currentLabel = new Label(origine, false, 0);

        ArrayList<Label> tableau_de_suivi = new ArrayList<>();

        while (currentLabel != destinationLabel) {
            for (Arc a : currentLabel.get_sommet_courant().getSuccessors()) {
                // Label b = new Label(a.getDestination(), false, currentLabel.get_cout_realise() + a.getLength(), a);
                int indexA = -1;
                for (int i = 0; i < tableau_de_suivi.size(); i++) {
                    if (tableau_de_suivi.get(i).get_sommet_courant() == a.getDestination()) {
                        indexA = i;
                        break;
                    }
                }
                if (indexA != -1) { // node already reached
                    // update the new potential shortest path for this node
                    float new_cost = Math.min(tableau_de_suivi.get(indexA).get_cout_realise(), 
                            currentLabel.get_cout_realise() + a.getLength());
                    tableau_de_suivi.get(indexA).set_cout_realise(new_cost);
                } else {
                    tableau_de_suivi.add(new Label(a.getDestination(), false, currentLabel.get_cout_realise() + a.getLength(), a))
                }
            }
            //find min
        }
        Label labelOrigine; 
        Label labelDestination; 

        Label current ;
        labelOrigine = new Label(origin, false, 0, null);
        current = labelOrigine ;  

        labelDestination = new Label(destination, false, -1, null);

        List<Label> labels = new ArrayList<>();

        while (labelDestination.marque == false) {
            
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
