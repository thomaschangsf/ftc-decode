package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="LinearDriveTest")
public class TotalTeleOpv2 extends LinearOpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);

        while(opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            double leftPower  = drive + turn;
            double rightPower = drive - turn;

            leftPower  = Range.clip(leftPower, -1.0, 1.0);
            rightPower = Range.clip(rightPower, -1.0, 1.0);

            motor1.setPower(leftPower);
            motor2.setPower(rightPower);

            motor3.setPower(0);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);


            if(gamepad1.right_trigger > 0.1) {
                motor3.setPower(-0.68);
            }

            if(gamepad1.left_trigger > 0.1) {
                leftShooterServo.setPosition(1);
                rightShooterServo.setPosition(0);
                sleep(1000);
                leftShooterServo.setPosition(0);
                rightShooterServo.setPosition(0);
            }
        }
    }
}
