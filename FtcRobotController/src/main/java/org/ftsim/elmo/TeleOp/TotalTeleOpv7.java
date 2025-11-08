package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TotalTeleOpv7", group="TeleOp")
public class TotalTeleOpv7 extends LinearOpMode
{
    DcMotor motor1;
    DcMotor motor2;
    DcMotor motor3;
    DcMotor motor4;
    DcMotor motor5;
    Servo leftShooterServo;
    Servo rightShooterServo;

    @Override
    public void runOpMode() {

        motor1 = hardwareMap.get(DcMotor.class, "motor1");
        motor2 = hardwareMap.get(DcMotor.class, "motor2");
        motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motor4 = hardwareMap.get(DcMotor.class, "motor4");
        motor5 = hardwareMap.get(DcMotor.class,"motor5");
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");
        telemetry.addData("Status", "Initialized and Ready");
        telemetry.update();

        waitForStart();
        leftShooterServo.setPosition(0);
        rightShooterServo.setPosition(0);

        while(opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;

            double leftPower  = drive + turn;
            double rightPower = drive - turn;
            if(gamepad1.right_trigger > 0.1){
                leftPower = Range.clip(leftPower, -0.5, 0.5);
                rightPower = Range.clip(rightPower, -0.5, 0.5);
            }
            else{
                leftPower = Range.clip(leftPower, -1.0, 1.0);
                rightPower = Range.clip(rightPower, -1.0, 1.0);
            }
            if(gamepad1.right_stick_x > 0.1 && gamepad1.left_stick_x > 0.1){
                motor1.setPower(-1);
                motor4.setPower(1);
                motor2.setPower(1);
                motor5.setPower(-1);
            }
            if(gamepad1.right_stick_x < -0.1 && gamepad1.left_stick_x < -0.1){
                motor1.setPower(-1);
                motor4.setPower(1);
                motor2.setPower(1);
                motor5.setPower(-1);
            }
            else {
                motor1.setPower(leftPower);
                motor2.setPower(rightPower);
                motor4.setPower(leftPower);
                motor5.setPower(rightPower);
            }
            motor3.setPower(0);
            leftShooterServo.setPosition(0);
            rightShooterServo.setPosition(0);

            if(gamepad2.right_trigger > 0.1) {
                motor3.setPower(-0.67);
            }
            else{
                motor3.setPower(0);
            }
            if(gamepad2.left_trigger > 0.1) {
                leftShooterServo.setPosition(-1);
                rightShooterServo.setPosition(1);
                sleep(300);
                leftShooterServo.setPosition(0);
                rightShooterServo.setPosition(0);
            }
        }
    }
}
