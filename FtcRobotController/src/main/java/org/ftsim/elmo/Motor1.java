package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

// Teleop mode tells program where to find in the Driver station under the teleop tab
// Autonomous mode tells program where to find in the Driver station app
@Autonomous(name="Motor1", group="ftsim")
public class Motor1 extends LinearOpMode {
    DcMotor motor;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // Wait for the start button to be pressed
        waitForStart();
        
        // Run the motor for 3 seconds
        motor.setPower(1);
        sleep(30000);
        motor.setPower(0);
    }
}
