// Question 6 — Library Book Management System Problem Statement
// Create a Java program for a library management system. Requirements
// Create a class Book- Add bookId, bookName and authorName
// Create constructor and display method- Create 3 book objects
// ake book name from user- 
// Check whether the book is available or not


import java.util.Scanner;

public class lmbs {

    static class Book {
        int bookId;
        String bookName;
        String authorName;

        // constructor of book
        Book(int bookId, String bookName, String authorName) {
            this.bookId = bookId;
            this.bookName = bookName;
            this.authorName = authorName;
        }

        // display Method
        void display() {
            System.out.println("Book ID : " + bookId);
            System.out.println("Book Name : " + bookName);
            System.out.println("Author Name : " + authorName);
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        
        // array to store the book details
        Book[] books = new Book[3];

        for (int i = 0; i < 3; i++) {

            System.out.println("Enter Book ID:");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.println("Enter Book Name:");
            String name = sc.nextLine();

            System.out.println("Enter Author Name:");
            String author = sc.nextLine();

            books[i] = new Book(id, name, author);
        }

        // display all books
        System.out.println("Book Details: ");

        for (int i = 0; i < 3; i++) {
            books[i].display();
        }

        // search book
        System.out.println("Enter book name to search:");
        String searchBook = sc.nextLine();

        boolean found = false;

        for (int i = 0; i < 3; i++) {

            if (books[i].bookName.equalsIgnoreCase(searchBook)) {
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Book is Available");
        } else {
            System.out.println("Book is Not Available");
        }

    }
}