package appTest.test

class HelperClass {
    void functionTest(){
        verify("this is verify function")
    }

    private void verify(String verifyString){
        def a = true
        def parmas = a

        if(a){
            return
        }
        print()
    }
}
