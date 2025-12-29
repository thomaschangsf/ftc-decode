package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TotalTeleOpv23", group = "TeleOp")
public class TotalTeleOpv23 extends LinearOpMode {

    private static final double MOTORRF_POWER_BOOST = 2;

    private static final double MOTORLB_POWER_REDUCTION = 0.35;

    private DcMotor motorlb;
    private DcMotor motorrb;
    //private DcMotor motorfw;
    private DcMotor motorlf;
    private DcMotor motorrf;
    private Servo leftShooterServo;
    private Servo rightShooterServo;

    @Override
    public void runOpMode() {
        motorlb = hardwareMap.get(DcMotor.class, "motorlb");//motor1
        motorrb = hardwareMap.get(DcMotor.class, "motorrb");//motor2
        //motorfw = hardwareMap.get(DcMotor.class, "motorfw"); //motor3
        motorlf = hardwareMap.get(DcMotor.class, "motorlf");//motor4
        motorrf = hardwareMap.get(DcMotor.class, "motorrf");//motor5
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;

            double maxPower = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            double motorrbPower = Range.clip(drive - turn, -maxPower, maxPower);
            motorrb.setPower(motorrbPower);

            double motorrfPower;
            if (Math.abs(drive) > 0.01) {
                motorrfPower = Range.clip(motorrbPower + (motorrbPower >= 0 ? MOTORRF_POWER_BOOST : -MOTORRF_POWER_BOOST), -maxPower, maxPower);
            }
            else {
                motorrfPower = motorrbPower;
            }
            motorrf.setPower(motorrfPower);

            double motorlbPower;
            if (Math.abs(turn) > 0.01) {
                motorlbPower = Range.clip(drive + turn, -maxPower, maxPower);
            }
            else if (Math.abs(drive) > 0.01) {
                motorlbPower = Range.clip(motorrbPower - MOTORLB_POWER_REDUCTION, -maxPower, maxPower);
            } else {
                motorlbPower = motorrbPower;
            }
            motorlb.setPower(motorlbPower);

            double motorlfPower = Range.clip(drive + turn, -maxPower, maxPower);
            motorlf.setPower(-motorlfPower);

            if(gamepad1.right_bumper){
                motorlb.setPower(-0.5);
                motorrf.setPower(-0.8);
                motorrb.setPower(0.8);
                motorlf.setPower(-0.5);
            }

            if(gamepad1.left_bumper){
                motorlb.setPower(0.5);
                motorrf.setPower(0.8);
                motorrb.setPower(-0.8);
                motorlf.setPower(0.5);
            }

            else{
                motorlb.setPower(0);
                motorrf.setPower(0);
                motorrb.setPower(0);
                motorlf.setPower(0);
            }

            //orignal loop time 9 secs
            //if (gamepad1.right_trigger > 0.1) { //Shooting loop
            // BALL 1 1250 m;
            //motorfw.setPower(-1);
            //sleep(200);
            //motorfw.setPower(-0.68);
            //sleep(900);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(350); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            // BALL 2 1025ms
            //motorfw.setPower(-0.67); //12.6 volts
            //sleep(800);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(300); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            // BALL 3 1325 ms
            //motorfw.setPower(-0.67); //12.6 volts
            //sleep(675);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(325); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            //sleep(1200);
            //motorfw.setPower(0);
            //motorfw.setPower(-1);
            //sleep(100);
            //To much power, shoots to high, to little, shoots to horizontal


            // give ball to fly and flywhell to regain speed
            //sleep(400);
            //sleep(300 + i*100);
            // Too low. 200,300 --> collide or no power

            //}

            //}

            //if(gamepad.left_trigger > 0.1){
            // BALL 1 1400 m;
            //motorfw.setPower(-1);
            //sleep(200);
            //motorfw.setPower(-0.5);
            //sleep(900);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(350); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);
            //}

            // Update telemetry to display current status
            telemetry.update();
        }
    }
}

