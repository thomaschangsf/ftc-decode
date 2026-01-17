package org.ftsim.elmo;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TotalTeleOpv26", group = "TeleOp")
public class TotalTeleOpv26 extends LinearOpMode {

    private static final double MOTORRF_POWER_BOOST = 2;

    private static final double MOTORLB_POWER_REDUCTION = 0.35;

    private DcMotor motorlb;
    private DcMotor motorrb;
    private DcMotor motorfw;
    private DcMotor motorlf;
    private DcMotor motorrf;
    private Servo leftShooterServo;
    private Servo rightShooterServo;
    private HuskyLens huskyLens;

    private static final double KNOWN_TAG_SIZE_INCHES = 2.0;
    private static final double FOCAL_LENGTH_PIXELS = 500.0;

    @Override
    public void runOpMode() {
        motorlb = hardwareMap.get(DcMotor.class, "motorlb");//motor1
        motorrb = hardwareMap.get(DcMotor.class, "motorrb");//motor2
        motorfw = hardwareMap.get(DcMotor.class, "motorfw"); //motor3
        motorlf = hardwareMap.get(DcMotor.class, "motorlf");//motor4
        motorrf = hardwareMap.get(DcMotor.class, "motorrf");//motor5
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        initHuskyLens();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            double maxPower = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            double motorrbPower = Range.clip(drive - turn, -maxPower, maxPower);
            motorrb.setPower(motorrbPower);

            double motorrfPower;
            if (Math.abs(drive) > 0.01) {
                motorrfPower = Range.clip(motorrbPower + (motorrbPower >= 0 ? MOTORRF_POWER_BOOST : -MOTORRF_POWER_BOOST), -maxPower, maxPower);
            } else {
                motorrfPower = motorrbPower;
            }
            motorrf.setPower(motorrfPower);

            double motorlbPower;
            if (Math.abs(turn) > 0.01) {
                motorlbPower = Range.clip(drive + turn, -maxPower, maxPower);
            } else if (Math.abs(drive) > 0.01 && Math.abs(motorrbPower) > 0.5) {
                motorlbPower = Range.clip(motorrbPower - MOTORLB_POWER_REDUCTION, -maxPower, maxPower);
            } else {
                motorlbPower = motorrbPower;
            }
            motorlb.setPower(motorlbPower);

            double motorlfPower = Range.clip(drive + turn, -maxPower, maxPower);
            motorlf.setPower(-motorlfPower);

            if (gamepad1.right_bumper) {
                motorlb.setPower(-0.5);
                motorrf.setPower(-0.8);
                motorrb.setPower(0.8);
                motorlf.setPower(-0.5);
            }

            if (gamepad1.left_bumper) {
                motorlb.setPower(0.5);
                motorrf.setPower(0.8);
                motorrb.setPower(-0.8);
                motorlf.setPower(0.5);
            } else {
                motorlb.setPower(0);
                motorrf.setPower(0);
                motorrb.setPower(0);
                motorlf.setPower(0);
            }

            if (gamepad1.left_stick_y > 0.1 && gamepad1.square) {
                motorlb.setPower(0.5);
                motorrf.setPower(0.5);
                motorrb.setPower(0.5);
                motorlf.setPower(0.5);
            }

            if (gamepad1.left_stick_y < -0.1 && gamepad1.square) {
                motorlb.setPower(-0.5);
                motorrf.setPower(-0.5);
                motorrb.setPower(-0.5);
                motorlf.setPower(-0.5);
            }

            //orignal loop time 9 secs
            if (gamepad1.right_trigger > 0.1) { //Shooting loop
                leftShooterServo.setPosition(0);
                rightShooterServo.setPosition(1);
                motorfw.setPower(0);

                //BALL 1 1250 m;
                motorfw.setPower(-1);
                sleep(300);
                motorfw.setPower(-0.62);
                sleep(850);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(325); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 2 1025ms
                motorfw.setPower(-0.62); //12.6 volts
                sleep(850);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(300); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 3 1325 ms
                motorfw.setPower(-0.62); //12.6 volts
                sleep(900);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(325); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                sleep(650);
                motorfw.setPower(1);
                sleep(450);
                motorfw.setPower(0);
            }
            if (gamepad1.left_trigger > 0.1) {
                motorfw.setPower(-1);
                sleep(300);
                motorfw.setPower(-0.62);
                sleep(900);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(325); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                sleep(400);
                motorfw.setPower(1);
                sleep(450);
                motorfw.setPower(0);
            }

            // Update vision telemetry
            updateVisionTelemetry();
            telemetry.update();
        }
    }

    private void initHuskyLens() {
        try {
            huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");
            huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
            telemetry.addData("HuskyLens", "Initialized");
        } catch (Exception e) {
            huskyLens = null;
            telemetry.addData("HuskyLens", "Failed to initialize: " + e.getMessage());
        }
    }

    private HuskyLens.Block getTargetTag() {
        if (huskyLens == null) return null;

        HuskyLens.Block[] blocks = huskyLens.blocks();
        if (blocks.length == 0) return null;

        // Look for tags with id 3 or 6
        for (HuskyLens.Block block : blocks) {
            if (block.id == 1 || block.id == 2) {
                return block;
            }
        }
        return null;
    }

    private double calculateDistance(HuskyLens.Block block) {
        if (block == null) return -1.0;

        double averagePixelSize = (block.width + block.height) / 2.0;
        double distance = (KNOWN_TAG_SIZE_INCHES * FOCAL_LENGTH_PIXELS) / averagePixelSize;

        return distance;
    }

    private void updateVisionTelemetry() {
        HuskyLens.Block block = getTargetTag();

        if (block == null) {
            telemetry.addLine("--- AprilTag Detection ---");
            telemetry.addData("Tag ID 1 or 2", "Not detected");
            telemetry.addData("Distance", "N/A");
        } else {
            double distance = calculateDistance(block);

            telemetry.addLine("--- AprilTag Detection ---");
            telemetry.addData("Tag ID", block.id);
            telemetry.addData("Dimensions", "Width: %d px, Height: %d px", block.width, block.height);
            telemetry.addData("Position", "X: %d, Y: %d", block.x, block.y);
            telemetry.addData("Distance", "%.2f inches", distance);
        }
    }
}