package appTest.test

class HelperClass {
    void functionTest(){
        verify("this is verify function")
    }

    private void verify(String verifyString){



        try{
            List<Integer> b = [1,2,3];

            switch( 0 ) {
                case 1: break;
                case 2: break;
                case 2: break;          // violation
            }
        }catch(Exception e){

        }
    }
}
