package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="TeleOp1", group="ftsim")
public class TeleOp1 extends OpMode {
    DcMotor leftMotor;
    DcMotor rightMotor;
    Servo servo;
    
    @Override
    public void init() {
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        servo = hardwareMap.get(Servo.class, "servo");
        
        telemetry.addData("Status", "Initialized");
    }
    
    @Override
    public void loop() {
        // Tank drive controls
        double leftPower = gamepad1.left_stick_y;
        double rightPower = gamepad1.right_stick_y;
        
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
        
        // Servo control with buttons
        // Servo moves to exact angles, typiccally 180-270 degrees
        if (gamepad1.a) {
            servo.setPosition(0.0);  // Servo to position 0; ie close gripper
                                     // 0.5 --> 90 degrees; 1 --> 180 degrees
        } else if (gamepad1.b) {
            servo.setPosition(1.0);  // Servo to position 1
        }
        
        // Motor control with triggers
        if (gamepad1.left_trigger > 0.1) {
            leftMotor.setPower(gamepad1.left_trigger);
        }
        if (gamepad1.right_trigger > 0.1) {
            rightMotor.setPower(gamepad1.right_trigger);
        }
        
        // Display telemetry
        telemetry.addData("Left Motor", "%.2f", leftPower);
        telemetry.addData("Right Motor", "%.2f", rightPower);
        telemetry.addData("Left Trigger", "%.2f", gamepad1.left_trigger);
        telemetry.addData("Right Trigger", "%.2f", gamepad1.right_trigger);
        telemetry.addData("A Button", gamepad1.a);
        telemetry.addData("B Button", gamepad1.b);
        telemetry.update();
    }
}