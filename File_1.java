import java.sql.*;
import java.util.Scanner;

public class File_1 {
    private static final String url = "jdbc:postgresql://localhost:5432/Hotel_db";
    private static final String username = "postgres";
    private static final String password = "#Suraj5007";
    public static void main(String[] args) throws ClassNotFoundException, SQLException{
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver connection formed");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println("Database connection formed");
                System.out.println("Welcome to Hotel Reservation System");
                System.out.println("1. Book room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("6. Exit");
                System.out.print("\nEnter operation you want to perform: ");
                Scanner sc = new Scanner(System.in);
                int index = sc.nextInt();

                switch(index){
                    case 1:
                        reservation(con,sc);
                        break;
                    case 2:
                        view(con);
                        break;
                    case 3:
                        roomInfo(con,sc);
                        break;
                    case 4:
                        update(con,sc);
                        break;
                    case 5:
                        delete(con,sc);
                        break;
                    case 6:
                        exit();
                        sc.close();
                        break;
                    default:
                        System.out.println("Invalid expression-----Try again.");
                }
                break;

            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
//        catch (InterruptedException e){
//            throw new RuntimeException(e);
//        }
    }
    private static void reservation(Connection con, Scanner sc){
        System.out.println("Enter Id");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter guest name: ");
        String name = sc.next();
        sc.nextLine();
        System.out.print("Enter room number: ");
        int room = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter contact number");
        String number = sc.next();

        System.out.println("Enter a date (format: yyyy-MM-dd): ");
        String dateString = sc.next();

        String sql = "INSERT INTO reservations(id,guest_name, room_number, contact_number, reservation_date) \n"+
                "VALUES ('" + id +"', '"+name+"', '"+room+"','"+number+"','"+dateString+"')";
        try(Statement st = con.createStatement()){
            int rowsAffected = st.executeUpdate(sql);
            if(rowsAffected>0){
                System.out.println("Reservation Successful!!!");
            }
            else{
                System.out.println("Reservation Failed.");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void view(Connection con){
        String sql = "select id, guest_name, room_number, contact_number, reservation_date from reservations";
        try(Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("guest_name");
                int room_no = rs.getInt("room_number");
                String contact = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        id,name,room_no,contact,date);
            }
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("Table displayed successfully");

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    private static void roomInfo(Connection con, Scanner sc){
        System.out.println("Enter guest id: ");
        int id = sc.nextInt();
        String sql = "select id, guest_name, room_number, contact_number, reservation_date from reservations where id = "+id;
        try(Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                int Id = rs.getInt("id");
                String name = rs.getString("guest_name");
                int room_no = rs.getInt("room_number");
                String contact = rs.getString("contact_number");
                String date = rs.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        Id,name,room_no,contact,date);
            }


        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }
    private static void update(Connection con, Scanner sc){
        System.out.print("Enter id to update details: ");
        int id = sc.nextInt();
        if(!reservationExists(con,id)){
            System.out.println("Id does not exists!!!");
        }
        System.out.print("Enter new id: ");
        int Id = sc.nextInt();
        System.out.print("Enter new name: ");
        String name = sc.next();
        System.out.print("Enter new room number:");
        int room_no = sc.nextInt();
        System.out.print("Enter new contact number: ");
        String contact = sc.next();
        System.out.print("Enter new reservation date: ");
        String date = sc.next();

        String sql = "update reservations set id = '"+Id+ "', "+
                "guest_name = '"+name+ "', "+
                "room_number = '"+room_no+ "', "+
                "contact_number = '"+contact+ "',"+
                "reservation_date = '"+date+"' "+
                "where id= "+id;
        try(Statement st = con.createStatement()){
            int rowsAffected = st.executeUpdate(sql);
            if(rowsAffected>0){
                System.out.println("Updation successful!!!");
            }
            else{
                System.out.println("Updation fail");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    private static  boolean reservationExists(Connection con, int id){
        String sql = "select id from reservations where id = "+id;
        try(Statement st = con.createStatement()){
            ResultSet rs = st.executeQuery(sql);
            return rs.next();
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;

        }
    }

    private static void delete(Connection con, Scanner sc){
        System.out.print("Enter guest id to delete: ");
        int id = sc.nextInt();
        if(reservationExists(con,id)){
            String sql = "delete from reservations where id = "+id;
            try(Statement st = con.createStatement()){
                int rowsAffected = st.executeUpdate(sql);
                if(rowsAffected>0){
                    System.out.println("Guest details deleted successfully!!!");
                }
                else{
                    System.out.println("Problem detected to delete details");
                }
            }
            catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Entered guest id does not exists!!!");
        }
    }
    private static void exit(){
        int i=5;
        System.out.print("Existing Application");
        while(i!=0){
            try{
                System.out.print(".");
                Thread.sleep(1000);
                i--;
            }
            catch (InterruptedException e){
                System.out.println(e.getMessage());
            }
        }
        System.out.print("\nThank you for using Hotel Booking Application!!!");
    }

}
