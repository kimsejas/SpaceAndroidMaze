//package edu.wm.cs.cs301.amazebykimberlysejas.generation;
//
//import java.util.HashMap;
//
//import gui.DistanceSensor;
//import gui.Robot.Direction;
//import generation.CardinalDirection;
//
//
//
///**
// * Class: ReliableSensor
// *
// * Responsibilities: Implements DistanceSensor interface to calculate the robot's distance
// * from an obstacle
// *
// * Collaborators: Maze, CardinalDirection
// *
// * @author KIMBERLY SEJAS
// *
// */
//public class ReliableSensor implements DistanceSensor {
//	public static Maze Maze;
//	public  Direction sensor;
//	public  CardinalDirection sensorCd;
//
//
//	/*
//	 * Get the Robot's current cardinal direction. Find the sensor's current cardinal direction.
//	 * Create a seperate case for each sensor cardinal direction (NESW). To check for an obstacle use
//	 * hasHall from maze and pass in a new coordinate that corresponds with the sensor's cardinal direction
//	 * and keep track of how many times this is done (distance) and return that when wall is found.
//	 * If the new coordinate is out of bounds, then an exit was reached and return Integer.MAX_VALUE
//	 * coordinate
//	 */
//	@Override
//	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply) throws Exception {
//		if (powersupply[0] < 0) {
//			throw new IndexOutOfBoundsException();
//		}
//
//		if (powersupply[0] < 1) {
//			throw new Exception("Power failure: Battery level insufficient");
//		}
//
//		if (sensor == null ) {
//			throw new UnsupportedOperationException("Sensor failure: Sensor not operational");
//		}
//
//		if (currentPosition[0] < 0 || currentPosition[0] >= Maze.getWidth() || currentPosition[1] < 0 || currentPosition[1] >= Maze.getHeight()) {
//			throw new IllegalArgumentException("Current position out of range");
//		}
//
//		powersupply[0] = powersupply[0]- getEnergyConsumptionForSensing();
//
//		int curDx = currentDirection.getDxDyDirection()[0];
//		int curDy = currentDirection.getDxDyDirection()[1];
//
//		int curX = currentPosition[0];
//		int curY = currentPosition[1];
//
//		sensorCd  = getSensorDxDyDirection(curDx, curDy);
//		boolean obstacleFound = false;
//		int stepsTaken = 0;
//		if (sensorCd == CardinalDirection.North) {
//			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//			while (!obstacleFound) {
//				if (curY-1 < 0) {
//					return Integer.MAX_VALUE;
//				}
//				else {
//					curY-=1;
//					stepsTaken +=1;
//					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//				}
//			}
//			return stepsTaken;
//		}
//		else if (sensorCd == CardinalDirection.South) {
//			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//			while (!obstacleFound) {
//				if (curY+1 > Maze.getHeight()) {
//					return Integer.MAX_VALUE;
//				}
//				else {
//					curY+=1;
//					stepsTaken +=1;
//					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//				}
//			}
//			return stepsTaken;
//		}
//		else if (sensorCd == CardinalDirection.East) {
//			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//			while (!obstacleFound ) {
//				if (curX+1 > Maze.getWidth()) {
//					return Integer.MAX_VALUE;
//				}
//				else {
//					curX+=1;
//					stepsTaken +=1;
//					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//
//				}
//			}
//			return stepsTaken;
//
//		}
//		else if (sensorCd == CardinalDirection.West) {
//			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//			while (!obstacleFound) {
//				if (curX-1 < 0) {
//					return Integer.MAX_VALUE;
//				}
//				else {
//					curX-=1;
//					stepsTaken +=1;
//					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
//				}
//			}
//			return stepsTaken;
//
//		}
//		return stepsTaken;
//	}
//
//
//
//	/*
//	 * A helper function for distanceToObstacle() that takes in the robot's current dx and dy.
//	 * Check the direction of the sensor e.g. left,right,forward etc. and manipulate  the dx and dy
//	 * to match what it's cardinal direction would be.
//	 *
//	 * right case:flip the coordinates and negate the new dy value
//	 * left case: flip and negate the new dx
//	 * forward case: same
//	 * backward case: negate both
//	 *
//	 */
//	protected CardinalDirection getSensorDxDyDirection(int curDx, int curDy) {
//		int[] result = new int[2];
//		if (sensor == Direction.FORWARD) {
//			return CardinalDirection.getDirection(curDx, curDy);
//		}
//		else if (sensor == Direction.BACKWARD) {
//			result[0] = curDx * -1;
//			result[1] = curDy * -1;
//
//		}
//		else if (sensor == Direction.LEFT) {
//			result[0] = curDy * -1;
//			result[1] = curDx;
//		}
//		else if (sensor == Direction.RIGHT) {
//			result[0] = curDy;
//			result[1] = curDx * -1;
//		}
//
//		return CardinalDirection.getDirection(result[0], result[1]);
//
//	}
//
//	@Override
//	public void setMaze(Maze maze) throws IllegalArgumentException{
//		if (maze == null || maze.getFloorplan() == null) {
//			throw new IllegalArgumentException();
//		}
//		Maze = maze;
//
//	}
//
//	@Override
//	public void setSensorDirection(Direction mountedDirection) {
//		sensor = mountedDirection;
//	}
//
//	@Override
//	public float getEnergyConsumptionForSensing() {
//		return 1;
//	}
//
//	@Override
//	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
//			throws UnsupportedOperationException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
//		// TODO Auto-generated method stub
//
//	}
//
//}
