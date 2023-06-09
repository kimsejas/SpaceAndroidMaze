package edu.wm.cs.cs301.amazebykimberlysejas.generation;


import android.util.Log;

import java.util.Arrays;

import edu.wm.cs.cs301.amazebykimberlysejas.gui.DistanceSensor;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot;
import edu.wm.cs.cs301.amazebykimberlysejas.gui.RobotDriver;

/**
 * Class: WallFollower
 *
 * Responsibilities: Implements the RobotDriver interface uses sensors to make decisions by following
 * the left wall until robot reaches the exit
 *
 * Collaborators: Robot, Maze
 *
 *
 * Algorithm: Uses the left sensor to check for the presence of a left wall to follow. Uses forward
 * sensor to check for obstacles in front of the robot.
 *
 * Begins by checking if robot is at exit position and using canSeeThroughTheExitIntoEternity()
 * to face the exit if that is the case.
 *
 * Otherwise, use left sensor to check for a left wall and then forward sensor to check for a wall in front of the robot.
 * If there is a front wall there is a obstacle so the robot needs to rotate right.
 * Otherwise, there is no obstacle in front of the robot and it can move forward.
 *
 * If there is no left wall, there is an opening to another part of the maze and it needs rotate to it, rotate left and move forward.
 *
 * Continue this process until the robot finally reaches the exit.
 *
 *
 * The left and forward sensor may fail if either of them are unreliable during this process.
 * Thus, the first solution is to find a working sensor, rotate the working sensor to mimic the failed sensor's direction and use that sensor,
 * lastly undo the rotation to go back to its original state.
 *
 * If when attempting to find a working sensor, there is no working sensor, the second solution will be to wait
 * for the failed sensor to work again and use it once it's working.
 *
 *
 * @author KIMBERLY SEJAS
 */
public class WallFollower implements RobotDriver {
	public static Robot Robot;
	public static Maze maze;
	public static boolean foundExit = false;
	public static boolean robotStopped = false;




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
		Robot.addDistanceSensor(left, edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.LEFT);
		Robot.addDistanceSensor(right, edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.RIGHT);
		Robot.addDistanceSensor(forward, edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.FORWARD);
		Robot.addDistanceSensor(backward, edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.BACKWARD);



	}



	@Override
	public void setMaze(Maze maze) {
		WallFollower.maze = maze;

	}


	/*
	 * Uses the left sensor to check for the presence of a left wall to follow. Uses forward
	 * sensor to check for obstacles in front of the robot.
	 *
	 * Begins by checking if robot is at exit position and using canSeeThroughTheExitIntoEternity()
	 * to face the exit if that is the case.
	 *
	 * Otherwise, use left sensor to check for a left wall and then forward sensor to check for a wall in front of the robot.
	 * If there is a front wall there is a obstacle so the robot needs to rotate right.
	 * Otherwise, there is no obstacle in front of the robot and it can move forward.
	 *
	 * If there is no left wall, there is an opening to another part of the maze and it needs rotate to it, rotate left and move forward.
	 *
	 * Continue this process until the robot finally reaches the exit.
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		boolean result = true;
		while (result == true) {
			try {
			result = drive1Step2Exit();
			}catch (InterruptedException e){
				robotStopped = true;
				return false;
			}

		}
		foundExit = true;
		Robot.move(1);
		return true;
	}


	@Override
	public boolean drive1Step2Exit() throws Exception {
		if (Robot.hasStopped()) {
			robotStopped = true;
			Log.v("Game over", "robot ran out of energy. losing screen");
			throw new Exception("Robot stopped due to battery or crash");
		}

		if (atExit()) {
			Log.v("reached exit line", "true.");
			return false;
		}
		if (Robot.distanceToObstacle(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.LEFT)==0) {
			if (Robot.distanceToObstacle(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.FORWARD)== 0){
				Robot.rotate(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Turn.RIGHT);
				Log.v("turn right", "x=" + Robot.getCurrentPosition()[0] + ",y="+ Robot.getCurrentPosition()[1]+ ",cd=" + Robot.getCurrentDirection());
			}
			else {
				Robot.move(1);
				Log.v("move 1", "x=" + Robot.getCurrentPosition()[0] + ",y="+ Robot.getCurrentPosition()[1]+ ",cd=" + Robot.getCurrentDirection());
			}
		}
		else {
			Robot.rotate(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Turn.LEFT);
			Robot.move(1);
			Log.v("rotateleft & move1", "x=" + Robot.getCurrentPosition()[0] + ",y="+ Robot.getCurrentPosition()[1]+ ",cd=" + Robot.getCurrentDirection());

		}
		return true;
	}

	/*
	 * A private helper method for drive1Step2Exit() to determine when to stop by checking
	 * if robot is at exit
	 */
	public boolean atExit() throws Exception, Exception {
		if (getEnergyConsumption() < 1) {
			throw new Exception("Out of battery");
		}

		if (Arrays.equals(Robot.getCurrentPosition(), maze.getExitPosition())) {
			try {
				if (Robot.distanceToObstacle(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.LEFT) == Integer.MAX_VALUE) {
					Robot.rotate(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Turn.LEFT);
					foundExit = true;
					return true;
				}
				else if (Robot.distanceToObstacle(edu.wm.cs.cs301.amazebykimberlysejas.gui.Robot.Direction.FORWARD) == Integer.MAX_VALUE){
					foundExit = true;
					return true;
				}
			}catch(Exception e){

			}
			return false;
		}

		return false;
	}


	@Override
	public float getEnergyConsumption() {
		return Robot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return Robot.getOdometerReading();
	}

}
