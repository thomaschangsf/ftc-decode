package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Ball Shooter", group="ftsim")
public class BallShooter extends LinearOpMode {
    DcMotor shootwheel;
    Servo artifactstopper;
    boolean isShooting = false;
    double shootPower = 0.8;  // Adjust this value (0.0 to 1.0)
    
    @Override
    public void runOpMode() {
        // Initialize hardware
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        
        // Set initial servo position (closed)
        artifactstopper.setPosition(0.2);
        
        telemetry.addData("Status", "Initialized - Press A to shoot");
        telemetry.update();
        
        waitForStart();
        
        while (opModeIsActive()) {
            // Shoot when A button is pressed and not already shooting
            if (gamepad1.a && !isShooting) {
                shoot();
            }
            
            // Adjust shooting power with triggers
            if (gamepad1.right_trigger > 0.1) {
                shootPower = gamepad1.right_trigger;
            }
            
            // Display status
            telemetry.addData("Shooting", isShooting ? "YES" : "NO");
            telemetry.addData("Shoot Power", "%.2f", shootPower);
            telemetry.addData("Controls", "A = Shoot, Right Trigger = Power");
            telemetry.update();
        }
    }
    
    public void shoot() {
        isShooting = true;
        
        // Open stopper and start wheel
        artifactstopper.setPosition(0.0);    // Open
        shootwheel.setPower(shootPower);
        sleep(250);
        
        // Close stopper
        artifactstopper.setPosition(0.2);    // Close
        sleep(200);
        
        // Stop wheel
        shootwheel.setPower(0);
        sleep(1500);
        
        isShooting = false;
    }
}