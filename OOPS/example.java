// encapsulation

// public class example{
//     private static void animal(int legs){
//         private int legs;
//     }

//     public static void main(String[] args){

//     }
// }

// polymorphism

public class example{

    public static class animals{
        void animal(){
            System.out.println("Most Animal has 4 legs");
        }
    }

    public static class cat{
        void animal(){
            System.out.println("Cat meows");
        }
    }

    public static class dog{
        void animal(){
            System.out.println("Dogs bhow");
        }
    }

    public static void main(String[] args){
        cat c = new cat();
        dog d = new dog();
        animals a = new animals();

        c.animal();
        d.animal();
        a.animal();
    }
}
