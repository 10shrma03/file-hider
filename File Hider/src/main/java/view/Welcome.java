package view;

import dao.UserDAO;
import model.User;
import service.GenerateOTP;
import service.SendOTPService;
import service.UserService;

import java.awt.geom.GeneralPath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to app");
        System.out.println("Press 1 to login");
        System.out.println("Press 2 to signUp");
        System.out.println("Press 0 to exit");
        int choice = 0;
        try {
            choice = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (choice){
            case 1 -> login();
            case 2 -> signUp();
            case 0 -> System.exit(0);

        }
    }
    private void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter email : ");
        String email = sc.nextLine();
        try{
            if(UserDAO.isExists(email)){
                String genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email, genOTP);
                System.out.println("Enter OTP : ");
                String OTP = sc.nextLine();
                if(OTP.equals(genOTP)){
                    UserView uv = new UserView(email);
                    uv.home();
                } else {
                    System.out.println("Wrong OTP");
                }
            } else {
                System.out.println("User doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name : ");
        String name = sc.nextLine();
        System.out.println("Enter email : ");
        String email = sc.nextLine();
        try {
            if(UserDAO.isExists(email)){
                System.out.println("User Already exists!!!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email, genOTP);
        System.out.println("Enter OTP : ");
        String OTP = sc.nextLine();
        if(OTP.equals(genOTP)){
            User user = new User(name, email);
            int response = UserService.saveUser(user);
            switch (response) {
                case 0 :
                    System.out.println("User exists");
                    break;
                case 1 :
                    System.out.println("User Registered!!!");
                    break;
                default:
                    System.out.println("Error");
            }
        } else {
            System.out.println("Wrong OTP");
        }
    }
}
