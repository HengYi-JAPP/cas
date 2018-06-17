package com.hengyi.cas.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public final class User {
    static String address = "192.168.0.133";

    public static boolean login(String username, String password) {
        String result = checkuser(username, password);
        if (result == null) {
            return false;
        }
        if (result.length() < 1) {
            return false;
        }
        if (result.charAt(0) != '0') {
            return false;
        }
        return true;
    }

    public static HashMap<String, Object> getAttributes(String username, String password) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String result = checkuser(username, password);
        if (result == null) {
            return null;
        }
        if (result.length() < 1) {
            return null;
        }
        if (result.charAt(0) != '0') {
            return null;
        }
        String values[] = result.substring(2, result.length() - 1).split(",");
        for (int i = 0; i < values.length; i++) {
            String entry[] = values[i].split("=");
            if (entry.length > 1) {
                map.put(entry[0].trim(), entry[1].trim());
            } else {
                map.put(entry[0].trim(), "");
            }
        }
        System.out.println(map.toString());
        return map;
    }

    private static String checkuser(String username, String password) {
        String result = "";
        String port = "8722";

        try {
            Socket socket = new Socket(address, new Integer(port).intValue());
            PrintWriter pwr = new PrintWriter(socket.getOutputStream());
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader read = new BufferedReader(in);
            result = read.readLine();
            String command = "authentication -auto " + username + " " + password;
            pwr.print(command + "\n\r");
            pwr.flush();
            result = read.readLine();
            read.close();
            pwr.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = "1";
        }
        //System.out.println(result);
        return result;
    }

    private static String changePassword(String username, String oldpassword, String newpassword) {
        String result = "";
        String port = "8722";

        try {
            Socket socket = new Socket(address, new Integer(port).intValue());
            PrintWriter pwr = new PrintWriter(socket.getOutputStream());
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            BufferedReader read = new BufferedReader(in);
            result = read.readLine();
            String command = "changepassword " + username + " " + oldpassword + " " + newpassword;
            pwr.print(command + "\n\r");
            pwr.flush();
            result = read.readLine();
            read.close();
            pwr.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = "1";
        }
        //System.out.println(result);
        return result;
    }

    public static void main(String[] argu) {
        getAttributes(argu[0], argu[1]);
    }

    public static boolean change(String username, String oldpassword, String newpassword) {
        String result = changePassword(username, oldpassword, newpassword);
        if (result == null) {
            return false;
        }
        if (result.length() < 1) {
            return false;
        }
        if (result.charAt(0) != '0') {
            return false;
        }
        return true;
    }
}
