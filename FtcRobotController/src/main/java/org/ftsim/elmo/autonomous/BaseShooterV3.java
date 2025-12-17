package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BaseShooterV3", group="Linear Opmode")
public class BaseShooterV3 extends LinearOpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motor1 = hardwareMap.get(DcMotor.class,"motor1");
        motor2 = hardwareMap.get(DcMotor.class,"motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(1);
        motor3.setPower(0);
        // BALL 1 1400 m;
        //motor3.setPower(-1);
        //sleep(200);
        motor3.setPower(-0.68);
        sleep(900);
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(350); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        // BALL 2 1025ms
        motor3.setPower(-0.67); //12.6 volts
        sleep(800);
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(300); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        // BALL 3 1325 ms
        motor3.setPower(-0.67); //12.6 volts
        sleep(675);
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(325); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        sleep(1200);
        motor3.setPower(0);
        //motor3.setPower(-1);
        //sleep(100);
        //To much power, shoots to high, to little, shoots to horizontal


        // give ball to fly and flywhell to regain speed
        //sleep(400);
        //sleep(300 + i*100);
        // Too low. 200,300 --> collide or no power

        //}
    }
}
