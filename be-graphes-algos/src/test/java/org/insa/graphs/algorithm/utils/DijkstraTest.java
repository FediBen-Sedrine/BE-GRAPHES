package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.Graph;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

public class DijkstraTest {

    // On stocke le graphe dans un attribut statique pour ne le charger qu'une seule fois
    private static Graph graphInsa;

    @BeforeClass
    public static void initAll() throws Exception {
        // Le chemin vers la carte
        final String mapName = "/home/charles/Documents/BE-GRAPHES/cartes/bretagne.mapgr";

        // Chargement du graphe
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {
            
            // La méthode read() permet de récupérer l'objet Graph
            graphInsa = reader.read(); 
        }
    }

    @Test
    public void testCheminExistantLongueurNonNulle() {
        // 1. Initialiser les données du test
        // Remplacer 109 et 89 par des IDs de noeuds valides et connectés sur la carte INSA
        Node origin = graphInsa.getNodes().get(109); 
        Node destination = graphInsa.getNodes().get(89);
        
        // Utilisation du filtre par défaut (généralement Distance + All roads allowed)
        // Assure-toi d'avoir importé org.insa.graphs.algorithm.ArcInspectorFactory
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        // 2. Créer les instances des algorithmes
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm bellman = new BellmanFordAlgorithm(data);

        // 3. Exécuter les algorithmes
        ShortestPathSolution solutionDijkstra = dijkstra.run();
        ShortestPathSolution solutionBellman = bellman.run();

        // 4. Vérifier la validité de la solution Dijkstra
        assertEquals(AbstractSolution.Status.OPTIMAL, solutionDijkstra.getStatus());
        assertTrue("Le chemin calculé par Dijkstra doit être valide", solutionDijkstra.getPath().isValid());

        // 5. Comparer avec l'Oracle (Bellman-Ford)
        // On compare des flottants/doubles : il FAUT renseigner un epsilon (delta) de tolérance
        assertEquals("Le coût trouvé doit être identique à Bellman-Ford", 
                     solutionBellman.getPath().getLength(), 
                     solutionDijkstra.getPath().getLength(), 
                     1e-4);
    }
    
    @Test
    public void testCheminNul() {
        // Origine et Destination identiques
        Node origin = graphInsa.getNodes().get(42); 
        Node destination = graphInsa.getNodes().get(42); 
        
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solutionDijkstra = dijkstra.run();

        // Vérifications
        assertEquals(AbstractSolution.Status.OPTIMAL, solutionDijkstra.getStatus());
        assertEquals("Le chemin nul doit avoir un coût de 0", 0.0, solutionDijkstra.getPath().getLength(), 1e-4);
        assertTrue("Le chemin contenant un seul noeud doit être valide", solutionDijkstra.getPath().isValid());
    }

    @Test
    public void testCheminInexistant() {
        // Attention : sur une petite carte routière connectée comme l'INSA, 
        // il peut être difficile de trouver deux noeuds totalement déconnectés.
        // Ce test est idéal sur des cartes spécifiques fournies (ex: carre.mapgr, insa avec un graphe non connexe).
        
        // Supposons que les noeuds 0 et 500 soient déconnectés dans le graphe utilisé
        Node origin = graphInsa.getNodes().get(371752);
        Node destination = graphInsa.getNodes().get(116033);
        
        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        ShortestPathSolution solution = dijkstra.run();

        // Le statut attendu est INFEASIBLE s'il n'y a pas de chemin
        assertEquals("Aucun chemin ne doit être trouvé", AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

}
