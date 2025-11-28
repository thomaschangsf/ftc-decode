package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "test2", group = "TeleOp")
public class test2 extends LinearOpMode {
    public void runOpMode() {
        waitForStart();
        motor1.setPower(1);
        }
    }
}   