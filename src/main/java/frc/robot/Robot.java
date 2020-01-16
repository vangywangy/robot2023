/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.*;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	//private PWMTalonSRX m_leftA = new PWMTalonSRX(0);
	//private PWMTalonSRX m_leftB = new PWMTalonSRX(1);
	//private PWMTalonSRX m_rightA = new PWMTalonSRX(3);
  //private PWMTalonSRX m_rightB = new PWMTalonSRX(2);
  // anthony testing stuff
  //private PWMTalonSRX teethshifting = new PWMTalonSRX(6);
  //private PWMTalonSRX wrist = new PWMTalonSRX(4);
  
  
  //private Joystick whiteR = new Joystick(0);
  private XboxController xbox = new XboxController(0);

  //private SpeedControllerGroup m_left = new SpeedControllerGroup(m_leftA, m_leftB);
	//private SpeedControllerGroup m_right = new SpeedControllerGroup(m_rightA, m_rightB);

  private PWMTalonSRX frontLeft = new PWMTalonSRX(0);
  private PWMTalonSRX backLeft = new PWMTalonSRX(1);
  private PWMTalonSRX frontRight = new PWMTalonSRX(3);
  private PWMTalonSRX backRight = new PWMTalonSRX(2);

  //private DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
  private MecanumDrive mecanumDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

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
  }

  /**
   * This function is called periodically during operator control.
   */
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
    double zRotation = 0;
    if (xbox.getBumper(Hand.kRight))
      zRotation = 0.5;
    else if (xbox.getBumper(Hand.kLeft))
      zRotation = -0.5;
    else
      zRotation = 0;


    mecanumDrive.driveCartesian(m_stickY * -1, m_stickX, zRotation);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
