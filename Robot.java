// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Percent;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //All Deffinitions
  private TalonSRX motorDriveLF = new TalonSRX(1);
  private TalonSRX motorDriveLB = new TalonSRX(11);

  private TalonSRX motorDriveRF = new TalonSRX(2);
  private TalonSRX motorDriveRB = new TalonSRX(12);
  

  private PS4Controller controller = new PS4Controller(0);

  private double factor = 1;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    motorDriveLF.setInverted(true);
    motorDriveLB.setInverted(true);
    motorDriveRB.setInverted(true);

    motorDriveLB.configMotionAcceleration(10000,10000);
    motorDriveLF.configMotionAcceleration(10000,10000);
    motorDriveRB.configMotionAcceleration(10000,10000);
    motorDriveRF.configMotionAcceleration(10000,10000);

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double translational = controller.getLeftY();
    double rotational = controller.getRightX();

    double left = translational - rotational;
    double right = translational + rotational;

    left *= factor;
    right *= factor;
    
    if(controller.getR1ButtonPressed() && factor < 1){
      factor += 0.25;
    }
    if(controller.getL1ButtonPressed() && factor > 0.25){
      factor -= 0.25;
    }

    if(left < -1){
      left = -1;
    }
    if(left > 1){
      left = 1;
    }

    if(right < -1){
      right = -1;
    }
    if(right > 1){
      right = 1;
    }

    motorDriveLF.set(ControlMode.PercentOutput, left);
    motorDriveLB.set(ControlMode.PercentOutput, left);

    motorDriveRF.set(ControlMode.PercentOutput, right);
    motorDriveRB.set(ControlMode.PercentOutput, right);
    System.out.println("Left: "+left);
    System.out.println("Right: "+ right);
    System.out.println("Factor: "+factor);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
    motorDriveLF.set(ControlMode.PercentOutput,0);
    motorDriveLB.set(ControlMode.PercentOutput,0);

    motorDriveRF.set(ControlMode.PercentOutput,0);
    motorDriveRB.set(ControlMode.PercentOutput,0);
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}