package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        Node origin = super.getInputData().getOrigin();
        Node destination = super.getInputData().getDestination();
        Label labelOrigine ; 
        Label labelDestination ; 

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
