package org.ftsim;

// Using Qualcomm SDK version 11.0.0, defined in build.dependencies.gradle
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

// Code was inspired from ftcscim.org: https://ftcsim.org/course/iuhjsrjteoc/
@TeleOp(name = "Field1", group = "ftsim")
public class Field1 extends LinearOpMode {
    DcMotor driveLeft;
    DcMotor driveRight;
    DcMotor shootwheel;
    DcMotor backLeftDrive;
    DcMotor backRightDrive;
    DcMotor frontLeftDrive;
    DcMotor frontRightDrive;
    Servo artifactstopper;
    ColorSensor color1;
    ColorSensor color2;
    DistanceSensor distance1;
    BNO055IMU imu;
    
    // Additional motor variables for test method
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor frontLeft;
    DcMotor frontRight;

    // Properly declared variables with correct types
    int duration;
    VisionPortal.Builder myVisionPortalBuilder;
    double forward;
    int nArtifacts;
    double turn;
    List<AprilTagDetection> myAprilTagDetections;
    VisionPortal myVisionPortal;
    boolean isShooting;
    AprilTagDetection myAprilTagDetection;
    double shootPower;
    int mode;
    double maxDrivePower;
    double strafe;

    //https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html
    AprilTagProcessor myApriltagProcessor;
    AprilTagProcessor.Builder myAprilTagProcessorBuilder;

    // Describe this function...
    public void inititalSetup(){
        // Put initialization blocks here
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        isShooting = false;
        // Holds back artifacts until we start shooting
        artifactstopper.setPosition(0.2);
    }

    // Describe this function...
    public void initializeVisionPortal(){
        myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
        myApriltagProcessor = myAprilTagProcessorBuilder.build();
        
        myVisionPortalBuilder = new VisionPortal.Builder();
        myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, "webcam"));
        myVisionPortalBuilder.addProcessor(myApriltagProcessor);
        myVisionPortal = myVisionPortalBuilder.build();
    }

    // Describe this function...
    public void pickMode(){
        if (mode == 0) {
            keyboardDrive();
        } else if (mode == 1) {
            gamepadDrive();
        } else if (mode == 2) {
            autoDrive();
        }
    }

    // Describe this function...
    public void keyboardDrive(){
        while (opModeIsActive()) {
            // Note: keyboard class is not available in FTC SDK - this would need to be implemented differently
            // turn = keyboard.isPressed(108) - keyboard.isPressed(106);
            // forward = keyboard.isPressed(105) - keyboard.isPressed(107);
            // strafe = keyboard.isPressed(111) - keyboard.isPressed(117);
            
            // Using gamepad instead for now
            turn = gamepad1.right_stick_x;
            forward = gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            
            processDriveInputs();
            if (gamepad1.a && !isShooting) {
                shoot();
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void gamepadDrive(){
        while (opModeIsActive()) {
            turn = gamepad1.right_stick_x;
            forward = gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            processDriveInputs();
            if (gamepad1.a && !isShooting) {
                shoot();
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void autoDrive(){
        driveToGoal();
        shootThreeArtifacts();
        driveToPlayerStationAndBack();
        shootThreeArtifacts();
        // After finishing autonomous, we fall back to drive
        keyboardDrive();
    }

    // Describe this function...
    public void driveToGoal(){
        forward = 1;
        processInputsAndSleep(2300);
        turn = -1;
        processInputsAndSleep(220);
        sleep(500);
    }

    // Describe this function...
    public void driveToPlayerStationAndBack(){
        forward = -1;
        processInputsAndSleep(2800);
        sleep(10000);
        forward = 1;
        processInputsAndSleep(2800);
        sleep(500);
    }

    // Describe this function...
    public void shootThreeArtifacts(){
        nArtifacts = 3;
        while (opModeIsActive() && nArtifacts > 0) {
            if (!isShooting) {
                shoot();
                nArtifacts -= 1;
            }
            displayVisionPortalData();
        }
    }

    // Describe this function...
    public void processInputsAndSleep(int duration){
        // This helper function makes the code a bit cleaner
        processDriveInputs();
        sleep(duration);
        // Stop all movement after sleep
        forward = 0;
        turn = 0;
        strafe = 0;
        processDriveInputs();
    }

    // Describe this function...
    public void processDriveInputs(){
        turn = turn * maxDrivePower;
        forward = forward * maxDrivePower;
        strafe = strafe * maxDrivePower;
        // Combine inputs to create drive and turn (or both!)
        frontLeftDrive.setPower(forward + turn + strafe);
        frontRightDrive.setPower(forward - turn - strafe);
        backLeftDrive.setPower(forward + turn - strafe);
        backRightDrive.setPower(forward - turn + strafe);
    }

    // Describe this function...
    public void shoot(){
        // Don't move while shooting
        isShooting = true;
        // Let one artifact come through
        artifactstopper.setPosition(0);
        shootwheel.setPower(shootPower);
        sleep(250);
        // Stop the next artifact
        artifactstopper.setPosition(0.2);
        sleep(200);
        shootwheel.setPower(0);
        sleep(1500);
        // Allow for a new shot to be triggered
        isShooting = false;
    }

    public void test() {
        motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
        motorRight = hardwareMap.get(DcMotor.class, "motorRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        color2 = hardwareMap.get(ColorSensor.class, "color2");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        // Put initialization blocks here
        waitForStart();

        NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");


    }

    // Describe this function...
    public void displayVisionPortalData(){
        myAprilTagDetections = (myApriltagProcessor.getDetections());
        // TWC: refer to RobotAutoDriveToAprilTagTank.java and *Omni to see how to use this
        // Omni are for robots that have 4 wheels, while Tank are for robots with 2 wheels
        for (AprilTagDetection myAprilTagDetection2 : myAprilTagDetections) {
            //https://ftc-docs.firstinspires.org/en/latest/apriltag/understanding_apriltag_detection_values/understanding-apriltag-detection-values.html#understanding-apriltag-detection-values
            myAprilTagDetection = myAprilTagDetection2;
            telemetry.addData("ID", (myAprilTagDetection.id));
            //Roll is the measure of rotation about the Y axis
            telemetry.addData("Range", (myAprilTagDetection.ftcPose.range));
            //Heading, or Yaw, is the measure of rotation about the Z axis
            telemetry.addData("Yaw", (myAprilTagDetection.ftcPose.yaw));
            //Pitch is the measure of rotation about the X axis
            telemetry.addData("Pitch", (myAprilTagDetection.ftcPose.pitch));
            telemetry.addData("X", (myAprilTagDetection.ftcPose.x));
            telemetry.addData("Y", (myAprilTagDetection.ftcPose.y));
            telemetry.addData("Z", (myAprilTagDetection.ftcPose.z));
        }
        telemetry.update();
    }


    @Override
    public void runOpMode() {
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        inititalSetup();
        initializeVisionPortal();
        shootPower = 0.8;
        maxDrivePower = 1;
        // mode 0 = keyboard, 1 = gamepad, 2 = autonomous
        mode = 2;
        waitForStart();
        pickMode();
    }

}
