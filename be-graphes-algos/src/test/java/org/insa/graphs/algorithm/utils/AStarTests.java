package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Graph;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

public class AStarTests {

    private static Graph graphInsa;

    @BeforeClass
    public static void initAll() throws Exception {
        final String mapName = "/home/charles/Documents/BE-GRAPHES/cartes/bretagne.mapgr";

        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {
            graphInsa = reader.read();
        }
    }

    @Test
    public void testAStarEmptyGraph() {
        Node origin = graphInsa.getNodes().get(42);
        Node destination = graphInsa.getNodes().get(42);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        AStarAlgorithm aStar = new AStarAlgorithm(data);
        ShortestPathSolution solution = aStar.run();

        assertEquals(AbstractSolution.Status.OPTIMAL, solution.getStatus());
        assertEquals("Le chemin nul doit avoir un coût de 0", 0.0, solution.getPath().getLength(), 1e-4);
        assertTrue("Le chemin contenant un seul noeud doit être valide", solution.getPath().isValid());
    }

    @Test
    public void testAStarSingleNode() {
        Node origin = graphInsa.getNodes().get(109);
        Node destination = graphInsa.getNodes().get(89);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        AStarAlgorithm aStar = new AStarAlgorithm(data);
        ShortestPathSolution solution = aStar.run();

        assertEquals(AbstractSolution.Status.OPTIMAL, solution.getStatus());
        assertTrue("Le chemin calculé par A* doit être valide", solution.getPath().isValid());
        assertTrue("Le chemin doit avoir une longueur strictement positive", solution.getPath().getLength() > 0);
    }

    @Test
    public void testAStarTwoNodes() {
        Node origin = graphInsa.getNodes().get(109);
        Node destination = graphInsa.getNodes().get(89);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        AStarAlgorithm aStar = new AStarAlgorithm(data);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);

        ShortestPathSolution solutionAStar = aStar.run();
        ShortestPathSolution solutionDijkstra = dijkstra.run();

        assertEquals(AbstractSolution.Status.OPTIMAL, solutionAStar.getStatus());
        assertTrue("Le chemin calculé par A* doit être valide", solutionAStar.getPath().isValid());
        assertEquals("Les coûts d'A* et de Dijkstra doivent être identiques",
                     solutionDijkstra.getPath().getLength(),
                     solutionAStar.getPath().getLength(),
                     1e-4);
    }

    @Test
    public void testAStarNoPath() {
        Node origin = graphInsa.getNodes().get(371752);
        Node destination = graphInsa.getNodes().get(116033);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        AStarAlgorithm aStar = new AStarAlgorithm(data);
        ShortestPathSolution solution = aStar.run();

        assertEquals("Aucun chemin ne doit être trouvé", AbstractSolution.Status.INFEASIBLE, solution.getStatus());
    }

    @Test
    public void testAStarHeuristicAdmissible() {
        Node origin = graphInsa.getNodes().get(109);
        Node destination = graphInsa.getNodes().get(89);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
        ShortestPathData data = new ShortestPathData(graphInsa, origin, destination, arcInspector);

        AStarAlgorithm aStar = new AStarAlgorithm(data);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);

        ShortestPathSolution solutionAStar = aStar.run();
        ShortestPathSolution solutionDijkstra = dijkstra.run();

        assertEquals(AbstractSolution.Status.OPTIMAL, solutionAStar.getStatus());
        assertEquals("A* ne doit pas sur-estimer le coût du plus court chemin",
                     solutionDijkstra.getPath().getLength(),
                     solutionAStar.getPath().getLength(),
                     1e-4);
    }
    @Test
    public void testInegaliteTriangulaire() {
        // on prend trois noeuds hard codes sur la carte bretagne
        // A et B sont les points de depart et d arrivee, C est un point intermediaire
        Node nodeA = graphInsa.getNodes().get(109);
        Node nodeB = graphInsa.getNodes().get(89);
        Node nodeC = graphInsa.getNodes().get(500);

        ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);

        // on calcule les trois chemins dont on a besoin pour l inegalite triangulaire
        ShortestPathData dataAB = new ShortestPathData(graphInsa, nodeA, nodeB, arcInspector);
        ShortestPathData dataAC = new ShortestPathData(graphInsa, nodeA, nodeC, arcInspector);
        ShortestPathData dataCB = new ShortestPathData(graphInsa, nodeC, nodeB, arcInspector);

        AStarAlgorithm aStarAB = new AStarAlgorithm(dataAB);
        AStarAlgorithm aStarAC = new AStarAlgorithm(dataAC);
        AStarAlgorithm aStarCB = new AStarAlgorithm(dataCB);

        ShortestPathSolution solAB = aStarAB.run();
        ShortestPathSolution solAC = aStarAC.run();
        ShortestPathSolution solCB = aStarCB.run();

        // si un des chemins n existe pas le test n a pas de sens donc on le saute
        Assume.assumeTrue(solAB.getStatus() == AbstractSolution.Status.OPTIMAL);
        Assume.assumeTrue(solAC.getStatus() == AbstractSolution.Status.OPTIMAL);
        Assume.assumeTrue(solCB.getStatus() == AbstractSolution.Status.OPTIMAL);

        double distAB = solAB.getPath().getLength();
        double distAC = solAC.getPath().getLength();
        double distCB = solCB.getPath().getLength();

        // le chemin optimal de A vers B ne peut pas etre plus long que le detour par C
        // si c est le cas ca veut dire que notre algo a rate quelque chose ou renvoie une valeur absurde
        assertTrue(
            "l inegalite triangulaire est violee : dist(A,B) devrait etre <= dist(A,C) + dist(C,B)",
            distAB <= distAC + distCB + 1e-4
        );
    }
}
