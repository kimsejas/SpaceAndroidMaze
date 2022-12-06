package edu.wm.cs.cs301.amazebykimberlysejas.generation;

import java.util.Arrays;

import edu.wm.cs.cs301.amazebykimberlysejas.gui.DistanceSensor;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.RobotDriver;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Turn;


/**
 * Class: Wizard
 *
 * Responsibilities: Implements the RobotDriver interface to drive the robot to exit if
 * it exits and if it has enough energy.
 *
 * Collaborators: Maze, ReliableRobot
 * @author KIMBERLY SEJAS
 *
 */
public class Wizard implements RobotDriver {
	public static Maze Maze;
	public static Robot Robot;
	public static boolean foundExit = false;

	DistanceSensor left;
	DistanceSensor right;
	DistanceSensor forward;
	DistanceSensor backward;


	/*
	 * Set robot and also add left,right,forward, and backward distance sensors.
	 */
	@Override
	public void setRobot(Robot r) {
		Robot = r;
		Robot.addDistanceSensor(left, Direction.LEFT);
		Robot.addDistanceSensor(right, Direction.RIGHT);
		Robot.addDistanceSensor(forward, Direction.FORWARD);
		Robot.addDistanceSensor(backward, Direction.BACKWARD);

	}

	@Override
	public void setMaze(Maze maze) {
		Maze = maze;

	}

	/*
	 *  Call drive1Step2Exit until it returns false, as that means it reached the exit.
	 *  Then move one step forward and return true. Return false if the reason why drive1Step2Exit
	 *  stopped due to battery etc.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		while (drive1Step2Exit()) {
			try {
				drive1Step2Exit();
			} catch (Exception e) {
				throw new Exception("out of battery");
			}
		}
		Robot.move(1);
		return true;
	}


	/*
	 * Get the current robot's position. Get the neighbor's position using getNeighborCloserToExit from Maze.
	 * Get the robot's current cardinal direction.
	 * Figure out which cardinal direction the neighbor position would face and compare them.
	 * If they are not the same then a rotation is needed followed by a move.
	 * Otherwise, a rotation is not needed and the robot can move.
	 * After the move, check if the robot's new position is at an exit as this should return false.
	 * Otherwise, return true.
	 *
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		int curX = Robot.getCurrentPosition()[0];
		int curY = Robot.getCurrentPosition()[1];

		if (atExit()) {
			return false;
		}

		int neighborX = Maze.getNeighborCloserToExit(curX, curY)[0];
		int neighborY = Maze.getNeighborCloserToExit(curX, curY)[1];

		CardinalDirection cdToFace = cdToFace(Robot.getCurrentDirection(), curX, curY, neighborX, neighborY);
		CardinalDirection curCd = Robot.getCurrentDirection();

		if (curCd!=cdToFace) {
			//rotate first and then move
			if (Robot.getBatteryLevel() < 9) {
				throw new Exception("Out of battery");
			}
			else if (!Robot.hasStopped()) {
				rotateAndMove(Robot.getCurrentDirection(), cdToFace);
				if (atExit()) {
					return false;
				}
				return true;
			}
		}
		else {
			//no rotation needed so move
			if (Robot.getBatteryLevel() < 6) {
				throw new Exception("Out of battery");
			}
			else if (!Robot.hasStopped()){
				Robot.move(1);
				if (atExit()) {

					return false;
				}
				return true;
			}
			}
		return false;
		}


	/*
	 * A helper function for drive1Step2Exit that checks if robot has been driven to exit already in that case it rotates the robot
	 * such that if faces the exit in its forward direction
	 */
	private boolean atExit() {
		try {
			if (Arrays.equals(Robot.getCurrentPosition(), Maze.getExitPosition())) {


				if (Robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)) {
					return true;
				}
				Turn exitDirection = null;
				if (Robot.canSeeThroughTheExitIntoEternity(Direction.LEFT)) {
					exitDirection = Turn.LEFT;
				}
				if (Robot.canSeeThroughTheExitIntoEternity(Direction.RIGHT)) {
					exitDirection = Turn.RIGHT;
				}
				if (Robot.canSeeThroughTheExitIntoEternity(Direction.BACKWARD)) {
					exitDirection = Turn.AROUND;
				}
				Robot.rotate(exitDirection);
				foundExit = true;
				return true;
			}
			else {
				return false;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	/*
	 * A helper function for drive1Step2Exit to figure out which cardinal direction the robot should face for its next move.
	 * Create a separate case for each cardinal direction and all the other directions it can face and return a cardinal direction.
	 */
	private CardinalDirection cdToFace (CardinalDirection curCd, int curX, int curY, int neighborX, int neighborY) {
		if (curCd == CardinalDirection.North) {
			if (neighborX > curX) {
				return CardinalDirection.East;
			}
			else if(neighborX < curX) {
				return CardinalDirection.West;
			}
			else if (neighborY> curY) {
				return CardinalDirection.South;
			}
			else {
				return curCd;
			}
		}
		else if (curCd == CardinalDirection.South) {
			if (neighborX > curX) {
				return CardinalDirection.East;
			}
			else if(neighborX < curX) {
				return CardinalDirection.West;
			}
			else if (neighborY< curY) {
				return CardinalDirection.North;
			}
			else {
				return curCd;
			}

		}

		else if (curCd == CardinalDirection.East) {
			if (neighborY> curY) {
				return CardinalDirection.South;
			}
			else if(neighborY < curY) {
				return CardinalDirection.North;
			}
			else if(neighborX < curX) {
				return CardinalDirection.West;
			}
			else {
				return curCd;
			}

		}

		else if (curCd == CardinalDirection.West) {
			if (neighborY> curY) {
				return CardinalDirection.South;
			}
			else if(neighborY < curY) {
				return CardinalDirection.North;
			}
			else if(neighborX > curX) {
				return CardinalDirection.East;
			}
			else {
				return curCd;
			}

		}
		return null;
	}


	/*
	 * A helper function for drive1Step2Exit that figure out what way to turn given the robot's cardinal direction
	 * and a cardinal direction for the next move.
	 * Create a case for each cardinal direction and all the other ways it could face and return a Turn object that will make the
	 * robot's current direction match with the move's next direction. Move forwards.
	 */
	private void rotateAndMove(CardinalDirection curCd, CardinalDirection cdToFace) {
		if (curCd == CardinalDirection.East) {
			if (cdToFace == CardinalDirection.South) {
				Robot.rotate(Turn.LEFT);
			}
			if (cdToFace == CardinalDirection.North) {
				Robot.rotate(Turn.RIGHT);
			}
			if (cdToFace == CardinalDirection.West) {
				Robot.rotate(Turn.AROUND);
			}
			Robot.move(1);

		}
		if (curCd == CardinalDirection.West) {
			if (cdToFace == CardinalDirection.South) {
				Robot.rotate(Turn.RIGHT);
			}
			if (cdToFace == CardinalDirection.North) {
				Robot.rotate(Turn.LEFT);
			}
			if (cdToFace == CardinalDirection.East) {
				Robot.rotate(Turn.AROUND);
			}
			Robot.move(1);


		}
		if (curCd == CardinalDirection.South) {
			if (cdToFace == CardinalDirection.East) {
				Robot.rotate(Turn.RIGHT);
			}
			if (cdToFace == CardinalDirection.West) {
				Robot.rotate(Turn.LEFT);
			}
			if (cdToFace == CardinalDirection.North) {
				Robot.rotate(Turn.AROUND);
			}
			Robot.move(1);
		}
		if (curCd == CardinalDirection.North) {
			if (cdToFace == CardinalDirection.East) {
				Robot.rotate(Turn.LEFT);
			}
			if (cdToFace == CardinalDirection.West) {
				Robot.rotate(Turn.RIGHT);
			}
			if (cdToFace == CardinalDirection.South) {
				Robot.rotate(Turn.AROUND);
			}
			Robot.move(1);
		}
	}


	@Override
	public float getEnergyConsumption() {
		return 3500 - Robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		return Robot.getOdometerReading();
	}


}