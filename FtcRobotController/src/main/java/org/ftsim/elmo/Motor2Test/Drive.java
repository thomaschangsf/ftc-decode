package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="motor1")
public class Drive extends OpMode
{
    DcMotor motor;
    DcMotor motor2;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");

        motor.setPower(0);
        motor2.setPower(0);
    }
    @Override
    public void loop(){
        if (gamepad1.left_trigger > 0.1) {
            motor.setPower(1);
        } else if (gamepad1.left_bumper) {
            motor.setPower(-1);
        } else {
            motor.setPower(0);
        }

        if (gamepad1.right_trigger > 0.1) {
            motor2.setPower(1);
        } else if (gamepad1.right_bumper) {
            motor2.setPower(-1);
        } else {
            motor2.setPower(0);
        }
    }
}