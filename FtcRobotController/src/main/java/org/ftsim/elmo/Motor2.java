package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="motor1")
r\\@Autonomous(name="motor2")
public class Motor2 extends OpMode
{
    DcMotor motor;
    DcMotor motor2;

    @Override
    public void init() {
        motor1 = hardwareMap.get(DcMotor.class, "motor1");

    }


    @Override
    public void loop(){
        motor.setPower(gamepad1.left_trigger);
    }
    }

