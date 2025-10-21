package org.ftsim.elmo;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


@Autonomous(name="servo")
public class TestShoot extends LinearOpMode {


    public Servo leftShooterServo;
    public Servo rightShooterServo;


    @Override
    public void runOpMode(){
        //public Servo leftShooterServo;
        //public Servo rightShooterServo;
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");




        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        if(gamepad1.circleWasPressed()) {
            leftShooterServo.setPosition(1);
            rightShooterServo.setPosition(1);
            sleep(600);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);
        }


    }
}
