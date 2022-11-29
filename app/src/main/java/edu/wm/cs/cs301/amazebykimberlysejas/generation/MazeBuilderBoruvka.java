package edu.wm.cs.cs301.amazebykimberlysejas.generation;

import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

	
	public class MazeBuilderBoruvka extends MazeBuilder implements Runnable{
		
		
		public static int [][] horMatrix;
		public static int [][] verMatrix;
		public static HashMap<Integer, Wallboard> wallWeights = new HashMap<Integer, Wallboard>();
		public static ArrayList<Wallboard> wallboards = new ArrayList<Wallboard>();
		public static ArrayList<Integer> wallvals = new ArrayList<Integer>();
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderPrim.class.getName());

	


	

	//constructor 
	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using B algorithm to generate maze.");

	}
	
	/**
	 * 
	 * @return vals
	 * creates a matrix with nodes to represent a maze with all of its walls up  
	 */
	public int [][] nodeMatrix(){
		int vals [][] = new int[height][width];
		int num = ((height*(width-1))*2)+1 ;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				vals[i][j] = num + 1;
				num++;
			}
		}
		
		
		
		
		return vals;
		
	}
	
	/**
	 * 
	 * @param nodes
	 * @return status
	 * checks for one singular component in the algorithm
	 * to signal to stop 
	 */
	public boolean completeStatus(int [][] nodes) {
		int firstVal = nodes[0][0];
		boolean status = true; 
		for (int i=0; i<nodes.length; i++) {
			for (int j = 0; j<nodes[i].length; j++) {
				if (nodes[i][j] == firstVal) {
					status = true;
				}
				else {
					return false; 
				}
			}
		}
		return status; 
	}
	
	/**
	 * 
	 * @param nodes
	 * @return compWeights
	 * creates a dictionary to keep track of the minimum weight 
	 * of every component
	 */
	public HashMap<Integer, Integer> compDict(int [][] nodes) {
		HashMap<Integer, Integer> compWeights= new HashMap<Integer, Integer>();
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j< nodes[i].length; j++) {
				compWeights.put(nodes[i][j], ((height*(width-1))*2)+1);
			}
		}
		
		return compWeights;
		
	}
	
	@Override
	protected void generatePathways() {
		//make a list of all internal wallboards as that is what we're going to iterate through to find the cheapst wallboard for a node in the matrix 
		// iterate through the list of wallboards to and get the corresponding node value by using wallboard.getx and y and the neighboring node 
		// these values represent components and so we will first compare them
		// if they are NOT in the same component compare the wallboard's weight with the weight of the component the node and the neighbor node corresponds to
		// update if the wallboard weight is less that their component's weight 
		// to represent components we need to gather look at all the components to see what wallboard is the minimum 
		// we will get x and y of wallboard and find the value in the matrix and get neighboring x and y val in the matrix
		// and then set all occurances of the neighboring value in the matrix to the node value and use this matrix for the next iteration 
		


		int [][] nodes = nodeMatrix();		
		
		for (int y = 0; y < nodes.length; y++) {
			for (int x = 0; x< nodes[y].length; x++) {
				getEdgeWeight(x,y,CardinalDirection.North);
				getEdgeWeight(x,y,CardinalDirection.South);
				getEdgeWeight(x,y,CardinalDirection.East);
				getEdgeWeight(x,y,CardinalDirection.West);
			}
		}
		
		HashMap<Integer, Integer> compWeights = compDict(nodes);
		
		
		//while loop begins here
		while (!completeStatus(nodes)) {
		
			for (int i = 0; i < wallboards.size(); i++) {
				int u = nodes[wallboards.get(i).getY()][wallboards.get(i).getX()];
				int v = nodes[wallboards.get(i).getNeighborY()][wallboards.get(i).getNeighborX()];
				if (u != v) {
					int edgeWeight = getEdgeWeight(wallboards.get(i).getX(),wallboards.get(i).getY(),wallboards.get(i).getDirection());
					if (edgeWeight < compWeights.get(u)) {
						compWeights.replace(u, edgeWeight);
						
					}
					if (edgeWeight < compWeights.get(v)) {
						compWeights.replace(v, edgeWeight);
						
					}
				}
				else {
					continue;
				}
		
			}
			
			ArrayList<Integer> toTearWallWeights = new ArrayList<Integer>();
			ArrayList<Wallboard> toTearWallboards = new ArrayList<Wallboard>();

			//value merger 
			for (int b = 0; b<nodes.length; b++) {
				for(int c = 0; c<nodes[b].length; c++) {
					int minWallWeight = compWeights.get(nodes[b][c]);
					if (minWallWeight != ((height*(width-1))*2)+1 && !toTearWallWeights.contains(minWallWeight)) {
						toTearWallWeights.add(minWallWeight);
				    }
				}
				
			}
			
			for (Map.Entry<Integer, Integer> entry : compWeights.entrySet()) {
			    Integer key = entry.getKey();
			    compWeights.replace(key, ((height*(width-1))*2)+1); //resetting min weights of nodes
			    
			}
			
			//adding wallboards to list based off values
			for (int j = 0; j<toTearWallWeights.size(); j++) {
				int dictVal = toTearWallWeights.get(j);
				toTearWallboards.add(wallWeights.get(dictVal));
			}
			
			nodes = components(toTearWallboards, nodes);
	
			for (int c = 0; c<toTearWallboards.size(); c++) {
				floorplan.deleteWallboard(toTearWallboards.get(c));
			}
		
		}
	
		


		}
	
	/**
	 * 
	 * @param nodeVal
	 * @param neighborVal
	 * @param nodes
	 * @return nodes
	 * taking in the values of two components this method locates all existing nodes of that value
	 * so that it can be replaced with the value of the other component
	 */
	public int [][] converter(int nodeVal, int neighborVal, int [][] nodes){
		for (int i =0; i< nodes.length; i++) {
			for(int j = 0; j <nodes[i].length; j++) {
				if (nodes[i][j] == neighborVal) {
					nodes[i][j] = nodeVal;
				}
			}
		}
		return nodes;
		
	}
	
	/**
	 * 
	 * @param toTearWallboards
	 * @param nodes
	 * @return nodes
	 * creates a representation of merged components using a matrix 
	 * by making the value represenation of two components the same 
	 */
	public int [][] components(ArrayList<Wallboard> toTearWallboards, int [][] nodes){

		
		for (int i = 0; i< toTearWallboards.size(); i++) {
			int nodeVal = nodes[toTearWallboards.get(i).getY()][toTearWallboards.get(i).getX()];
			int neighborVal = nodes[toTearWallboards.get(i).getNeighborY()][toTearWallboards.get(i).getNeighborX()];
			
			nodes = converter(nodeVal, neighborVal, nodes);
			
		}

		return(nodes);

	}


	/**
	 * 
	 * @param x
	 * @param y
	 * @param cd
	 * @return edgeWeight
	 * uses a matrix for horizontal and vertical walls with randomized wall weights and returns the weight 
	 * given the corresponding coordinates and cardinal direction  
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection cd) {
		//horizontal walls are south and north, vertical walls are east and west
		// using a horizontal matrix and vertical matrix will allow us to use the corresponding x and y coordinates 
		
		
		//first we need to find the number of horizontal walls and the number of vertical walls to get the total amount of internal walls
		//the number of internal walls will determine the amount of randomized wall weights 
		//then we can generate the randomized wall weights for all internal walls
		//the horizontal matrix and vertical matrix will reassign the values at random 
		//using the parameters passed into get edge weight, we will get the corresponding value from horizontal matrix or vertical matrix 
		//based on whether the cd is east/west (horizontal) or north/south (vertical)
		
		
		
		if (horMatrix == null && verMatrix == null) {
			int numOfHor = width*(height-1);
			int numOfVer = height*(width-1);
			edgeVals(numOfVer, numOfHor);
			horMatrix = horEdgeMatrix();
			verMatrix = verEdgeMatrix();
		}
		Wallboard wallboard = new Wallboard(x,y,cd);
		if (!floorplan.isPartOfBorder(wallboard)) {
			if (cd == CardinalDirection.East || cd == CardinalDirection.West) {
				int edgeWeight = VerEdgeWeight(x,y, cd);
				if (!wallWeights.containsKey(edgeWeight)) {
					wallboards.add(wallboard);
					wallWeights.put(edgeWeight, wallboard);
			}
				return (edgeWeight);
			}
			else if (cd == CardinalDirection.North || cd == CardinalDirection.South){
				int edgeWeight = HorEdgeWeight(x,y, cd);
				if (!wallWeights.containsKey(edgeWeight)) {
					wallboards.add(wallboard);
					wallWeights.put(edgeWeight, wallboard);
				}	
				return (edgeWeight);
			}
		}
		return 0;			
		
	}

	/**
	 * 
	 * @param numOfVer
	 * @param numOfHor
	 * @return wallvals
	 * generates randomized values to be used as wall weights 
	 */
	public ArrayList<Integer> edgeVals(int numOfVer, int numOfHor){
		for (int i = 1; i < numOfVer+numOfHor+1; i ++) {
			wallvals.add(i);
		}
		return wallvals;
	}
	
	/**
	 * 
	 * @return horMatrix
	 * creates matrix for horizontal edges with shuffled numbers 
	 */
	public int [][] horEdgeMatrix(){

		Random r = new Random();
		int horMatrix[][] = new int [height-1][width];
		
		for (int i = 0; i < height-1; i++) {
			for (int j = 0; j < width; j++) {
				int randIndex = r.nextInt(wallvals.size());
				horMatrix[i][j] = wallvals.get(randIndex);
				wallvals.remove(randIndex);
				}
			}
		return horMatrix;
	}

	
	/**
	 * 
	 * @return verMatrix
	 * creates matrix for vertical edges with shuffled numbers 
	 */
	
	public int [][] verEdgeMatrix(){
		Random r = new Random();
		int verMatrix[][] = new int [height][width-1];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width-1; j++) {
				int randIndex = r.nextInt(wallvals.size());
				verMatrix[i][j] = wallvals.get(randIndex);
				wallvals.remove(randIndex);
				}
			}
		return verMatrix;
		
	}
	

	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cd
	 * @return int 
	 * using the horizontal matrix this method indexes it to return the weight 
	 */
	public int HorEdgeWeight(int x, int y, CardinalDirection cd) {
		for (int i = 0; i < horMatrix.length; i++) {
			for (int j = 0; j< horMatrix[i].length; j++) {
				Wallboard wallboard = new Wallboard(x,y,cd);
				if (!floorplan.isPartOfBorder(wallboard)){
					if (cd == CardinalDirection.South) {
						if (j == x && i == y ) {
							
							return (horMatrix[i][j]);
						}
					}
					else if(cd == CardinalDirection.North){
						if (j == x && i == y-1) {
							return (horMatrix[i][j]);
						}
					}
				}
				else {
					return 0;
				}
			}
		}
		return 0; 
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cd
	 * @return int
	 * using the vertical matrix this method indexes it to return the weight 
	 */
	public int VerEdgeWeight(int x, int y, CardinalDirection cd) {
		for (int i = 0; i < verMatrix.length; i++) {
			for (int j = 0; j< verMatrix[i].length; j++) {
				Wallboard wallboard = new Wallboard(x,y,cd);
				if (!floorplan.isPartOfBorder(wallboard)){
					if (cd == CardinalDirection.East) {
						if (j == x && i == y ) {
							return(verMatrix[i][j]);
						}
					}
					else if(cd == CardinalDirection.West){
						if (j == x-1 && i == y) {
							return(verMatrix[i][j]);
						}
					}
				}
				else {
					return(0);
				}
			}
		}
		return 0;
	}
	

	
	
	
	
	}