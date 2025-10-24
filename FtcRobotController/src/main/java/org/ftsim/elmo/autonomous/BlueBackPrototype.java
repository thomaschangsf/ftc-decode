package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="BlueBackPrototype", group="Linear Opmode")
public class BlueBackPrototype extends LinearOpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);
        motor3.setPower(0);
        motor1.setPower(1);
        motor2.setPower(1);
        sleep(650);
        motor1.setPower(-1);
        motor2.setPower(1);
        sleep(450);
        motor1.setPower(1);
        sleep(500);
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(-1.2);
        sleep(3000);
        for (int i = 0; i < 3; i++) {
            telemetry.addData("Loop Count", i + 1); // Add telemetry to track loops
            telemetry.update();
            leftShooterServo.setPosition(1);
            rightShooterServo.setPosition(-1);
            sleep(1000);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(1);
            sleep(500);
        }
        motor3.setPower(0);
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);


        }
    }
