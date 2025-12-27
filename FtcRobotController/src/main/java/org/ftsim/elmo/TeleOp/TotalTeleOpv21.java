package org.ftsim.elmo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "TotalTeleOpv21", group = "TeleOp")
public class TotalTeleOpv21 extends LinearOpMode {

    private static final double MIN_SHOOT_POWER = 0.3;
    private static final double MAX_SHOOT_POWER = 0.92;
    private static final double DEFAULT_SHOOT_POWER = 0.6;

    private DcMotor motorlb;
    private DcMotor motorrb;
    //private DcMotor motor3;
    private DcMotor motorlf;
    private DcMotor motorrf;
    private Servo leftShooterServo;
    private Servo rightShooterServo;

    @Override
    public void runOpMode() {
        motorlb = hardwareMap.get(DcMotor.class, "motorlb");//motor1
        motorrb = hardwareMap.get(DcMotor.class, "motorrb");//motor2
        //motor3 = hardwareMap.get(DcMotor.class, "motor3");
        motorlf = hardwareMap.get(DcMotor.class, "motorlf");//motor4
        motorrf = hardwareMap.get(DcMotor.class, "motorrf");//motor5
        leftShooterServo = hardwareMap.get(Servo.class, "leftShooterServo");
        rightShooterServo = hardwareMap.get(Servo.class, "rightShooterServo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            double leftDrive = -gamepad1.left_stick_y;
            double rightDrive = -gamepad1.right_stick_y;
            double maxPower = gamepad1.right_trigger > 0.1 ? 0.5 : 1.0;

            motorlb.setPower(Range.clip(leftDrive, -maxPower, maxPower));

            double rightPower = Range.clip(rightDrive, -maxPower, maxPower);
            motorrb.setPower(rightPower);
            motorrf.setPower(rightPower);

            if(gamepad1.right_bumper){
                motorlb.setPower(-0.5);
                motorrf.setPower(-0.5);
                motorrb.setPower(0.5);
                motorlf.setPower(-0.5);
            }

            if(gamepad1.left_bumper){
                motorlb.setPower(0.5);
                motorrf.setPower(0.5);
                motorrb.setPower(-0.5);
                motorlf.setPower(0.5);
            }

            else{
                motorlb.setPower(0);
                motorlf.setPower(0);
                motorrf.setPower(0);
                motorrb.setPower(0);
            }

            //orignal loop time 9 secs
            //if (gamepad1.right_trigger > 0.1) { //Shooting loop
            // BALL 1 1250 m;
            //motor3.setPower(-1);
            //sleep(200);
            //motor3.setPower(-0.68);
            //sleep(900);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(350); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            // BALL 2 1025ms
            //motor3.setPower(-0.67); //12.6 volts
            //sleep(800);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(300); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            // BALL 3 1325 ms
            //motor3.setPower(-0.67); //12.6 volts
            //sleep(675);
            //leftShooterServo.setPosition(1); //open to shoot
            //rightShooterServo.setPosition(0);
            //sleep(325); //gives ball time to escape servo
            //leftShooterServo.setPosition(0);//close for next shot
            //rightShooterServo.setPosition(1);

            //sleep(1200);
            //motor3.setPower(0);
            //motor3.setPower(-1);
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
            //motor3.setPower(-1);
            //sleep(200);
            //motor3.setPower(-0.5);
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

