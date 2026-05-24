// Question 4 — Bank Account Management System Problem Statement
// Create a Java program for a simple bank account system.
// Requirements- Create a class BankAccount- Add accountNumber, customerName and balance
// Create constructor and display method- Perform deposit and withdraw operations
// Print final balance

import java.util.Scanner;
public class BankAccount {
    private long accountNumber;
    private String customerName;
    private int balance;

    // setters
    BankAccount(int accountNumber, String customerName, int balance){
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    // getters
    void display(){
        System.out.println("Initial Details of customer: ");
        System.out.println("Account Number = "+this.accountNumber);
        System.out.println("Customer Name = "+this.customerName);
        System.out.println("Balance = "+this.balance);
    }

    int deposit(int amount){
       
        return balance += amount;
    }

    int withdraw(int amount){
        System.out.println("Amount to be withdrawal: "+amount);
        if(this.balance > 0 && this.balance > amount){
            return balance -= amount;
        }
        return -1;
    }

    int getBalance(int balance){
        return this.balance;
    }

    public static void main(String[] args){
        Scanner ip = new Scanner(System.in);
        System.out.println("Enter initial balance: ");
        int balance = ip.nextInt();
        
        BankAccount ba1 = new BankAccount(456543249, "Rishiraj Pathak", balance);
        ba1.display();
        System.out.println("Amount to be deposited: ");
        int amount = ip.nextInt();
        System.out.println("Balance after deposit = "+ba1.deposit(amount));
        System.out.println("Amount to be withdrawal: ");
        amount = ip.nextInt();
        System.out.println("Balance after withdraw = "+ba1.withdraw(amount));
        System.out.println("Final Balance = "+ba1.getBalance(ba1.balance));
    }

}
