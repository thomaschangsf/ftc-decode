package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="total")
public class OriginalDrive extends OpMode
{
    DcMotor motor1;
    DcMotor motor2;
    Servo leftShooterServo;
    Servo rightShooterServo;


    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");


        motor1.setPower(0);
        motor2.setPower(0);
    }
    @Override
    public void loop(){
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        if(gamepad1.circleWasPressed()) {
            leftShooterServo.setPosition(1);
            rightShooterServo.setPosition(1);

            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);
        }
        if (gamepad1.left_trigger > 0.1) {
            motor1.setPower(1);
        } else if (gamepad1.left_bumper) {
            motor1.setPower(-1);
        } else {
            motor1.setPower(0);
        }

        if (gamepad1.right_trigger > 0.1) {
            motor2.setPower(1);
        } else if (gamepad1.right_bumper) {
            motor2.setPower(-1);
        } else {
            motor2.setPower(0);
        }

    }
}