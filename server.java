import java.util.Scanner;
import java.io.*;
import java.net.*;

class Limit extends Exception
{ 
Limit(String str)
{
  super(str); 
 }
}

public class server
{
    static ServerSocket ss;
    static DataInputStream dis;
    static DataOutputStream dos;
    static Scanner in = new Scanner(System.in);
    public static user users[] = new user[10];
    public static int totalClientsOnline=0;

    public static void main(String args[]) throws Exception
    {
        try
        {
            int i=0;
            ss = new ServerSocket(7777);

            for(i=0;i<10;i++)
            {
                users[i] = new user(i+1,ss.accept());
                totalClientsOnline++;
            }
            if(i>3)
            {
                throw new Limit("Max server limit Reached"); 
            }
        }
        catch(IOException e)
        {
            System.out.println("Exception caught in main due to user connection loss...");
        }
        catch (Limit ex) 
        { 
            System.out.println(ex.getMessage());
        } 
    }

    public void sendMessageToAll(String msg)
    {
        for(int c=0;c<totalClientsOnline;c++)
        {
            try
            {
                users[c].sendMessage(msg);
            }

            catch(Exception e){}
        }
    } 
}

class user extends Thread
{
    server tirth = new server();
    int userID;
    public Socket userSocket;
    public DataInputStream userDIS;
    public DataOutputStream userDOS;
    public Thread t;
    OutputStream os;


    public user(int id,Socket a)
    {
        try
        {
            userID = id;
            userSocket = a;
            userDIS = new DataInputStream(userSocket.getInputStream());
            userDOS = new DataOutputStream(userSocket.getOutputStream());
            System.out.println(userID+ " client connected.");

            t = new Thread(this);
            t.start();
        }
        catch(Exception e)
              {
                  System.out.println("Exception caught in constructor.");
              }
    }


    public void run()
    {
        Scanner in = new Scanner(System.in);
        String message;
        while(true)
        {
            try
            {
                message = userDIS.readUTF();
                tirth.sendMessageToAll(message);
            }
            catch(Exception e)
            {

            }
        }
    }

    public void sendMessage(String s)
    {
        try
        {
            userDOS.writeUTF(s);
        }

        catch(Exception e){}
    }
}