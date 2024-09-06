package appTest.common

import groovy.appTest.RunWeb

//This is the correct comment check
//@author ttoanle
class HpHomePage {
    RunWeb r = new RunWeb()
    String query = "SELECT * FROM dbo.UserTable Where User = 1234;"
    String query2 = "SELECT * FROM dbo.UserTable Where User = 1234;"

    static void main(String[] args) {
        String queryInMethod = "SELECT * FROM dbo.UserTable Where User = 1234;"
        print("This is the app function class")
    }
}
