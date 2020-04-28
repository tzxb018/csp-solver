package csp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import csp.MyACAlgorithms;
import csp.MainStructures.MyClique;
import csp.MainStructures.MyConstraint;
import csp.MainStructures.MyExtensionConstraint;
import csp.MainStructures.MyIntensionConstraint;
import csp.MainStructures.MyProblem;
import csp.MainStructures.MyVariable;
import csp.Search.SearchAlgorithms;
import csp.Search.OrderingHeuristics.DDR;
import csp.Search.OrderingHeuristics.DEG;
import csp.TreeDecompisition.*;
import csp.Search.OrderingHeuristics.Minwidth;

/**
 * 
 * @author Tomo Bessho
 * @version HW 3, HW 4
 * @since 2/17/2020
 */

public class Setup {

    protected MyProblem myProblem;
    protected ArrayList<MyVariable> variables;
    protected ArrayList<MyVariable> current_path;
    protected ArrayList<MyVariable> copy_current_path;
    protected int[] assignments;

    protected ArrayList<MyVariable> unassignedVariables;
    protected ArrayList<MyVariable> assignedVariables;

    protected String orderingHeuristic;
    protected String orderedCurrentPathString;

    protected ArrayList<MyClique> cliques;

    /**
     * This is the constructor for all the backtrack searches
     * 
     * @param myProblem          takes in the problem data structure to get the
     *                           constraints, variables, and whether it is an
     *                           extension/intension problem. If it is an extension
     *                           problem, then this will also tell the algorithm if
     *                           the extension variables are supports or conflict
     * @param ordering_heursitic defines how the variables will be ordered
     * @throws IOException
     */
    public Setup(MyProblem myProblem, String ordering_heursitic, boolean staticOrdering, String searchType)
            throws IOException {

        this.myProblem = myProblem;
        this.variables = myProblem.getVariables();
        this.current_path = new ArrayList<MyVariable>();
        this.assignments = new int[variables.size() + 1];
        this.orderingHeuristic = ordering_heursitic;
        this.cliques = new ArrayList<MyClique>();

        // if we are using static ordering
        if (staticOrdering) {

            // printing for normal problem solving
            if (!ordering_heursitic.contains("TD")) {
                System.out.println("Search: " + searchType);
                System.out.println("variable-order-heuristic: " + ordering_heursitic);
                System.out.println("var-static-dynamic: static");
                System.out.println("value-ordering-heuristic: " + ordering_heursitic);
                System.out.println("val-static-dynamic: static");
            } else {
                // printing for stats about tree decompoisition
                System.out.println("Number of variables: " + this.variables.size());
                System.out.println("Number of edges: " + myProblem.getEdges());
                System.out.println("Density: "
                        + (2 * myProblem.getEdges() / (float) (this.variables.size() * (this.variables.size() - 1))));
            }

            // running NC before running search
            ArrayList<MyConstraint> unaryConstraints = new ArrayList<MyConstraint>();

            // finding the unary constraints
            for (MyConstraint constraint : myProblem.getConstraints()) {
                if (constraint.getScope().size() == 1) {
                    unaryConstraints.add(constraint);
                }
            }

            // executing node consistency
            if (unaryConstraints.size() > 0) {
                MyACAlgorithms ac = new MyACAlgorithms();

                // depending on which type of unary constraint it is, run NC
                if (myProblem.getExtension()) {
                    for (MyConstraint c : unaryConstraints) {
                        ac.nodeConsistencyExtension(variables, (MyExtensionConstraint) c);
                    }
                } else {
                    for (MyConstraint c : unaryConstraints) {
                        ac.nodeConsistencyIntension(variables, (MyIntensionConstraint) c);
                    }
                }
            }

            // adding into the current-path in order lexiographically
            for (int i = 0; i < variables.size(); i++) {
                current_path.add(variables.get(i));
                // System.out.println(variables.get(i).getName() + ": " +
                // variables.get(i).getNeighbors());
            }

            for (int i = 0; i < assignments.length; i++) {
                assignments[i] = -1;
            }

            // using the ordering heuristic to determine how the variables are put into
            // current-path

            switch (ordering_heursitic) {
                case ("LX"):
                    Collections.sort(current_path, MyVariable.LX_COMPARATOR);
                    break;
                case ("SOL"):
                    Collections.sort(current_path, MyVariable.SOL_COMPARATOR);
                    break;
                case ("LD"):
                    Collections.sort(current_path, MyVariable.LD_COMPARATOR);
                    break;
                case ("DEG"):
                    DEG deg = new DEG();
                    this.current_path = deg.degreeOrdering(this.current_path);
                    break;
                case ("DD"):
                    DDR ddr = new DDR();
                    this.current_path = ddr.ddrOrdering(this.current_path);
                    break;
                case ("W"):
                    Minwidth w = new Minwidth();
                    this.current_path = w.widthOrdering(this.current_path);
                    Collections.reverse(this.current_path);
                    break;
                case ("MF"):
                    Minfill mf = new Minfill();
                    this.current_path = mf.minfill(this.current_path);
                    Collections.reverse(this.current_path);
                    break;
                case ("MC"):
                    Maxcard mc = new Maxcard();
                    this.current_path = mc.maxCardinality(this.current_path);
                    break;
                case ("TD-MC"):
                    treeDecompisition(this.current_path, true);
                    break;
                case ("TD"):
                    treeDecompisition(this.current_path, false);
                    break;
            }

            // for writing the order of the varialbes to a csv file
            // csvOrder();
            this.current_path.add(0, null); // pointer starts at 1
            orderedCurrentPathString = "[";
            for (int i = 1; i < this.current_path.size() - 1; i++) {
                orderedCurrentPathString += (this.current_path.get(i).getName() + ",");
            }
            orderedCurrentPathString += (this.current_path.get(this.current_path.size() - 1)).getName() + "]";
            // System.out.println(orderedCurrentPathString);
        }
    }

    // function for tree decomposition
    public void treeDecompisition(ArrayList<MyVariable> input, boolean runMaxCard) throws IOException {

        // running minfill to triangulate the graph
        Minfill mf1 = new Minfill();
        this.current_path = mf1.minfill(this.current_path);
        System.out.println("Number of filled edges: " + mf1.getNumFilled());
        // System.out.println("PEO : " + this.current_path);

        // running max cardinality to get a better PEO
        if (runMaxCard) {
            Maxcard mc1 = new Maxcard();
            this.current_path = mc1.maxCardinality(this.current_path);
            // System.out.println("After Max Card: " + this.current_path);
        }

        // runing max clique to obtain all the maximal cliques
        MaxClique mq = new MaxClique();
        cliques = mq.getMaxClique(this.current_path);
        System.out.println("Number of Max Cliques: " + mq.getNumberOfCliques());
        System.out.println("Treewidth: " + (mq.getLargestClique()));
        // System.out.println(cliques);

        // building the joining tree
        JointTree jt = new JointTree();
        ArrayList<MyClique> c = jt.primalAcyclicity(cliques);
        System.out.println("Largest Number of Variables in Seperators: " + jt.getLargestSepartor());
        // for (MyClique cc : c) {
        // System.out.println(cc + " with " + cc.getNeighbors());
        // }

        // writing to csv
        csvTreeDecomp(this.variables.size(), myProblem.getEdges(),
                (2 * myProblem.getEdges() / (float) (this.variables.size() * (this.variables.size() - 1))),
                mf1.getNumFilled(), mq.getNumberOfCliques(), mq.getLargestClique(), jt.getLargestSepartor());
    }

    // function to write the stats for tree decompisition
    public void csvTreeDecomp(int vars, int edges, float density, int filled, int clique_num, int tree_width,
            int largest) throws IOException {
        String fileContent = myProblem.getProblemName() + "," + vars + "," + edges + "," + density + "," + filled + ","
                + clique_num + "," + tree_width + "," + largest + "\n";

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:/Users/14022/Documents/VS Code Projects/csp-solver/tree_stats.csv", true));
        writer.write(fileContent);
        writer.close();
    }

    // function to write the order of the variables after ordering heuristic to a
    // csv file
    public void csvOrder() throws IOException {

        String fileContent = myProblem.getProblemName() + "," + this.orderingHeuristic + ",";

        for (MyVariable v : current_path) {
            fileContent += v.getName() + ",";
        }
        fileContent += "\n";

        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:/Users/14022/Documents/VS Code Projects/csp-solver/out.csv", true));
        writer.write(fileContent);
        writer.close();

    }

    public void runSearch(String searchType) throws IOException {

        // Run BCSSP
        SearchAlgorithms searchAlgorithms = new SearchAlgorithms(this.myProblem, this.current_path, this.assignments,
                searchType);
        searchAlgorithms.BCSSP(this.variables.size(), "unknown");

        // Writing the results to a csv fileSD
        // String fileContent = myProblem.getProblemName() + "," + searchType + "," +
        // this.orderingHeuristic + ","
        // + searchAlgorithms.getCSVRow() + "\n";

        // BufferedWriter writer = new BufferedWriter(
        // new FileWriter("C:\\Users\\14022\\Documents\\VS Code
        // Projects\\csp-solver\\out.csv", true));
        // writer.write(fileContent);
        // writer.close();

    }

}