package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="motor1")
public class Java extends OpMode
{
    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor1");
    }


    @Override
    public void loop(){
        while(gamepad1.left_trigger > 0){
            motor.setPower(1);
        }
        while(gamepad1.left_bumper == true){
            motor.setPower(-1);
        }
    }
}