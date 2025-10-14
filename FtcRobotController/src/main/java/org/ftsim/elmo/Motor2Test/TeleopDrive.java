package org.ftsim.elmo.Motor2Test;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="motor1")
public class Drive extends OpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    Servo leftShooterServo;
    Servo rightShooterServo;


    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");


        motor1.setPower(0);
        motor2.setPower(0);
    }
    @Override
    public void loop(){
        motor3.setPower(-1);
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        if(gamepad1.circle) {
            leftShooterServo.setPosition(1);
            rightShooterServo.setPosition(1);
            motor3.setPower(-1);
            sleep(600);
            motor3.setPower(0);
        }
        if(gamepad1.x){
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);
        }
        if (gamepad1.left_trigger > 0.1) {
            motor1.setPower(-1);
        }
        else if (gamepad1.left_bumper) {
            motor1.setPower(1);
        } else {
            motor1.setPower(0);
        }

        if (gamepad1.right_trigger > 0.1) {
            motor2.setPower(-1);
        } else if (gamepad1.right_bumper) {
            motor2.setPower(1);
        } else {
            motor2.setPower(0);
        }

    }
}