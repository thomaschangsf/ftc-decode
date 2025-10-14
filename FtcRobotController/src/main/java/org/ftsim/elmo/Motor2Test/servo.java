package org.ftsim.elmo.;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="servo", group="elmo")
public class servo extends LinearOpMode {

    public Servo servo;
    ElapsedTime timer;

    @Override
    public void runOpMode(){ // Corrected: "runOpMode" with a capital 'M'
        servo = hardwareMap.servo.get("servo1");
        timer = new ElapsedTime();

        waitForStart();
        // The timer automatically starts at 0, so reset() here is optional

        while(opModeIsActive()){

            if(timer.seconds() < 2.0) {
                // In the first 2 seconds, set position to 0
                servo.setPosition(0);
            }
            else if(timer.seconds() < 4.0) {
                // Between 2 and 4 seconds, set position to 1
                servo.setPosition(1);
            }
            else {
                // After 4 seconds, reset the timer to start the cycle over
                timer.reset();
            }
        }
    }
}
