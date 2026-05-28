package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Math;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.shortestpath.LabelStar;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.utils.BinaryHeap;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
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

    Map<Node, LabelStar> labels = new HashMap<>();
    BinaryHeap<Label> priorityQueue = new BinaryHeap<>();

    LabelStar destLabel = new LabelStar(data.getDestination(), false, null);
    labels.put(data.getDestination(), destLabel);

    // Initialisation de la source
    LabelStar source = new LabelStar(data.getOrigin(), false, 0, destLabel);
    labels.put(data.getOrigin(), source);
    priorityQueue.insert(source); 
    notifyOriginProcessed(source.get_sommet_courant());

    while (!priorityQueue.isEmpty()) {

        Label currentLabel = priorityQueue.deleteMin();
        Node currentNode = currentLabel.get_sommet_courant();
        notifyNodeReached(currentNode);

        if (currentLabel.get_marque()) { 
            continue;
        }
        currentLabel.setMarque(true);
        notifyNodeMarked(currentNode);

        // permet de skip les doublons (en cas de mise a jour du cout d'un noeud on a plusieurs fois
        // la référence à un même label. la première à sauter et celle de cout le + faible, 
        // quand on croise l'une des autres on passe à la boucle suivante

        if (currentNode.equals(data.getDestination())) { // Fin si on a atteint la destination
            break;
        }

        for (Arc arc : currentNode.getSuccessors()) {
            if (!data.isAllowed(arc)) continue; // vérifie si le mode de transport est autorisé

            Node successor = arc.getDestination();
            LabelStar destiLabel = labels.get(successor);

            if (destiLabel == null) {
                destiLabel = new LabelStar(successor, false, Float.POSITIVE_INFINITY, destLabel);
                labels.put(successor, destiLabel);
            }

            if (!destiLabel.get_marque()) {
                float oldDistance = destiLabel.get_cout_realise();
                float newDistance = currentLabel.get_cout_realise() + (float)data.getCost(arc);

                if (newDistance < oldDistance) {
                    destiLabel.setCoutRealise(newDistance);
                    destiLabel.setPere(arc);
                    priorityQueue.insert(destiLabel);
                }
            }
        }
    }

    // On vérifie si l'algo a bien rencontré la destination
    LabelStar destiLabel= labels.get(data.getDestination());

    if (data.getOrigin().equals(data.getDestination())) {
        // Cas Spécial : Origine et Destination sont le même sommet (Chemin de longueur 0)
        solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, new Path(data.getGraph(), new ArrayList<Arc>()));
    }

    else if (destiLabel== null ||destiLabel.get_pere() == null) {
        // Cas 1 : pas de chemin
        solution = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
    } 
    
    else {
        // Cas 2 : on a trouvé un chemin, on le reconstruit
        ArrayList<Arc> arcs = new ArrayList<>();
        Arc currentArc = destLabel.get_pere();
        
        while (currentArc != null) {
            arcs.add(currentArc);
            // On remonte au label du sommet d'origine de l'arc
            LabelStar prevLabel = labels.get(currentArc.getOrigin());
            currentArc = prevLabel.get_pere();
        }
        
        // Le chemin est construit à l'envers, il faut le retourner
        Collections.reverse(arcs);

        Graph graph = data.getGraph();
        Path finalPath = new Path(graph, arcs);
        solution = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, finalPath);
        notifyDestinationReached(destLabel.get_sommet_courant());
    }
        
        return solution;
    }

}
