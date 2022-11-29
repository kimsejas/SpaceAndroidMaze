//package edu.wm.cs.cs301.amazebykimberlysejas.generation;
//
//import edu.wm.cs.cs301.amazebykimberlysejas.gui.DistanceSensor;
//import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot;
//import gui.Control;
//import gui.DistanceSensor;
//import gui.Robot;
//import gui.Constants.UserInput;
//import gui.Robot.Direction;
//import generation.ReliableSensor;
//
//import java.util.Arrays;
//
//
///**
// * Class: ReliableRobot
// *
// * Responsibilities: Implements Robot interface to Move and turn the robot, track energy costs and distance traveled,
// * gets distance from obstacles, and checks if in room or at exit
// *
// * Collaborators: Robot, Control, Maze, ReliableSensor
// * @author KIMBERLY SEJAS
// *
// */
//public class ReliableRobot implements Robot {
////	Control control;
//	float energy = 3500;
//	int disTraveled;
//	float[] powersupply = new float[1];
//	boolean hasStopped = false;
//
//	DistanceSensor left;
//	DistanceSensor right;
//	DistanceSensor forward;
//	DistanceSensor backward;
//
//
//
//
//	@Override
//	public void setController(Control controller) {
//		control = controller;
//
//	}
//
//	/*
//	 * Using the public sensor objects in the class, create an instance of the ReliableSensor class for
//	 * that sensor and call setSensorDirection() from ReliableSensor
//	 */
//	@Override
//	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
//		if (sensor == left) {
//			DistanceSensor left = new ReliableSensor();
//			left.setMaze(control.getMaze());
//			left.setSensorDirection(Direction.LEFT);
//			this.left = left;
//		}
//		else if (sensor == right) {
//			DistanceSensor right = new ReliableSensor();
//			right.setMaze(control.getMaze());
//			right.setSensorDirection(Direction.RIGHT);
//			this.right = right;
//		}
//		else if (sensor == forward) {
//			DistanceSensor forward = new ReliableSensor();
//			forward.setMaze(control.getMaze());
//			forward.setSensorDirection(Direction.FORWARD);
//			this.forward = forward;
//
//		}
//		else if (sensor == backward) {
//			DistanceSensor backward = new ReliableSensor();
//			backward.setMaze(control.getMaze());
//			backward.setSensorDirection(Direction.BACKWARD);
//			this.backward = backward;
//		}
//
//	}
//
//
//
//	@Override
//	public int[] getCurrentPosition() throws Exception {
//		return control.getCurrentPosition();
//	}
//
//	@Override
//	public CardinalDirection getCurrentDirection() {
//		return control.getCurrentDirection();
//
//	}
//
//	@Override
//	public float getBatteryLevel() {
//		return energy;
//	}
//
//	@Override
//	public void setBatteryLevel(float level) {
//		energy = level;
//	}
//
//	@Override
//	public float getEnergyForFullRotation() {
//		return 12;
//	}
//
//	@Override
//	public float getEnergyForStepForward() {
//		return 6;
//	}
//
//	@Override
//	public int getOdometerReading() {
//		return disTraveled;
//	}
//
//	@Override
//	public void resetOdometer() {
//		disTraveled = 0;
//	}
//
//
//
//	/*
//	 * Use switch statements for each turn case, check for sufficient battery, use handleKeyboardInput
//	 * from control to rotate and account for energy costs
//	 */
//	@Override
//	public void rotate(Turn turn) {
//
//			switch (turn) {
//
//			case LEFT:
//				if (getBatteryLevel() >= getEnergyForStepForward() && !hasStopped()) {
//					control.handleKeyboardInput(UserInput.LEFT, (control.getMaze().getHeight())*(control.getMaze().getWidth()));
//					setBatteryLevel(energy-3);
//					hasStopped();
//				}
//				break;
//
//			case RIGHT:
//				if (getBatteryLevel() >= getEnergyForStepForward()&& !hasStopped()) {
//					control.handleKeyboardInput(UserInput.RIGHT, (control.getMaze().getHeight())*(control.getMaze().getWidth()));
//					setBatteryLevel(energy-3);
//					hasStopped();
//				}
//
//				break;
//
//			case AROUND:
//				if (getBatteryLevel() >= getEnergyForStepForward()&& !hasStopped()) {
//					control.handleKeyboardInput(UserInput.LEFT, (control.getMaze().getHeight())*(control.getMaze().getWidth()));
//					control.handleKeyboardInput(UserInput.LEFT, (control.getMaze().getHeight())*(control.getMaze().getWidth()));
//					setBatteryLevel(energy-3);
//					hasStopped();
//				}
//				break;
//
//
//			}
//
//		}
//
//
//
//	/*
//	 * First check to make sure robot is not trying to move into wall. Then check
//	 * for sufficient energy to move forward and call handleKeyboardInput from control.
//	 * Account for energy costs and add to total distance traveled. To know when to stop
//	 * moving, subtract from the distance passed in and stop it reaches 0.
//	 */
//	@Override
//	public void move(int distance) {
//		if (distance < 0) {
//			throw new IllegalArgumentException("Negative distance not allowed");
//		}
////		robotCrash(); //checking if robot is going to move into a wall
//		while (getBatteryLevel() >= getEnergyForStepForward() && distance != 0 ) {
//			control.handleKeyboardInput(UserInput.UP, (control.getMaze().getHeight())*(control.getMaze().getWidth()));
//			setBatteryLevel(energy-getEnergyForStepForward());
//
//			distance -= 1;
//			disTraveled+=1;
//			hasStopped();
//		}
//	}
//
//
//	/**
//	 * A helper function for move() to check if the robot is going to crash into a wall.
//	 * Uses distanceToObstacle because if it is 0 and move() is trying to be called then
//	 * crash would occur and hasStopped will be updated to true.
//	 */
////	private void robotCrash() {
////		if (distanceToObstacle(Direction.FORWARD) == 0 ) {
////			hasStopped = true;
////		}
////	}
//
//
//	@Override
//	public void jump() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public boolean isAtExit() throws Exception {
//		int [] exitPos = control.getMaze().getExitPosition();
//		return Arrays.equals(exitPos, getCurrentPosition());
//	}
//
//	@Override
//	public boolean isInsideRoom() throws Exception {
//		return control.getMaze().isInRoom(getCurrentPosition()[0], getCurrentPosition()[1]);
//	}
//
//	/*
//	 * Energy and hasStopped will be global variables so they can be updated across all methods. This method will
//	 * check if energy is <= 0, if so then the robot should stop and hasStopped is true.
//	 * Otherwise, it'll return hasStopped.
//	 */
//	@Override
//	public boolean hasStopped() {
//		if (energy <= 0 ) {
//			hasStopped = true;
//		}
//		return hasStopped;
//	}
//
//
//	/*
//	 * Will call distanceToObstacle from ReliableSensor on one of the robot's sensors e.g. left or right.
//	 */
//	@Override
//	public int distanceToObstacle(Direction direction) {
//
//		powersupply[0] = energy;
//
//		switch (direction) {
//		case LEFT:
//			try {
//				int dist =  left.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case RIGHT:
//			try {
//				int dist = right.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case FORWARD:
//			try {
//				int dist = forward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		case BACKWARD:
//			try {
//				int dist = backward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return 0;
//
//	}
//
//	/*
//	 * Will call distanceToObstacle from ReliableSensor on one of the robot's sensors e.g. left or right.
//	 * That method will have a special case for when at an exit.
//	 */
//	@Override
//	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
//		powersupply[0] = energy;
//
//		switch (direction) {
//		case LEFT:
//			try {
//				if (left.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply) == Integer.MAX_VALUE) {
//					energy = powersupply[0];
//					return true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			energy = powersupply[0];
//			break;
//
//
//		case RIGHT:
//			try {
//				if (right.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply) == Integer.MAX_VALUE) {
//					energy = powersupply[0];
//					return true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			energy = powersupply[0];
//			break;
//
//		case FORWARD:
//			try {
//				if (forward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply) == Integer.MAX_VALUE) {
//					energy = powersupply[0];
//					return true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			energy = powersupply[0];
//			break;
//
//		case BACKWARD:
//			try {
//				if (backward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply) == Integer.MAX_VALUE) {
//					energy = powersupply[0];
//					return true;
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			energy = powersupply[0];
//			break;
//		}
//		return false;
//	}
//
//	@Override
//	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
//			throws UnsupportedOperationException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
//		// TODO Auto-generated method stub
//
//	}
//
//}