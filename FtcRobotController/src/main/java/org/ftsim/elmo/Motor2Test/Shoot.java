package org.ftsim.elmo.Motor2Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="servo")
public class Shoot extends LinearOpMode {

    public Servo servo;
    ElapsedTime timer;

    @Override
    public void runOpMode(){
        servo = hardwareMap.servo.get("servo1");
        timer = new ElapsedTime();

        waitForStart();

        while(opModeIsActive()){
            if(timer.seconds() < 2.0) {
                servo.setPosition(0);
            }
            else if(timer.seconds() < 4.0) {
                servo.setPosition(1);
            }
            else {
                timer.reset();
            }
        }
    }
}
