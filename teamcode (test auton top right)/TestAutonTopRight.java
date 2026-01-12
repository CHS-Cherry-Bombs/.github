package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.AprilTagWebcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class TestAutonTopRight extends OpMode {
    double DistanceToScore = 80; //Distance to score from the front of the score zone in CM
    AprilTagWebcam aprilTagWebcam = new AprilTagWebcam();
    public DcMotor BackLeft, BackRight, Intake, Outtake;
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime relativetimer = new ElapsedTime();
    @Override
    public void init() {
        aprilTagWebcam.init(hardwareMap, telemetry);
        BackLeft = hardwareMap.get(DcMotor.class, "Left Drive");
        BackRight = hardwareMap.get(DcMotor.class, "Right Drive");
        Intake = hardwareMap.get(DcMotor.class, "Intake");
        Outtake = hardwareMap.get(DcMotor.class, "Outtake");

        BackRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void Drive(double power) {
        BackLeft.setPower(power);
        BackRight.setPower(power);
    }

    public void AllMotors(double power) {
        BackLeft.setPower(power);
        BackRight.setPower(power);
        Intake.setPower(power);
        Outtake.setPower(power);
    }

    @Override
    public void loop(){
        // update the vision portal
        aprilTagWebcam.update();
        AprilTagDetection id24 = aprilTagWebcam.getTagBySpecifcId(24);
        telemetry.addData("id24 String", id24.toString());
        timer.reset();
        relativetimer.reset();
        AllMotors(0);

        if (id24 == null) {
            Drive(1);
        }
        if (id24.ftcPose.range > DistanceToScore) {
            Drive(1);
        }
        relativetimer.reset();
        if (DistanceToScore > id24.ftcPose.range && relativetimer.time() < 3){
            Drive(0);
            Outtake.setPower(1);
            relativetimer.reset();
            if (0.5 > relativetimer.time() && relativetimer.time() < 3){
                Intake.setPower (1);
            }
        }
    }
}
