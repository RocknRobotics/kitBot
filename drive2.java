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

    public void drivePeriodic() {
        double translational = controller.getLeftY();
        double rotational = controller.getRightX();

        double left = translational - rotational;
        double right = translational + rotational;

        gearShift();
        
        left *= factor;
        right *= factor;

        left = calculate(left);
        right = calculate(right);

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
        double difference = desiredOutput - prev;

        if(Math.abs(difference) > maxAccel){
            difference = Math.copySign(maxAccel, difference);
        }

        double newOutput = prev + difference;

        prev = newOutput;

        return newOutput;
    }

}