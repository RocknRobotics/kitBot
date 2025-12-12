package frc.robot;

import java.util.GregorianCalendar;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PS4Controller;

public class drive {
    private TalonSRX motorDriveLF = new TalonSRX(1);
    private TalonSRX motorDriveLB = new TalonSRX(11);

    private TalonSRX motorDriveRF = new TalonSRX(2);
    private TalonSRX motorDriveRB = new TalonSRX(12);

    private PS4Controller controller = new PS4Controller(0);

    private double factor = 1;

    public drive() {
    }

    /*
    Sets all motors to rotate in the same direction forward
    */
    public void setUp() {
        motorDriveLF.setInverted(true);
        motorDriveLB.setInverted(true);
        motorDriveRB.setInverted(true);
    }

    public void tearDown() {
        motorDriveLF.set(ControlMode.PercentOutput, 0);
        motorDriveLB.set(ControlMode.PercentOutput, 0);

        motorDriveRF.set(ControlMode.PercentOutput, 0);
        motorDriveRB.set(ControlMode.PercentOutput, 0);
    }

    /*
    Handles basic drive functions
    */
    public void drivePeriodic() {
        double translational = controller.getLeftY();
        double rotational = controller.getRightX();

        double left = translational - rotational;
        double right = translational + rotational;

        gearShift();  // Affects the max speed baed off current gear

        if (left < -1) {
            left = -1;
        }
        if (left > 1) {
            left = 1;
        }

        if (right < -1) {
            right = -1;
        }
        if (right > 1) {
            right = 1;
        }

        /*
        limits speed by gear limit
        */
        left *= factor;
        right *= factor;

        /*
        Calculates acceleration
        */
        left = calculate(left);
        right = calculate(right);
        
        motorDriveLF.set(ControlMode.PercentOutput, left);
        motorDriveLB.set(ControlMode.PercentOutput, left);

        motorDriveRF.set(ControlMode.PercentOutput, right);
        motorDriveRB.set(ControlMode.PercentOutput, right);

        System.out.println("Left: " + left);
        System.out.println("Right: " + right);
        System.out.println("Factor: " + factor);
    }

    public void gearShift(){
        if(controller.getR1ButtonPressed() && factor < 1){
            factor += 0.25;
          }
          if(controller.getL1ButtonPressed() && factor > 0.25){
            factor -= 0.25;
          }
    }


//ACCEL CODE
    private double prev = 0;
    private double maxAccel = 0.05;

    public double calculate(double desiredOutput){
        double velChange = desiredOutput - prev;  // Calculates the net change in velocity

        if(Math.abs(velChange) > maxAccel){
            velChange = Math.copySign(maxAccel, velChange);  // Determines if you want to increse or decrease vlocity
        }

        double newOutput = prev + velChange;  // Changes velocity

        prev = newOutput;

        return newOutput;
    }
}
