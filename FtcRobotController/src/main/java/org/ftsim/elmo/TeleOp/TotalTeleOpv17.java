package org.ftsim.elmo;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TotalTeleOpv16BLUE", group = "TeleOp")
public class TotalTeleOpv16RED extends LinearOpMode {

    private static final double MIN_SHOOT_POWER = 0.3;
    private static final double MAX_SHOOT_POWER = 0.92;
    private static final double PIXELS_NEAR = 150.0;
    private static final double PIXELS_FAR = 40.0;
    private static final double DEFAULT_SHOOT_POWER = 0.6;

    private static final int REQUIRED_TAG_ID = 6;
    private static final int BLOCKED_TAG_ID = 3;
    private static final int TARGET_TAG_ID = -1;
    private static final double TURN_GAIN = 0.003;
    private static final int FRAME_CENTER_X = 160;

    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private Servo leftShooterServo;
    private Servo rightShooterServo;

    private HuskyLens huskyLens;

    @Override
    public void runOpMode() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor4 = hardwareMap.get(DcMotor.class, "motor4");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        initHuskyLens();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Shoot Tag ID", REQUIRED_TAG_ID);
        telemetry.addData("Blocked Tag ID", BLOCKED_TAG_ID);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x + getTagTurnCorrection();
            double maxPower = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            motor1.setPower(Range.clip(drive + turn, -maxPower, maxPower));
            motor2.setPower(Range.clip(drive - turn, -maxPower, maxPower));

            motor3.setPower(0);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);

            //orignal loop time 9 secs
            if (gamepad2.right_trigger > 0.1) { //Shooting loop
                // BALL 1 1400 m;
                //motor3.setPower(-1);
                //sleep(200);
                motor3.setPower(-0.65);
                sleep(1000);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(350); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 2 1025ms
                motor3.setPower(-0.66); //12.5 volts
                sleep(1250);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(300); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 3 1325 ms
                motor3.setPower(-0.67); //12.5 volts
                sleep(1250);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(325); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                sleep(1200);
                motor3.setPower(0);
                //motor3.setPower(-1);
                //sleep(100);
                //for (int i = 0; i < 3; i++) {


                // give ball to fly and flywhell to regain speed
                //sleep(400);
                //sleep(300 + i*100);
                // Too low. 200,300 --> collide or no power

                //}

            }

            //motor4.setPower(gamepad2.circle ? 1.0 : 0);

            updateVisionTelemetry();
            telemetry.update();
        }
    }

    private void initHuskyLens() {
        try {
            huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");
            huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
        } catch (Exception e) {
            huskyLens = null;
        }
    }

    private HuskyLens.Block getTrackedTag() {
        if (huskyLens == null) return null;
        HuskyLens.Block[] blocks = huskyLens.blocks();
        if (blocks.length == 0) return null;

        if (TARGET_TAG_ID >= 0) {
            for (HuskyLens.Block block : blocks) {
                if (block.id == TARGET_TAG_ID) return block;
            }
            return null;
        }
        return blocks[0];
    }

    private boolean isBlockedTagDetected() {
        if (huskyLens == null) return false;
        HuskyLens.Block[] blocks = huskyLens.blocks();
        if (blocks.length == 0) return false;

        for (HuskyLens.Block block : blocks) {
            if (block.id == BLOCKED_TAG_ID) return true;
        }
        return false;
    }

    private boolean canShoot() {
        HuskyLens.Block block = getTrackedTag();
        return block != null && block.id == REQUIRED_TAG_ID && !isBlockedTagDetected();
    }

    private double getTagTurnCorrection() {
        HuskyLens.Block block = getTrackedTag();
        if (block == null) return 0.0;
        return Range.clip((block.x - FRAME_CENTER_X) * TURN_GAIN, -0.2, 0.2);
    }

    private double getDistanceBasedShootPower() {
        HuskyLens.Block block = getTrackedTag();
        if (block == null) return DEFAULT_SHOOT_POWER;

        double ratio = (Range.clip(block.width, PIXELS_FAR, PIXELS_NEAR) - PIXELS_FAR) / (PIXELS_NEAR - PIXELS_FAR);
        return MIN_SHOOT_POWER + (1.0 - ratio) * (MAX_SHOOT_POWER - MIN_SHOOT_POWER);
    }

    private void updateVisionTelemetry() {
        HuskyLens.Block block = getTrackedTag();
        boolean blocked = isBlockedTagDetected();
        if (block == null) {
            telemetry.addData("Tag", "None");
            telemetry.addData("Shoot Power", "%.2f", DEFAULT_SHOOT_POWER);
            telemetry.addData("Can Shoot", blocked ? "No - ID " + BLOCKED_TAG_ID + " detected" : "Yes - No tag required");
        } else {
            double power = getDistanceBasedShootPower();
            double turn = getTagTurnCorrection();
            boolean canShoot = canShoot();
            telemetry.addData("Tag ID", block.id);
            telemetry.addData("Size", "%dx%d", block.width, block.height);
            telemetry.addData("Shoot Power", "%.2f", power);
            telemetry.addData("Turn Assist", "%.3f", turn);
            if (blocked) {
                telemetry.addData("Can Shoot", "No - ID " + BLOCKED_TAG_ID + " detected");
            } else {
                telemetry.addData("Can Shoot", canShoot ? "Yes (ID " + REQUIRED_TAG_ID + ")" : "Yes - No tag required");
            }
        }
    }
}

