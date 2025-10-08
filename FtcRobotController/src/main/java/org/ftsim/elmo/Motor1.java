package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

// Teleop mode tells program where to find in the Driver station under the teleop tab
// Autonomous mode tells program where to find in the Driver station app
@Autonomous(name="Motor1", group="elmo")
public class Motor1 extends OpMode {
    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor1");
    }
    // Wait for the start button to be pressed


    @Override
    public void loop(){
        motor.setPower(1);
    }
}
