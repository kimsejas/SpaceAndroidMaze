//package edu.wm.cs.cs301.amazebykimberlysejas.generation;
//
//import gui.DistanceSensor;
//import gui.Robot;
//import gui.Robot.Direction;
//import gui.Robot.Turn;
//
//
///*
// * Class: UnreliableRobot
// *
// * Responsibilities: Implements Robot interface to move and rotate the robot, track energy costs and distance traveled,
// * gets distance from obstacles using sensors, and checks if in room or at exit
// *
// *
// * Collaborators: DistanceSensor (ReliableSensor/UnreliableSensor), Control, Maze, Robot
// */
//
//public class UnreliableRobot extends ReliableRobot implements Robot {
//
//	Thread leftThread;
//	Thread rightThread;
//	Thread forwardThread;
//	Thread backwardThread;
//
//	UnreliableSensor left;
//	UnreliableSensor right;
//	UnreliableSensor forward;
//	UnreliableSensor backward;
//
//
//
//	@Override
//	public int distanceToObstacle(Direction direction) {
//	powersupply[0] = energy;
//
//		switch (direction) {
//		case LEFT:
//			try {
//				int dist =  left.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception UnsupportedOperationException) {
//				//plan A
//				UnreliableSensor workingSensor = findWorkingSensor(left);
//				boolean state = workingSensor.operational;
//				if (workingSensor != null) {
//					Turn workingSensorRotationMade = workingSensorRotation(left, workingSensor);
//					try {
//						int dist = workingSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//						rotateBack(workingSensorRotationMade);
//						energy = powersupply[0];
//						return dist;
//					} catch (Exception e1) {
//						rotateBack(workingSensorRotationMade);
//						System.out.println(state);
//						System.out.println(workingSensor.operational);
//						System.out.println("plan a did not work");
//						int dist = waitWorkingSensor(left);
//						System.out.println(dist);
//						return dist;
//
//					}
//				}
//		}
//
//		case RIGHT:
//			try {
//				int dist = right.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//
//			}
//
//		case FORWARD:
//			try {
//				int dist = forward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception UnsupportedOperationException) {
//				//plan A
//				UnreliableSensor workingSensor = findWorkingSensor(forward);
//				boolean state = workingSensor.operational;
//				if (workingSensor != null) {
//					Turn workingSensorRotationMade = workingSensorRotation(forward, workingSensor);
//					try {
//						int dist = workingSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//						rotateBack(workingSensorRotationMade);
//						energy = powersupply[0];
//						return dist;
//					} catch (Exception e1) {
//						rotateBack(workingSensorRotationMade);
//						System.out.println(state);
//						System.out.println(workingSensor.operational);
//						System.out.println("plan a did not work");
//						int dist = waitWorkingSensor(forward);
//						System.out.println(dist);
//						return dist;
//
//					}
//				}
//
//
//			}
//
//		case BACKWARD:
//			try {
//				int dist = backward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//				energy = powersupply[0];
//				return dist;
//			} catch (Exception e) {
//			}
//		}
//		return 0;
//	}
//
//	/*
//	 * Private helper function for distanceToObstacle. Waits for failed sensor to work again.
//	 */
//	private int waitWorkingSensor(UnreliableSensor failedSensor) {
//		boolean workingStatus = failedSensor.operational;
//		int result = 0;
//		while (workingStatus == false ) {
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//			}
//			workingStatus = failedSensor.operational;
//		}
//		try {
//			result = failedSensor.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), powersupply);
//			energy = powersupply[0];
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//
//
//
//	/*
//	 * Private helper function for distanceToObstacle when a sensor is failing.
//	 * Identifies a current working sensor by checking the other possible working sensors for the left or
//	 * forward failed sensor by seeing it its operational.
//	 */
//	private UnreliableSensor findWorkingSensor(UnreliableSensor failedSensor) {
//		if (failedSensor == left) {
//			if (forward.operational == true ) {
//				return forward;
//			}
//			if (backward.operational == true ) {
//				return backward;
//			}
//			if (right.operational == true ) {
//				return right;
//			}
//
//		}
//		if (failedSensor == forward) {
//			if (left.operational == true ) {
//				return left;
//			}
//			if (right.operational == true ) {
//				return right;
//			}
//			if (backward.operational == true ) {
//				return backward;
//			}
//		}
//
//		return null;
//	}
//
//	/*
//	 * Private helper function for distanceToObstacle to rotate the robot so that
//	 * the working sensor can mimic the failed sensor. Returns the rotation made for when
//	 * the robot will rotate back.
//	 */
//	private Turn workingSensorRotation(UnreliableSensor failedSensor, UnreliableSensor workingSensor) {
//		Turn rotation = null;
//		if ( (failedSensor == left && workingSensor == right) ||(failedSensor == forward && workingSensor == backward)) {
//			rotation = Turn.AROUND;
//			rotate(rotation);
//
//			return rotation;
//		}
//		else {
//			if (failedSensor == left && workingSensor == forward) {
//				rotation = Turn.LEFT;
//				rotate(rotation);
//
//				return rotation;
//
//			}
//
//			if (failedSensor == left && workingSensor == backward) {
//				rotation = Turn.RIGHT;
//				rotate(rotation);
//
//				return rotation;
//
//			}
//
//			if (failedSensor == forward && workingSensor == left) {
//				rotation = Turn.RIGHT;
//				rotate(rotation);
//
//				return rotation;
//
//			}
//
//			if (failedSensor == forward && workingSensor == right) {
//				rotation = Turn.LEFT;
//				rotate(rotation);
//				return rotation;
//
//			}
//		}
//		return null;
//
//	}
//
//
//	/*
//	 * Private helper function for distanceToObstacle to rotate the robot back to the direction it was facing by
//	 * doing the reversing the rotation e.g. if rotated left then rotate right
//	 */
//	private void rotateBack (Turn workingSensorRotationMade) {
//		if (workingSensorRotationMade == Turn.AROUND) {
//			rotate(Turn.AROUND);
//		}
//		else if (workingSensorRotationMade == Turn.LEFT) {
//			rotate(Turn.RIGHT);
//		}
//		else if (workingSensorRotationMade == Turn.RIGHT) {
//			rotate(Turn.LEFT);
//		}
//	}
//
//	@Override
//	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
//		if (sensor == left) {
//			UnreliableSensor left = new UnreliableSensor();
//			left.setMaze(control.getMaze());
//			left.setSensorDirection(Direction.LEFT);
//			Thread leftThread = new Thread((Runnable) left);
//			this.left = left;
//			this.leftThread = leftThread;
//		}
//		else if (sensor == right) {
//			UnreliableSensor right = new UnreliableSensor();
//			right.setMaze(control.getMaze());
//			right.setSensorDirection(Direction.RIGHT);
//			Thread rightThread = new Thread((Runnable) right);
//			this.right = right;
//			this.rightThread = rightThread;
//		}
//		else if (sensor == forward) {
//			UnreliableSensor forward = new UnreliableSensor();
//			forward.setMaze(control.getMaze());
//			forward.setSensorDirection(Direction.FORWARD);
//			Thread forwardThread = new Thread((Runnable) forward);
//			this.forward = forward;
//			this.forwardThread = forwardThread;
//
//		}
//		else if (sensor== backward){
//			UnreliableSensor backward = new UnreliableSensor();
//			backward.setMaze(control.getMaze());
//			backward.setSensorDirection(Direction.BACKWARD);
//			Thread backwardThread = new Thread((Runnable) backward);
//			this.backward = backward;
//			this.backwardThread = backwardThread;
//		}
//
//	}
//
//
//	@Override
//	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair) throws UnsupportedOperationException {
//
//
//		if (direction == Direction.LEFT) {
//			leftThread.start();
//		}
//		if (direction == Direction.RIGHT) {
//			rightThread.start();
//
//		}
//		if (direction == Direction.FORWARD) {
//			forwardThread.start();
//
//
//		}
//		if (direction == Direction.BACKWARD) {
//			backwardThread.start();
//
//		}
//
//
//	}
//
//
//	@Override
//	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
//		if (direction == Direction.LEFT) {
//			left.stopFailureAndRepairProcess();
//		}
//		if (direction == Direction.RIGHT) {
//			right.stopFailureAndRepairProcess();
//
//		}
//		if (direction == Direction.FORWARD) {
//			forward.stopFailureAndRepairProcess();
//
//
//		}
//		if (direction == Direction.BACKWARD) {
//			backward.stopFailureAndRepairProcess();
//
//		}
//
//
//	}
//
//
//	/*
//	 * Will call distanceToObstacle from UnreliableSensor on one of the robot's sensors e.g. left or right.
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
//
//}
//
