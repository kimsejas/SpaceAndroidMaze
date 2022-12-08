package edu.wm.cs.cs301.amazebykimberlysejas.generation;

import android.util.Log;

import edu.wm.cs.cs301.amazebykimberlysejas.gui.CompassRose;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Constants;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.DistanceSensor;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.FirstPersonView;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.MazePanel;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.PlayAnimationActivity;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.RobotDriver;

/**
 * Class: ReliableRobot
 *
 * Responsibilities: Implements Robot interface to Move and turn the robot, track energy costs and distance traveled,
 * gets distance from obstacles, and checks if in room or at exit
 *
 * Collaborators: Robot, Control, Maze, ReliableSensor
 * @author KIMBERLY SEJAS
 *
 */
public class ReliableRobot implements Robot {
//	Control control;
	float energy = 3500;
	int pathLength;
	float[] powersupply = new float[1];
	public boolean hasStopped = false;

	DistanceSensor left;
	DistanceSensor right;
	DistanceSensor forward;
	DistanceSensor backward;

	int px;
	int py;
	CardinalDirection cd;
	Maze Maze;

	public void setMaze(Maze maze){
		Maze = maze;
	}



	/*
	 * Using the public sensor objects in the class, create an instance of the ReliableSensor class for
	 * that sensor and call setSensorDirection() from ReliableSensor
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		if (sensor == left) {
			DistanceSensor left = new ReliableSensor();
			left.setSensorDirection(Direction.LEFT);
			this.left = left;
		}
		else if (sensor == right) {
			DistanceSensor right = new ReliableSensor();
			right.setSensorDirection(Direction.RIGHT);
			this.right = right;
		}
		else if (sensor == forward) {
			DistanceSensor forward = new ReliableSensor();
			forward.setSensorDirection(Direction.FORWARD);
			this.forward = forward;

		}
		else if (sensor == backward) {
			DistanceSensor backward = new ReliableSensor();
			backward.setSensorDirection(Direction.BACKWARD);
			this.backward = backward;
		}

	}



	@Override
	public int[] getCurrentPosition() {
        return new int[] {px,py};
	}

	public void setCurrentPosition(int x, int y){
		px = x;
		py = y;
	}


	@Override
	public CardinalDirection getCurrentDirection() {
//		return control.getCurrentDirection();
        return cd;
	}

	public void setCurrentDirection(CardinalDirection direction){
		cd = direction;
	}

	@Override
	public float getBatteryLevel() {
		return energy;
	}

	@Override
	public void setBatteryLevel(float level) {
		energy = level;
	}

	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	@Override
	public float getEnergyForStepForward() {
		return 6;
	}

	@Override
	public int getOdometerReading() {
		return pathLength;
	}

	@Override
	public void resetOdometer() {
		pathLength = 0;
	}



	/*
	 * Use switch statements for each turn case, check for sufficient battery, use handleKeyboardInput
	 * from control to rotate and account for energy costs
	 */
	@Override
	public void rotate(Turn turn) {

			switch (turn) {

			case LEFT:
				if (getBatteryLevel() >= 3 && !hasStopped()) {
					rotateSteps(1);
					setBatteryLevel(energy-3);
					hasStopped();
				}
				else{
					hasStopped = true;
				}
				break;

			case RIGHT:
				if (getBatteryLevel() >= 3&& !hasStopped()) {
					rotateSteps(-1);
					setBatteryLevel(energy-3);
					hasStopped();
				}else{
					hasStopped = true;
				}

				break;

			case AROUND:
				if (getBatteryLevel() >= 6 && !hasStopped()) {
					rotateSteps(1);
					rotateSteps(1);
					setBatteryLevel(energy-6);
					hasStopped();
				}
				else{
					hasStopped = true;
				}

			}

		}


	/**
	 * Rotate steps since there is no control.
 	 * @param dir direction. -1 is right. 1 is left
	 */
	public void rotateSteps(int dir){
		final int originalAngle = cd.angle();//angle;
		final int steps = 4;
		int angle = originalAngle; // just in case for loop is skipped
		for (int i = 0; i != steps; i++) {
			// add 1/4 of 90 degrees per step
			// if dir is -1 then subtract instead of addition
			angle = originalAngle + dir*(90*(i+1))/steps;
			angle = (angle+1800) % 360;
			// draw method is called and uses angle field for direction
			// information.
		}
		// update maze direction only after intermediate steps are done
		// because choice of direction values are more limited.
		cd = CardinalDirection.getDirection(angle);
	}



	/*
	 * First check to make sure robot is not trying to move into wall. Then check
	 * for sufficient energy to move forward and call handleKeyboardInput from control.
	 * Account for energy costs and add to total distance traveled. To know when to stop
	 * moving, subtract from the distance passed in and stop it reaches 0.
	 */
	@Override
	public void move(int distance) {
		if (distance < 0) {
			throw new IllegalArgumentException("Negative distance not allowed");
		}
		if (getBatteryLevel() >= getEnergyForStepForward()){
			walk(1);
			setBatteryLevel(energy-getEnergyForStepForward());
			pathLength+=1;
			hasStopped();
		}else{
			hasStopped = true;
		}
	}

	/**
	 * Moves in the given direction with 4 intermediate steps,
	 * updates the screen and the internal position
	 * @param dir, only possible values are 1 (forward) and -1 (backward)
	 */
	private synchronized void walk(int dir) {
		// check if there is a wall in the way
//		if (!wayIsClear(dir))
//			return;
//		pathLength+=1;
		int walkStep = 0;
		// walkStep is a parameter of FirstPersonView.draw()
		// it is used there for scaling steps
		// so walkStep is implicitly used in slowedDownRedraw
		// which triggers the draw operation in
		// FirstPersonView and Map
		for (int step = 0; step != 4; step++) {
			walkStep += dir;
		}
		// update position to neighbor
		int[] tmpDxDy = cd.getDxDyDirection();
		setCurrentPosition(px + dir*tmpDxDy[0], py + dir*tmpDxDy[1]) ;
	}





	/**
	 * A helper function for move() to check if the robot is going to crash into a wall.
	 * Uses distanceToObstacle because if it is 0 and move() is trying to be called then
	 * crash would occur and hasStopped will be updated to true.
	 */
//	private void robotCrash() {
//		if (distanceToObstacle(Direction.FORWARD) == 0 ) {
//			hasStopped = true;
//		}
//	}


	//TODO implement this
	@Override
	public void jump() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAtExit() throws Exception {
//		int [] exitPos = control.getMaze().getExitPosition();
//		return Arrays.equals(exitPos, getCurrentPosition());
        return false;
	}

	@Override
	public boolean isInsideRoom() throws Exception {
//		return control.getMaze().isInRoom(getCurrentPosition()[0], getCurrentPosition()[1]);
        return false;

    }

	/*
	 * Energy and hasStopped will be global variables so they can be updated across all methods. This method will
	 * check if energy is <= 0, if so then the robot should stop and hasStopped is true.
	 * Otherwise, it'll return hasStopped.
	 */
	@Override
	public boolean hasStopped() {
		if (energy <= 0 ) {
			hasStopped = true;
		}
		return hasStopped;
	}


	/*
	 * Will call distanceToObstacle from ReliableSensor on one of the robot's sensors e.g. left or right.
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws Exception {


		powersupply[0] = energy;
		if (direction == Direction.LEFT){
			int dist =  left.distanceToObstacle(getCurrentPosition(), cd, powersupply);
			energy = powersupply[0];
			return dist;
		}else if(direction == Direction.RIGHT){
			int dist =  right.distanceToObstacle(getCurrentPosition(), cd, powersupply);
			energy = powersupply[0];
			return dist;

		}else if(direction == Direction.FORWARD){
			int dist =  forward.distanceToObstacle(getCurrentPosition(), cd, powersupply);
			energy = powersupply[0];
			return dist;

		}else if(direction == Direction.BACKWARD){
			int dist =  backward.distanceToObstacle(getCurrentPosition(), cd, powersupply);
			energy = powersupply[0];
			return dist;

		}

//		switch (direction) {
//		case LEFT:
//			try {
//				int dist =  left.distanceToObstacle(getCurrentPosition(), cd, powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case RIGHT:
//			try {
//				int dist = right.distanceToObstacle(getCurrentPosition(), cd, powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case FORWARD:
//			try {
//				int dist = forward.distanceToObstacle(getCurrentPosition(), cd, powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case BACKWARD:
//			try {
//				int dist = backward.distanceToObstacle(getCurrentPosition(), cd, powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		return 0;

	}

	/*
	 * Will call distanceToObstacle from ReliableSensor on one of the robot's sensors e.g. left or right.
	 * That method will have a special case for when at an exit.
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		powersupply[0] = energy;

		switch (direction) {
		case LEFT:
			try {
				if (left.distanceToObstacle(getCurrentPosition(), cd, powersupply) == Integer.MAX_VALUE) {
					energy = powersupply[0];
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			energy = powersupply[0];
			break;


		case RIGHT:
			try {
				if (right.distanceToObstacle(getCurrentPosition(), cd, powersupply) == Integer.MAX_VALUE) {
					energy = powersupply[0];
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			energy = powersupply[0];
			break;

		case FORWARD:
			try {
				if (forward.distanceToObstacle(getCurrentPosition(), cd, powersupply) == Integer.MAX_VALUE) {
					energy = powersupply[0];
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			energy = powersupply[0];
			break;

		case BACKWARD:
			try {
				if (backward.distanceToObstacle(getCurrentPosition(), cd, powersupply) == Integer.MAX_VALUE) {
					energy = powersupply[0];
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			energy = powersupply[0];
			break;
		}
		return false;
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}