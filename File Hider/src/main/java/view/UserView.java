package view;

import dao.DataDAO;
import model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;
    public UserView(String email){
        this.email = email;
    }
    public void home() {
        do{
            System.out.println("Welcome " + email);
            System.out.println("Press 1 to show all hidden file");
            System.out.println("Press 2 to hide a new file");
            System.out.println("Press 3 to Unhide a file");
            System.out.println("Press 0 to exit");
            Scanner sc = new Scanner(System.in);
            int ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1 : {
                    try{
                        List<Data> files = DataDAO.getAllFiles(email);
                        System.out.println("ID - File Name");
                        for(Data file : files){
                            System.out.println(file.getId() + " - " +  file.getFileName());
                        }
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    break;
                }

                case 2 : {
                    System.out.println("Enter file path : ");
                    String path = sc.nextLine();
                    File f = new File(path);
                    Data files = new Data(f.getName(), path, email);
                    try {
                        DataDAO.hideFile(files);
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case 3 : {
                    try{
                        List<Data> files = DataDAO.getAllFiles(email);
                        System.out.println("ID - File Name");
                        for(Data file : files){
                            System.out.println(file.getId() + " - " +  file.getFileName());
                        }
                        System.out.println("Enter the id of file to show : ");
                        int id = Integer.parseInt(sc.nextLine());
                        boolean isValidId = false;
                        for(Data file : files){
                            if(file.getId() == id){
                                isValidId = true;
                                break;
                            }
                        }
                        if(isValidId){
                            try {
                                DataDAO.showFile(id);
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Wrong ID");
                        }
                    } catch (SQLException e){
                        e.printStackTrace();
                    }
                    break;
                }

                case 0 : System.exit(0);
            }
        } while(true);
    }
}
