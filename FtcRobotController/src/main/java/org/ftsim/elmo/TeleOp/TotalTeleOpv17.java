package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TotalTeleOpv17", group = "TeleOp")
public class TotalTeleOpv17 extends LinearOpMode {

    private static final double MIN_SHOOT_POWER = 0.3;
    private static final double MAX_SHOOT_POWER = 0.92;
    private static final double DEFAULT_SHOOT_POWER = 0.6;

    private DcMotor motor1;
    private DcMotor motor2;
    private DcMotor motor3;
    private DcMotor motor4;
    private Servo leftShooterServo;
    private Servo rightShooterServo;

    @Override
    public void runOpMode() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor4 = hardwareMap.get(DcMotor.class, "motor4");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            double maxPower = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            motor1.setPower(Range.clip(drive + turn, -maxPower, maxPower));
            motor2.setPower(Range.clip(drive - turn, -maxPower, maxPower));

            motor3.setPower(0);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);

            //orignal loop time 9 secs
            if (gamepad2.right_trigger > 0.1) { //Shooting loop
                // BALL 1 1350ms;
                //motor3.setPower(-1);
                //sleep(200);
                motor3.setPower(-0.65);
                sleep(1000);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(350); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 2 1550ms
                motor3.setPower(-0.66); //12.5 volts
                sleep(1250);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(300); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                // BALL 3 1575 ms
                motor3.setPower(-0.67); //12.5 volts
                sleep(1250);
                leftShooterServo.setPosition(1); //open to shoot
                rightShooterServo.setPosition(0);
                sleep(325); //gives ball time to escape servo
                leftShooterServo.setPosition(0);//close for next shot
                rightShooterServo.setPosition(1);

                sleep(1200);
                motor3.setPower(0);
                //total time 4475ms
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

            telemetry.update();
        }
    }
}

