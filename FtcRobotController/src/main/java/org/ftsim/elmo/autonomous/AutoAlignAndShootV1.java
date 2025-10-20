package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@Autonomous(name="Robust Align & Shoot", group="ftsim")
public class RobustAlignAndShoot extends LinearOpMode {
    // Hardware
    DcMotor leftDrive, rightDrive, shootwheel;
    Servo artifactstopper;
    
    // AprilTag
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;
    
    // Constants
    final int TARGET_TAG = 1;
    final double SHOOT_DISTANCE = 18.0;
    final double SPEED_GAIN = 0.02;
    final double TURN_GAIN = 0.01;
    
    // Timeouts (in milliseconds)
    final int MAX_ALIGN_TIME = 15000;  // 15 seconds max to align
    final int MAX_SEARCH_TIME = 5000;  // 5 seconds max to find tag
    
    @Override
    public void runOpMode() {
        // Initialize
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        
        initAprilTag();
        artifactstopper.setPosition(0.2);
        
        waitForStart();
        
        // Robust sequence
        if (alignAndShoot()) {
            telemetry.addData("Result", "SUCCESS - Autonomous completed");
        } else {
            telemetry.addData("Result", "FAILED - Could not complete mission");
        }
        telemetry.update();
        
        // Always stop motors
        stopAllMotors();
    }
    
    public boolean alignAndShoot() {
        long startTime = System.currentTimeMillis();
        
        // Step 1: Find and align (with timeout)
        while (opModeIsActive() && (System.currentTimeMillis() - startTime) < MAX_ALIGN_TIME) {
            AprilTagDetection tag = findTarget();
            
            if (tag != null) {
                telemetry.addData("Status", "Tag found - aligning...");
                telemetry.addData("Distance", "%.1f", tag.ftcPose.range);
                telemetry.addData("Bearing", "%.1f", tag.ftcPose.bearing);
                
                if (isReadyToShoot(tag)) {
                    telemetry.addData("Status", "ALIGNED - Ready to shoot!");
                    break; // Success!
                }
                
                alignToTag(tag);
            } else {
                telemetry.addData("Status", "Searching for tag...");
                // Search strategy: move slowly in a pattern
                searchForTag();
            }
            
            telemetry.update();
            sleep(50);
        }
        
        // Check if we succeeded
        if ((System.currentTimeMillis() - startTime) >= MAX_ALIGN_TIME) {
            telemetry.addData("Error", "Timeout - could not align in time");
            return false;
        }
        
        // Step 2: Shoot with error checking
        return shootSequence();
    }
    
    public AprilTagDetection findTarget() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null && detection.id == TARGET_TAG) {
                return detection;
            }
        }
        return null;
    }
    
    public void searchForTag() {
        // Simple search pattern: small movements
        moveRobot(0.1, 0.1); // Move forward and turn slightly
        sleep(100);
        moveRobot(0, 0); // Stop
    }
    
    public boolean isReadyToShoot(AprilTagDetection tag) {
        double distanceError = Math.abs(tag.ftcPose.range - SHOOT_DISTANCE);
        double bearingError = Math.abs(tag.ftcPose.bearing);
        
        return distanceError < 2.0 && bearingError < 5.0;
    }
    
    public void alignToTag(AprilTagDetection tag) {
        double distanceError = tag.ftcPose.range - SHOOT_DISTANCE;
        double bearingError = tag.ftcPose.bearing;
        
        double drive = Range.clip(distanceError * SPEED_GAIN, -0.5, 0.5);
        double turn = Range.clip(bearingError * TURN_GAIN, -0.3, 0.3);
        
        moveRobot(drive, turn);
    }
    
    public boolean shootSequence() {
        int successfulShots = 0;
        
        for (int i = 0; i < 3; i++) {
            if (!opModeIsActive()) break;
            
            telemetry.addData("Shooting", "Ball %d of 3", i + 1);
            telemetry.update();
            
            if (shoot()) {
                successfulShots++;
            }
            
            sleep(1000);
        }
        
        telemetry.addData("Shots", "Successful: %d/3", successfulShots);
        return successfulShots >= 2; // Success if at least 2 shots work
    }
    
    public boolean shoot() {
        try {
            artifactstopper.setPosition(0.0);
            shootwheel.setPower(0.8);
            sleep(250);
            
            artifactstopper.setPosition(0.2);
            sleep(200);
            
            shootwheel.setPower(0);
            sleep(1500);
            
            return true; // Shot completed
        } catch (Exception e) {
            telemetry.addData("Shoot Error", e.getMessage());
            return false; // Shot failed
        }
    }
    
    public void moveRobot(double drive, double turn) {
        double leftPower = drive - turn;
        double rightPower = drive + turn;
        
        double max = Math.max(Math.abs(leftPower), Math.abs(rightPower));
        if (max > 1.0) {
            leftPower /= max;
            rightPower /= max;
        }
        
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }
    
    public void stopAllMotors() {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        shootwheel.setPower(0);
    }
    
    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().build();
        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();
    }
}