package appTest.test

class Hpgergregre {
    void functionTest(){
        verify("this is verify function")
    }

    private void verify(String verifyString){
        print(verifyString)
    }

    def myMethod() {
        try {
            print("violation")
        } catch(Exception e) {
        }
    }
}
