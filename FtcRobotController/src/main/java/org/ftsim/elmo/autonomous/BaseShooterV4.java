package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BaseShooterV4", group="Linear Opmode")
public class BaseShooterV4 extends LinearOpMode
{
    DcMotor motorlb;
    DcMotor motorrb;
    DcMotor motorfw;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motorlb = hardwareMap.get(DcMotor.class,"motorlb");
        motorrb = hardwareMap.get(DcMotor.class,"motorrb");
        motorfw = hardwareMap.get(DcMotor.class, "motorfw");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(1);
        motorfw.setPower(0);
        // BALL 1 1400 m;
        //motor3.setPower(-1);
        //sleep(200);
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(1);
        motorfw.setPower(0);

        //BALL 1
        motorfw.setPower(-1);
        sleep(275);
        motorfw.setPower(-0.63);
        sleep(800);
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(325); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        sleep(700);

        //BALL 2
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(325); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        sleep(700);

        //BALL 3
        leftShooterServo.setPosition(1); //open to shoot
        rightShooterServo.setPosition(0);
        sleep(350); //gives ball time to escape servo
        leftShooterServo.setPosition(0);//close for next shot
        rightShooterServo.setPosition(1);

        //STOP MECHANISM
        sleep(500);
        motorfw.setPower(1);
        sleep(300);
        motorfw.setPower(0);

        motorrb.setPower(-1);
        motorlb.setPower(-1);
        sleep(1000);
        motorrb.setPower(0);
        motorlb.setPower(0);

        motorfw.setPower(-0.62);
        sleep(6000);
        motorfw.setPower(0);
    }
}
