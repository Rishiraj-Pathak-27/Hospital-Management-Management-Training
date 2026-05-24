// Question 5 — Online Food Ordering System Problem Statement
// Create a Java program for a simple food ordering system.
// Requirements- Display food menu using switch-case
// Take food choice and quantity- Calculate total bill
// Apply 5% discount if bill is greater than 1000
// Print food item, quantity, total bill and final bill

import java.util.Scanner;
public class ofos{

    String foodMenu;
    
    public void display(){
        int choice;
        System.out.println("Menu Items: ");
        System.out.println("Hara Bhara Kabab");
        System.out.println("Crispy Veg");
        System.out.println("Paneer Butter Masala");
        System.out.println("Palak Panner");
        System.out.println("Samosa");
        System.out.println("Dahi Vada");
        System.out.println();
    }

    public String getMenu(int choice){
        switch(choice){
            case 1 : {
                return "Hara Bhara Kabab";
            }

            case 2 : {
                return "Crispy Veg";
            }

            case 3 : {
                return "Paneer Butter Masala";
            }

            case 4 : {
                return "Palak Panner";
            }

            case 5 : {
                return "Samosa";
            }
            case 6 : {
                return "Dahi vada";
            }
            default : {
                return "Invalid Choice";
            }
        }
    }

    public int getPrice(int choice){
        switch(choice) {
            case 1 : {
                return 350;
            }

            case 2 : {
                return 300;
            }

            case 3 : {
                return 280;
            }

            case 4 : {
                return 250;
            }

            case 5 : {
                return 50;
            }

            case 6 : {
                return 150;
            }

            default : {
                return -1;
            }
        }
    }

    // total bill
    public int totalBill(int choice, int quantity){
        int price = getPrice(choice);
        int bill = price * quantity;
        return bill;
    }


    // after discount
    public double calculateFinalBill(int choice, int quantity){
        int bill = totalBill(choice, quantity);
        
        double finalBill = bill;
        if(bill > 1000){
            finalBill = bill * 0.95;
        }
        return finalBill;
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ofos o = new ofos();

        o.display();

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        
        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        System.out.println("\nFood Item Selected: " + o.getMenu(choice));
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Bill = " + o.totalBill(choice, quantity));
        System.out.println();

        if(o.calculateFinalBill(choice, quantity) > 1000){
            System.out.println("Hurray Discount applied !!!");
            System.out.println("Final Bill (after discount) = " + o.calculateFinalBill(choice, quantity));
        }else{
            System.out.println("No Discount on order < 1000rs");
            System.out.println("Final Bill = " + o.calculateFinalBill(choice, quantity));
        }

    }
}