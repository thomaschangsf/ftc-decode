package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="BaseShooterPrototype", group="Linear Opmode")
public class BaseShooterPrototype extends LinearOpMode
{
    DcMotor motor3;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        motor3.setPower(0);

        for (int i = 0; i < 3; i++) {
            telemetry.addData("Loop Count", i + 1); // Add telemetry to track loops
            telemetry.update();
            motor3.setPower(-0.58);
            sleep(3000);
            leftShooterServo.setPosition(1);
            rightShooterServo.setPosition(-1);
            sleep(250);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(1);
            sleep(500);
        }
        motor3.setPower(0);
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
    }
}
