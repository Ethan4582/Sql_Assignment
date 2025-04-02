package project;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.Scanner;



public class Student {

    private static final String DB_NAME = "studentDB";
    private static final String COLLECTION = "students";
    private static MongoCollection<Document> studrec;

    public static void main(String[] args) {
        MongoClient dbClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = dbClient.getDatabase(DB_NAME);
        studrec = database.getCollection(COLLECTION);

        Scanner scann = new Scanner(System.in);

//---------------------------------------------------------------------------------

        if (authenticateUser(scann)) {
            int choice;
            do {
                showMenu();
                choice = scann.nextInt();
                scann.nextLine();

                switch (choice) {
                    case 1:
                        insertStudent(scann);
                        break;
                    case 2:
                        removeStudent(scann);
                        break;
                    case 3:
                        modifyStudent(scann);
                        break;
                    case 4:
                        findStudent(scann);
                        break;
                    case 5:
                        listStudents();
                        break;
                    case 9:
                        System.out.println("Exiting... Thank you for using the system.");
                        dbClient.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } while (choice != 9);
        } else {
            System.out.println("Authentication failed.");
        }
    }


//---------------------------------------------------------------------------------
    

    private static boolean authenticateUser(Scanner scann) {
        System.out.print(" Username: ");
        String usernam = scann.nextLine();

        System.out.print("Enter Password: ");
        String passwrd = scann.nextLine();

        Document user = studrec.find(Filters.and(
                Filters.eq("usernam", usernam),
                Filters.eq("passwrd", passwrd)
        )).first();

        return user != null;
    }



//---------------------------------------------------------------------------------

    public static void showMenu() {
        System.out.println("MENU");
        System.out.println("1: Add stud");
        System.out.println("2: Delete stud");
        System.out.println("3: Update stud");
        System.out.println("4: Search stud");
        System.out.println("5: Display Students");
        System.out.println("9: Exit program");
        System.out.print("Select an option: ");
    }

    private static void insertStudent(Scanner scann) {
        System.out.print("Enter stud ID: ");
        
        int studentID = scann.nextInt();
        
        scann.nextLine();

        System.out.print("Enter stud Name: ");
        String studentName = scann.nextLine();

        System.out.print("Enter Contact Number: ");
        int contactNumber = scann.nextInt();
        scann.nextLine();

        System.out.print(" Username: ");
        String usernam = scann.nextLine();

        System.out.print("Enter Password: ");
        String passwrd = scann.nextLine();

        Document studentDoc = new Document("id", studentID)
                .append("name", studentName)
                .append("contact", contactNumber)
                .append("usernam", usernam)
                .append("passwrd", passwrd);

        studrec.insertOne(studentDoc);
        System.out.println("stud successfully added.");
    }


//---------------------------------------------------------------------------------

    
    private static void removeStudent(Scanner scann) {

        
        System.out.print("Enter stud ID to delete: ");
        int studentID = scann.nextInt();
        Document foundStudent = studrec.find(Filters.eq("id", studentID)).first();
        studrec.deleteOne(Filters.eq("id", studentID));
        if (foundStudent != null) {
            System.out.println("stud removed successfully.");
        } else {
            System.out.println("stud not found.");
        }
    }


////////////---------------------------------------------------------------------------------

    private static void modifyStudent(Scanner scann) {
        System.out.print("Enter stud ID to update: ");
        int studentID = scann.nextInt();
        scann.nextLine();
        Document existingStudent = studrec.find(Filters.eq("id", studentID)).first();
        System.out.print("Enter new Name: ");
        String newName = scann.nextLine();

        System.out.print("Enter new Contact Number: ");
        int newContact = scann.nextInt();

        studrec.updateOne(Filters.eq("id", studentID),
                new Document("$set", new Document("name", newName).append("contact", newContact)));
        if (existingStudent != null) {
            System.out.println("stud details updated successfully.");
        } else {
            System.out.println("stud not found.");
        }
    }


    //---------------------------------------------------------------------------------

    
    private static void findStudent(Scanner scann) {
        System.out.print("Enter stud ID to search: ");
        int studentID = scann.nextInt();
        Document studentData = studrec.find(Filters.eq("id", studentID)).first();
        if (studentData != null) {
            System.out.println("stud Details: " + studentData.toJson());
        } else {
            System.out.println("stud not found.");
        }
    }

    //---------------------------------------------------------------------------------
    private static void listStudents() {
        FindIterable<Document> allstud = studrec.find();
        for (Document student : allstud) {
            System.out.println(student.toJson());
        }
    }
}
