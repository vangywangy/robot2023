/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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

	private PWMTalonSRX m_leftA = new PWMTalonSRX(0);
	private PWMTalonSRX m_leftB = new PWMTalonSRX(1);
	private PWMTalonSRX m_rightA = new PWMTalonSRX(3);
  private PWMTalonSRX m_rightB = new PWMTalonSRX(2);
  // anthony testing stuff
  private PWMTalonSRX teethshifting = new PWMTalonSRX(6);
  private PWMTalonSRX wrist = new PWMTalonSRX(4);
  
  
  //private Joystick whiteR = new Joystick(0);
  private XboxController xbox = new XboxController(0);

  private SpeedControllerGroup m_left = new SpeedControllerGroup(m_leftA, m_leftB);
	private SpeedControllerGroup m_right = new SpeedControllerGroup(m_rightA, m_rightB);

  private DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);

  private double limitedTrigger = 0.5;
  private double limitedTurn = 0;

  private double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
  private double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
  private double turnDir = 1;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
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
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:

        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
       
        break;
    }
      NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
      NetworkTableEntry tx = table.getEntry("tx");
      NetworkTableEntry ty = table.getEntry("ty");
      //NetworkTableEntry ta = table.getEntry("ta");
      NetworkTableEntry tv = table.getEntry("tv");
      double speed = 0.5;
      double threshold = 5;
      double x = tx.getDouble(0.0);
      double y = ty.getDouble(0.0);
      System.out.printf("x: %f, y: %f%n", x, y);
      double targetstatus = tv.getDouble(0.0);
      if(targetstatus == 0.0){
        m_drive.arcadeDrive(0, speed * turnDir);
      }
      else{	
        if(x > threshold) {
          m_drive.arcadeDrive(0, speed);
          turnDir = 1;
          } 
          else if (x < -1 * threshold) {
            m_drive.arcadeDrive(0, -1 * speed);
            turnDir = -1;
          	}
      }
      //double area = ta.getDouble(0.0);
      
    
    




  }

  /**
   * This function is called periodically during operator control.
   */
  public void daboat(){
    //anthony's coding starts from here (note that i set up teethshifting above)
    // awesome thing controls the green ball shooty thing (update: it is impossible to shoot the ball with this)
    if(xbox.getRawButton(6)){
      teethshifting.set(1);
    }
    
    else if(xbox.getRawButton(4)){
      teethshifting.set(-1);
    }
    else{
      teethshifting.set(0);
    }
    //wrist is the tiny thingy tha ontrols the angle of the ball box thing
    if(xbox.getRawButton(1))
			wrist.set(0.50);
		else if(xbox.getRawButton(2))
			wrist.set(-0.35);
		else
			wrist.set(0);
  }
  @Override
  public void teleopPeriodic() {
    //double m_stickX = whiteR.getX();
		//double m_stickY = whiteR.getY();
    //double m_stickZ = whiteR.getZ();

    double m_stickX = xbox.getX(Hand.kLeft);
    //double m_stickY = xbox.getY(Hand.kLeft);

    double l_trigger = xbox.getTriggerAxis(Hand.kLeft) * -1;
    double r_trigger = xbox.getTriggerAxis(Hand.kRight);
    double m_stickY = l_trigger + r_trigger;

    // anthony attempts to use the kRight
    //double s_stickX = xbox.getX(Hand.kRight);
    //double s_stickY = xbox.getY(Hand.kRight);

    if(m_stickX < 0.1 && m_stickX > -0.1){
      m_stickX = 0;
    }
    if(m_stickY < 0.01 && m_stickY > -0.01){
      m_stickY = 0;
    } 
    
    if(m_stickX > 0.75)
      m_stickX = 0.75;
    else if (m_stickX < -0.75)
      m_stickX = -0.75;
    
    //brake button lol
    if(xbox.getRawButton(3))
      m_stickY = 0;
    
		double triggerChange = m_stickY - limitedTrigger;
    double triggerLimit = 0.025;

    double turnChange = m_stickX - limitedTurn;
    double turnLimit = 0.025;

    // so the robot doesnt flip over if we stop all of a sudden
		if (triggerChange > triggerLimit)
			triggerChange = triggerLimit;
		
		else if(triggerChange < triggerLimit * -1)
      triggerChange = triggerLimit * -1;
 
    if (turnChange > turnLimit)
      turnChange = turnLimit;
		
		else if(turnChange < turnLimit * -1)
      turnChange = turnLimit * -1;

    limitedTrigger += triggerChange;
    limitedTurn += turnChange;
    
    //for xbox
    limitedTrigger = m_stickY * -1;
    double mulitiplier = 0;
    if(xbox.getBumper(Hand.kRight))
      mulitiplier = 0.5;
    if(xbox.getBumper(Hand.kLeft))
      mulitiplier = -0.5;
    
    m_drive.arcadeDrive(m_stickY, m_stickX + mulitiplier);
    //daboat();

   
    //ends here*/
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
