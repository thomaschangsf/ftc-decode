package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="servo")
public class Shoot extends LinearOpMode {

    public Servo leftShooterServo;
    public Servo rightShooterServo;
    ElapsedTime timer;

    @Override
    public void runOpMode(){
        //public Servo leftShooterServo;
        //public Servo rightShooterServo;
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");


        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        if(gamepad2.left_bumper) {
            leftShooterServo.setPosition(1);
            sleep(600);
            leftShooterServo.setPosition(0);
        }
        if(gamepad2.right_bumper) {
            rightShooterServo.setPosition(1);
            sleep(600);
            rightShooterServo.setPosition(0);
        }
    }
}
