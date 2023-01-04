package frc.robot;

import edu.wpi.first.wpilibj.*;
//import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// import edu.wpi.first.networktables.NetworkTable;
// import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj.Servo;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  
  private XboxController xbox = new XboxController(0); // xbox controller support

  PWMTalonSRX m_left = new PWMTalonSRX(1);
  PWMTalonSRX m_right = new PWMTalonSRX(0);
  PWMSparkMax spark1 = new PWMSparkMax(6); // sweep
  PWMSparkMax spark2 = new PWMSparkMax(4); // lift
  Servo servo = new Servo(3); 
  DifferentialDrive m_drive = new DifferentialDrive(m_right, m_left);
  int x = 0;

   // start up code
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    System.out.println("starting up!");
  }

  // this is called periodicly no matter the mode
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
      double tv = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0); //taget 0/1
      double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0); // horizontal offset from crosshair to target
      double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0); // vertical offset
      double ta = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0); //targer area 0-100
      double targetOffsetAngle_Vertical = ty;

// how many degrees back is your limelight rotated from perfectly vertical?
double limelightMountAngleDegrees = 90.0;

// distance from the center of the Limelight lens to the floor
double limelightLensHeightInches = 6.4;

// distance from the target to the floor
double goalHeightInches = 6.4;

double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

//calculate distance
double distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches)/Math.tan(angleToGoalRadians);
System.out.println(distanceFromLimelightToGoalInches);

      
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(3); //wall
        if(distanceFromLimelightToGoalInches<5){
          m_drive.tankDrive(-.5, .6);
        }
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(1); //gear
        if (tv == 1 && ta == 25){ // if you see a ball and it is 25 percent of screen 
          if(tx>0 && tx<4){ // if ball is centered enough 
            if(ty<20){ // if ball is down low on the camera
              m_drive.tankDrive(-.5, .6);
            }
          }
          }
          else{
            m_drive.tankDrive(.5, .5);
          }
        x++;
        if(x==500){
          m_drive.tankDrive(-1, -.5);
          Timer.delay(2);
        }
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
       
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

   // double LJoystickX = xbox.getLeftX();
    double LJoystickY = xbox.getLeftY();
    // ^^ left joystick inputs
    // double RJoystickx = xbox.getRightX();
    double RJoystickY = xbox.getRightY(); 
    // ^^ right joystick inputs
    // double L_trigger = xbox.getLeftTriggerAxis();
    // double R_trigger = xbox.getRightTriggerAxis();
    // ^^ trigger inputs
    boolean L_bumper = xbox.getLeftBumper();
    boolean R_bumper = xbox.getRightBumper();
    // ^^ bumper inputs
    boolean A_Button = xbox.getRawButton(1);
    boolean B_Button = xbox.getRawButton(2);
    boolean X_Button = xbox.getRawButton(3);
    boolean Y_Button = xbox.getRawButton(4);
    double Arrow = xbox.getPOV();  
    // ^^ button inputs
    boolean L_Joystick = xbox.getLeftStickButton();
    boolean R_Joystick = xbox.getRightStickButton();
    // ^^ joystick down input
    RJoystickY = RJoystickY * -1;
    if(Arrow == 0){
      spark2.set(.5);
    }
    else if(Arrow == 180){
      spark2.set(-.5);
    }
    else if(Arrow == 90 || Arrow == 270){
      spark2.set(0);
    }
    else if(B_Button){
      spark1.set(.5);
    }
    else if(X_Button){      
      spark1.set(0);
    }
    if(A_Button){
      System.out.println("mama");
      servo.setAngle(180);
     System.out.println(servo.getAngle());
     servo.set(1);
    }
    else if(Y_Button){
      System.out.println("joe");
      servo.setAngle(0);
      servo.set(0);
    }
    else if(R_Joystick || L_Joystick){ 
      m_drive.tankDrive(LJoystickY/2, RJoystickY/2);
}    
    else if (L_bumper){
      m_drive.tankDrive(-1, 1);
    }
    else if(R_bumper){
      m_drive.tankDrive(0, 0);
    }
    else{
      m_drive.tankDrive(LJoystickY, RJoystickY);
    }
   
   
   
  }


  /*
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
//6.4in
  }
}