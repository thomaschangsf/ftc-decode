package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

@TeleOp(name="TotalTeleOpv11", group="TeleOp")
public class TotalTeleOpv11 extends LinearOpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    Servo leftShooterServo;
    Servo rightShooterServo;

    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;

    private static final double MIN_SHOOT_POWER = 0.3;
    private static final double MAX_SHOOT_POWER = 0.9;
    private static final double MIN_DISTANCE = 12.0;
    private static final double MAX_DISTANCE = 48.0;
    private static final double DEFAULT_SHOOT_POWER = 0.69;

    private static final int TARGET_TAG_ID = -1;

    @Override
    public void runOpMode() {

        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor4 = hardwareMap.get(DcMotor.class, "motor4");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        initAprilTag();

        telemetry.addData("Status", "Initialized and Ready");
        telemetry.addData("AprilTag", "Detection enabled");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);

        while(opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            double leftPower  = drive + turn;
            double rightPower = drive - turn;
            if(gamepad1.right_trigger > 0.1){
                leftPower = Range.clip(leftPower, -0.5, 0.5);
                rightPower = Range.clip(rightPower, -0.5, 0.5);
            }
            else{
                leftPower = Range.clip(leftPower, -1.0, 1.0);
                rightPower = Range.clip(rightPower, -1.0, 1.0);
            }
            motor1.setPower(leftPower);
            motor2.setPower(rightPower);

            motor3.setPower(0);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);

            if(gamepad2.right_trigger > 0.1) {
                motor3.setPower(-getDistanceBasedShootPower());
            }

            if(gamepad2.right_bumper){
                double shootPower = getDistanceBasedShootPower();
                leftShooterServo.setPosition(-1);
                rightShooterServo.setPosition(1);
                motor3.setPower(-shootPower);
                leftShooterServo.setPosition(0);
                rightShooterServo.setPosition(0);
            }

            if(gamepad2.left_trigger > 0.1) {
                leftShooterServo.setPosition(1);
                rightShooterServo.setPosition(0);
                sleep(300);
                leftShooterServo.setPosition(0);
                rightShooterServo.setPosition(0);
            }

            if(gamepad2.circle){
                motor4.setPower(1.5);
            }

            else{
                motor3.setPower(0);
                motor4.setPower(0);
            }

            updateAprilTagTelemetry();
            telemetry.update();
        }

        visionPortal.close();
    }

    private void initAprilTag() {
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        try {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    hardwareMap.get(WebcamName.class, "Webcam 1"), aprilTag);
        } catch (Exception e) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection.BACK, aprilTag);
        }
    }

    private double getAprilTagDistance() {
        for (AprilTagDetection detection : aprilTag.getDetections()) {
            if (detection.metadata != null &&
                    (TARGET_TAG_ID < 0 || detection.id == TARGET_TAG_ID)) {
                return detection.ftcPose.range;
            }
        }
        return -1;
    }

    private double getDistanceBasedShootPower() {
        double distance = getAprilTagDistance();
        if (distance < 0) return DEFAULT_SHOOT_POWER;

        distance = Range.clip(distance, MIN_DISTANCE, MAX_DISTANCE);
        double ratio = (distance - MIN_DISTANCE) / (MAX_DISTANCE - MIN_DISTANCE);
        return Range.clip(MAX_SHOOT_POWER - ratio * (MAX_SHOOT_POWER - MIN_SHOOT_POWER),
                MIN_SHOOT_POWER, MAX_SHOOT_POWER);
    }

    private void updateAprilTagTelemetry() {
        double distance = getAprilTagDistance();
        double power = getDistanceBasedShootPower();

        telemetry.addData("Tag Distance", distance >= 0 ? String.format("%.1f\"", distance) : "None");
        telemetry.addData("Shoot Power", "%.2f", power);
        telemetry.addData("Tags", aprilTag.getDetections().size());
    }
}

