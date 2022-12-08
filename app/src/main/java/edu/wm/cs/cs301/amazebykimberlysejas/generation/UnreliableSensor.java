package edu.wm.cs.cs301.amazebykimberlysejas.generation;



import edu.wm.cs.cs301.amazebykimberlysejas.gui.DistanceSensor;

/**
 * Class: UnreliableSensor
 *
 * Responsibilities: Implements DistanceSensor interface to calculate the robot's distance
 * from an obstacle. Implements Runnable to switch unreliable sensors between operational and failed states for the
 * the failure and repair processes.
 *
 * Collaborators: Maze, Runnable, CardinalDirection
 *
 * @author KIMBERLY SEJAS
 *
 */

public class UnreliableSensor extends ReliableSensor implements DistanceSensor, Runnable{


	/*
	 * keeps track of whether the game is running or ended
	 */
	public boolean runningGame = true;


	/*
	 * State of the sensor. True if operational.
	 * False if failing.
	 */
	public boolean operational = true;





	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		if (powersupply[0] < 0) {
			throw new IndexOutOfBoundsException();
		}

		if (powersupply[0] < 1) {
			throw new Exception("Power failure: Battery level insufficient");
		}

		if (sensor == null || operational == false) {
			throw new UnsupportedOperationException("Sensor failure: sensor not operational");
		}

		if (currentPosition[0] < 0 || currentPosition[0] >= Maze.getWidth() || currentPosition[1] < 0 || currentPosition[1] >= Maze.getHeight()) {
			throw new IllegalArgumentException("Current position out of range");
		}

		powersupply[0] = powersupply[0]- getEnergyConsumptionForSensing();

		int curDx = currentDirection.getDxDyDirection()[0];
		int curDy = currentDirection.getDxDyDirection()[1];

		int curX = currentPosition[0];
		int curY = currentPosition[1];

//		sensorCd  = getSensorDxDyDirection(curDx, curDy);
		boolean obstacleFound = false;
		int stepsTaken = 0;
		if (sensorCd == CardinalDirection.North) {
			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
			while (!obstacleFound) {
				if (curY-1 < 0) {
					return Integer.MAX_VALUE;
				}
				else {
					curY-=1;
					stepsTaken +=1;
					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
				}
			}
			return stepsTaken;
		}
		else if (sensorCd == CardinalDirection.South) {
			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
			while (!obstacleFound) {
				if (curY+1 >= Maze.getHeight()) {
					return Integer.MAX_VALUE;
				}
				else {
					curY+=1;
					stepsTaken +=1;
					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
				}
			}
			return stepsTaken;
		}
		else if (sensorCd == CardinalDirection.East) {
			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
			while (!obstacleFound ) {
				if (curX+1 >= Maze.getWidth()) {
					return Integer.MAX_VALUE;
				}
				else {
					curX+=1;
					stepsTaken +=1;
					obstacleFound = Maze.hasWall(curX, curY, sensorCd);

				}
			}
			return stepsTaken;

		}
		else if (sensorCd == CardinalDirection.West) {
			obstacleFound = Maze.hasWall(curX, curY, sensorCd);
			while (!obstacleFound) {
				if (curX-1 < 0) {
					return Integer.MAX_VALUE;
				}
				else {
					curX-=1;
					stepsTaken +=1;
					obstacleFound = Maze.hasWall(curX, curY, sensorCd);
				}
			}
			return stepsTaken;

		}
		return stepsTaken;
	}


	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {

		if (operational == true) {

			try {
				Thread.sleep(meanTimeBetweenFailures);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			operational = false;

		}
		else if (operational == false){

			try {
				Thread.sleep(meanTimeToRepair);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			operational = true;


			}


	}

	/*
	 * Will be called when the game has ended. Sets the sensor's operational state to true.
	 * Sets the global boolean runningGame to false to indicate the game has ended
	 * for the thread to terminate.
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		operational = true;
		runningGame = false;



	}



	/*
	 * The thread will continue the failure and repair process until the game has ended.
	 * The global boolean runningGame will be set to false when stopFailureAndRepairProcess() and thus
	 * the thread will exit the while loop and run() then terminate.
	 */
	@Override
	public void run() {

			while (runningGame) {
				startFailureAndRepairProcess(4, 2);
			}
		}

	}


